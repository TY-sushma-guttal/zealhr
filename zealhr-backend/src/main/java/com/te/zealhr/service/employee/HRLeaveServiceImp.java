package com.te.zealhr.service.employee;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.zealhr.dto.DashboardRequestDTO;
import com.te.zealhr.entity.admin.CompanyHolidayDetails;
import com.te.zealhr.entity.admin.CompanyInfo;
import com.te.zealhr.entity.admin.CompanyWorkWeekRule;
import com.te.zealhr.entity.admin.WorkOffDetails;
import com.te.zealhr.entity.employee.EmployeeLeaveApplied;
import com.te.zealhr.entity.employee.EmployeeOfficialInfo;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.employee.mongo.EmployeeAttendanceDetails;
import com.te.zealhr.exception.DataNotFoundException;
import com.te.zealhr.repository.admin.CompanyHolidayDetailsRepository;
import com.te.zealhr.repository.admin.CompanyInfoRepository;
import com.te.zealhr.repository.employee.EmployeeLeaveAppliedRepository;
import com.te.zealhr.repository.employee.mongo.EmployeeAttendanceDetailsRepository;

@Service
public class HRLeaveServiceImp implements HRLeaveService {

	@Autowired
	private CompanyHolidayDetailsRepository companyHolidayDetailsRepository;

	@Autowired
	private EmployeeLeaveAppliedRepository employeeLeaveAppliedRepository;

	@Autowired
	private CompanyInfoRepository companyInfoRepository;

	@Autowired
	private EmployeeAttendanceDetailsRepository employeeAttendanceDetailsRepository;

	private static final String WORKING_DAYS = "Working Days";

	private static final String ABSENT = "Absent";

	@Override
	public List<Map<String, String>> getLeaveDetails(DashboardRequestDTO dashboardRequestDTO, Long companyId) {
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new DataNotFoundException("Company Not Found"));
		List<Map<String, String>> employeePunctualPerformanceDTOList = new ArrayList<>();
		LocalDate date = LocalDate.now();
		Integer year = date.getYear();
		LocalDate startDate = LocalDate.of(year, dashboardRequestDTO.getMonth(), 1);
		LocalDate endDate = LocalDate.of(year, dashboardRequestDTO.getMonth(),
				startDate.getMonth().length(startDate.isLeapYear()));

		List<LocalDate> holidayList = companyHolidayDetailsRepository
				.findByHolidayDateBetweenAndCompanyInfoCompanyId(startDate, endDate, companyInfo.getCompanyId())
				.orElse(new ArrayList<>()).stream().map(CompanyHolidayDetails::getHolidayDate)
				.collect(Collectors.toList());
		List<EmployeePersonalInfo> employeePersonalInfoList = companyInfo.getEmployeePersonalInfoList().stream()
				.filter(EmployeePersonalInfo::getIsActive).collect(Collectors.toList());
		if (dashboardRequestDTO.getDepartmentList() != null && !dashboardRequestDTO.getDepartmentList().isEmpty()) {
			employeePersonalInfoList = employeePersonalInfoList.stream()
					.filter(employee -> employee.getEmployeeOfficialInfo() != null
							&& employee.getEmployeeOfficialInfo().getDepartment() != null
							&& dashboardRequestDTO.getDepartmentList()
									.contains(employee.getEmployeeOfficialInfo().getDepartment()))
					.collect(Collectors.toList());
		}
		for (EmployeePersonalInfo employeePersonalInfo : employeePersonalInfoList) {
			employeePunctualPerformanceDTOList
					.add(getMonthDetails(holidayList, employeePersonalInfo, startDate, endDate, companyInfo, date));
		}
		return employeePunctualPerformanceDTOList;
	}

	private Map<String, String> getMonthDetails(List<LocalDate> holidayList, EmployeePersonalInfo employeePersonalInfo,
			LocalDate startDate, LocalDate endDate, CompanyInfo companyInfo, LocalDate date) {

		Map<String, String> result = getMap(holidayList, employeePersonalInfo);
		EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();

		if (employeeOfficialInfo != null) {

			CompanyWorkWeekRule companyWorkWeekRule = Optional
					.ofNullable(employeeOfficialInfo.getCompanyWorkWeekRule() == null).filter(rule -> rule)
					.map(x -> Optional
							.ofNullable(companyInfo.getCompanyWorkWeekRuleList().stream()
									.filter(CompanyWorkWeekRule::getIsDefault).collect(Collectors.toList()))
							.filter(f -> !f.isEmpty()).map(y -> y.get(0)).orElseGet(CompanyWorkWeekRule::new))
					.orElse(employeeOfficialInfo.getCompanyWorkWeekRule());

			if (companyWorkWeekRule.getWorkOffDetailsList() != null) {
				Double halfWeekOffCount = 0.0;
				Double fullWeekOffCount = 0.0;
				List<WorkOffDetails> workOffDetailsList = companyWorkWeekRule.getWorkOffDetailsList();
				Map<Integer, List<String>> weekOffName = workOffDetailsList.stream().collect(Collectors
						.toMap(WorkOffDetails::getWeekNumber, WorkOffDetails::getFullDayWorkOff, (k1, k2) -> k1));
				Map<Integer, List<String>> halfDayWeekOffName = workOffDetailsList.stream().collect(Collectors
						.toMap(WorkOffDetails::getWeekNumber, WorkOffDetails::getHalfDayWorkOff, (k1, k2) -> k1));

				List<LocalDate> datesUntil = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
				for (LocalDate localDate : datesUntil) {
					List<String> weekOff = weekOffName
							.get(Integer.valueOf(localDate.get(ChronoField.ALIGNED_WEEK_OF_MONTH)));
					List<String> halfWeekOff = halfDayWeekOffName
							.get(Integer.valueOf(localDate.get(ChronoField.ALIGNED_WEEK_OF_MONTH)));
					if (weekOff != null && weekOff
							.contains(Arrays.stream(localDate.getDayOfWeek().toString().substring(0, 3).split("\\s+"))
									.map(name -> name.substring(0, 1).toUpperCase()
											+ name.substring(1, name.length()).toLowerCase())
									.collect(Collectors.joining(" ")))) {
						fullWeekOffCount = fullWeekOffCount + 1;
					}
					if (halfWeekOff != null && halfWeekOff
							.contains(Arrays.stream(localDate.getDayOfWeek().toString().substring(0, 3).split("\\s+"))
									.map(name -> name.substring(0, 1).toUpperCase()
											+ name.substring(1, name.length()).toLowerCase())
									.collect(Collectors.joining(" ")))) {
						halfWeekOffCount = halfWeekOffCount + 1;
					}
				}
				Double approvedLeaveCount = employeeLeaveAppliedRepository
						.findByStatusIgnoreCaseAndEmployeePersonalInfoEmployeeInfoIdAndStartDateBetween("Approved",
								employeePersonalInfo.getEmployeeInfoId(), startDate, endDate)
						.stream().map(EmployeeLeaveApplied::getLeaveDuration).reduce(0.0, (a, b) -> (a + b));

				Optional<EmployeeAttendanceDetails> attendanceOptional = employeeAttendanceDetailsRepository
						.findByEmployeeInfoIdAndMonthNoAndYear(employeePersonalInfo.getEmployeeInfoId(),
								date.getMonthValue(), date.getYear());
				Double present = 0.0;
				if (attendanceOptional.isPresent() && attendanceOptional.get().getAttendanceDetails() != null) {
					present = Double.valueOf(attendanceOptional.get().getAttendanceDetails().stream()
							.filter(attendance -> attendance.getPunchIn() != null && attendance.getPunchOut() != null
									&& attendance.getIsInsideLocation() != null
									&& (attendance.getIsInsideLocation()
											|| (attendance.getIsInsideLocation().equals(Boolean.FALSE)
													&& attendance.getStatus() != null
													&& attendance.getStatus().containsKey("Approved"))))
							.count());
				}

				result.put("Approved Leave", String.valueOf(approvedLeaveCount));
				result.put(WORKING_DAYS, String.valueOf(
						date.lengthOfMonth() - holidayList.size() - fullWeekOffCount - (halfWeekOffCount / 2)));
				result.put(ABSENT, String.valueOf(Double.valueOf(result.get(WORKING_DAYS)) - present));
				result.put("LOP",
						(Double.valueOf(result.get(ABSENT)) - approvedLeaveCount) > 0
								? String.valueOf(Double.valueOf(result.get(ABSENT)) - approvedLeaveCount)
								: String.valueOf(0));
			}
		}
		return result;
	}

	private Map<String, String> getMap(List<LocalDate> holidayList, EmployeePersonalInfo employeePersonalInfo) {
		return new LinkedHashMap<String, String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("Name", employeePersonalInfo.getFirstName());
				put("Employee Id", employeePersonalInfo.getEmployeeOfficialInfo() == null ? null
						: employeePersonalInfo.getEmployeeOfficialInfo().getEmployeeId());
				put("Department", employeePersonalInfo.getEmployeeOfficialInfo() == null ? null
						: employeePersonalInfo.getEmployeeOfficialInfo().getDepartment());
				put("Designation", employeePersonalInfo.getEmployeeOfficialInfo() == null ? null
						: employeePersonalInfo.getEmployeeOfficialInfo().getDesignation());
				put("Holiday", String.valueOf(holidayList.size()));
				put("Approved Leave", String.valueOf(0));
				put(WORKING_DAYS, String.valueOf(0));
				put(ABSENT, String.valueOf(0));
				put("LOP", String.valueOf(0));
			}
		};
	}

}
