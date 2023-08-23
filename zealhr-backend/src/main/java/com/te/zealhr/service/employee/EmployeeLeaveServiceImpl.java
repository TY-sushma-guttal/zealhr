package com.te.zealhr.service.employee;

import static com.te.zealhr.common.employee.EmployeeLeaveConstants.LEAVE_DURATION_FULL;
import static com.te.zealhr.common.employee.EmployeeLeaveConstants.LEAVE_DURATION_HALF;
import static com.te.zealhr.common.employee.EmployeeLeaveConstants.LEAVE_STATUS_ALL;
import static com.te.zealhr.common.employee.EmployeeLeaveConstants.LEAVE_STATUS_APPLIED;
import static com.te.zealhr.common.employee.EmployeeLeaveConstants.LEAVE_STATUS_APPROVED;
import static com.te.zealhr.common.employee.EmployeeLeaveConstants.LEAVE_STATUS_HOLIDAY;
import static com.te.zealhr.common.employee.EmployeeLeaveConstants.LEAVE_STATUS_INVALID;
import static com.te.zealhr.common.employee.EmployeeLeaveConstants.LEAVE_STATUS_PENDING;
import static com.te.zealhr.common.employee.EmployeeLeaveConstants.LEAVE_STATUS_REJECTED;
import static java.time.temporal.ChronoUnit.MINUTES;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.AtomicDouble;
import com.te.zealhr.dto.employee.EmployeeAllotedLeavesDTO;
import com.te.zealhr.dto.employee.EmployeeApplyLeaveDTO;
import com.te.zealhr.dto.employee.EmployeeCalanderDetailsDTO;
import com.te.zealhr.dto.employee.EmployeeCalenderLeaveInfoDTO;
import com.te.zealhr.dto.employee.EmployeeDropdownDTO;
import com.te.zealhr.dto.employee.EmployeeLeaveDTO;
import com.te.zealhr.dto.hr.CanlenderRequestDTO;
import com.te.zealhr.entity.admin.CompanyHolidayDetails;
import com.te.zealhr.entity.admin.CompanyInfo;
import com.te.zealhr.entity.admin.CompanyRuleInfo;
import com.te.zealhr.entity.admin.CompanyShiftInfo;
import com.te.zealhr.entity.admin.WorkOffDetails;
import com.te.zealhr.entity.employee.EmployeeLeaveAllocated;
import com.te.zealhr.entity.employee.EmployeeLeaveApplied;
import com.te.zealhr.entity.employee.EmployeeOfficialInfo;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.employee.EmployeeReportingInfo;
import com.te.zealhr.exception.DataNotFoundException;
import com.te.zealhr.exception.EmployeeNotFoundException;
import com.te.zealhr.exception.LeaveIdNotFoundException;
import com.te.zealhr.exception.employee.InsufficientLeavesException;
import com.te.zealhr.repository.admin.CompanyHolidayDetailsRepository;
import com.te.zealhr.repository.employee.EmployeeLeaveAllocatedRepository;
import com.te.zealhr.repository.employee.EmployeeLeaveAppliedRepository;
import com.te.zealhr.repository.employee.EmployeePersonalInfoRepository;
import com.te.zealhr.repository.employee.EmployeeReportingInfoRepository;
import com.te.zealhr.service.hr.CompanyEventDetailsServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeLeaveServiceImpl implements EmployeeLeaveService {

	@Autowired
	private EmployeeLeaveAppliedRepository leaveAppliedRepository;

	@Autowired
	private EmployeePersonalInfoRepository personalInfoRepository;

	@Autowired
	private EmployeeLeaveAllocatedRepository allocatedRepository;

	@Autowired
	private CompanyHolidayDetailsRepository companyHolidayDetailsRepository;

	@Autowired
	private EmployeeReportingInfoRepository employeeReportingInfoRepository;

	@Autowired
	private CompanyEventDetailsServiceImpl companyEventDetailsServiceImpl;

	@Autowired
	private EmployeeLoginServiceImpl employeeLoginServiceImpl;

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

	// To add new leave request
	@Override
	public EmployeeApplyLeaveDTO saveLeaveApplied(EmployeeApplyLeaveDTO employeeDTO, Long employeeInfoId) {

		if (employeeDTO.getEndDate().isBefore(employeeDTO.getStartDate())) {
			throw new LeaveIdNotFoundException("End date cannot be after Start date");
		}

		EmployeePersonalInfo employeeInfo = personalInfoRepository.findById(employeeInfoId).orElse(null);

		if (employeeInfo == null) {
			throw new EmployeeNotFoundException("Employee not found with id :" + employeeInfoId);
		}

		CompanyShiftInfo companyShiftInfo = employeeInfo.getEmployeeOfficialInfo().getCompanyShiftInfo();
		if (companyShiftInfo == null) {
			throw new DataNotFoundException("Shift is not Assigned");
		}

		CompanyInfo companyInfo = employeeInfo.getCompanyInfo();
		CompanyRuleInfo companyRuleInfo = companyInfo == null ? null : companyInfo.getCompanyRuleInfo();
		if (companyRuleInfo == null) {
			throw new DataNotFoundException("Company Rules Not Defined");
		}

		EmployeeLeaveAllocated leaveAlloted = allocatedRepository
				.findByEmployeePersonalInfoEmployeeInfoId(employeeInfoId)
				.orElseThrow(() -> new DataNotFoundException("No Leave Allocated for the Employee"));
		double shiftMinutes = companyShiftInfo.getLoginTime().until(companyShiftInfo.getLogoutTime(), MINUTES);
		List<LocalDate> startEndDate = employeeLoginServiceImpl.getStartEndDate(LocalDate.now(),
				companyRuleInfo.getFiscalYear());
		List<EmployeeLeaveApplied> employeeLeaveAppliedList = leaveAppliedRepository
				.findByEmployeePersonalInfoEmployeeInfoIdAndStartDateBetween(employeeInfo.getEmployeeInfoId(),
						startEndDate.get(0), startEndDate.get(1));
		Double appliedCount = employeeLeaveAppliedList.stream()
				.filter(e -> e.getRejectionReason() != null && e.getLeaveOfType().equals(employeeDTO.getLeaveOfType()))
				.map(EmployeeLeaveApplied::getLeaveDuration).reduce(0.0, (a, b) -> (a + b));
		String allotedLeavecount = leaveAlloted.getLeavesDetails().get(employeeDTO.getLeaveOfType());

		if (allotedLeavecount == null) {
			throw new DataNotFoundException("No leaves alloted for this category");
		}

		EmployeeOfficialInfo employeeOfficialInfo = employeeInfo.getEmployeeOfficialInfo();

		if (employeeOfficialInfo.getCompanyWorkWeekRule() == null) {
			throw new LeaveIdNotFoundException("Employee Does Not Have Any Work Week Rule");
		}

		List<WorkOffDetails> employeeWorkOffDetails = employeeOfficialInfo.getCompanyWorkWeekRule()
				.getWorkOffDetailsList();
		Double currentLeave;
		Double availableLeaves = Double.parseDouble(allotedLeavecount) - appliedCount.doubleValue();

		AtomicDouble[] leaveCounts = currentLeaveCount(employeeDTO, employeeWorkOffDetails);

		double leaveCount = leaveCounts[0].doubleValue();

		if (employeeDTO.getLeaveType().equalsIgnoreCase(LEAVE_DURATION_FULL)) {
			currentLeave = 1.0 * leaveCount;
		} else if (employeeDTO.getLeaveType().equalsIgnoreCase(LEAVE_DURATION_HALF)) {
			currentLeave = 0.5 * leaveCount;
		} else {
			double leaveMinutes = employeeDTO.getStartTime().until(employeeDTO.getEndTime(), MINUTES);
			currentLeave = Math.round((leaveMinutes / shiftMinutes) * leaveCount * 100) / 100.0;
		}

		if (currentLeave + leaveCounts[1].doubleValue() > availableLeaves) {
			throw new InsufficientLeavesException("Insufficient Leaves for current type");
		}

		EmployeeLeaveApplied leaveApplied = new EmployeeLeaveApplied();
		BeanUtils.copyProperties(employeeDTO, leaveApplied);
		leaveApplied.setLeaveDuration(currentLeave + leaveCounts[1].doubleValue());
		leaveApplied.setEmployeePersonalInfo(employeeInfo);
		if (employeeDTO.getReportingId() != null) {
			EmployeeReportingInfo optionalReport = employeeReportingInfoRepository
					.findById(employeeDTO.getReportingId())
					.orElseThrow(() -> new DataNotFoundException("Reporting Manager Not Found"));
			leaveApplied.setEmployeeReportingInfo(optionalReport);
		}
		leaveApplied.setStatus(LEAVE_STATUS_PENDING);
		BeanUtils.copyProperties(leaveAppliedRepository.save(leaveApplied), employeeDTO);
		return employeeDTO;
	}

	@Override
	public EmployeeApplyLeaveDTO editLeave(EmployeeApplyLeaveDTO employeeDTO, Long leaveAppliedId,
			Long employeeInfoId) {
		EmployeeLeaveApplied employeeLeaveApplied = leaveAppliedRepository
				.findByLeaveAppliedIdAndEmployeePersonalInfoEmployeeInfoId(leaveAppliedId, employeeInfoId).orElse(null);

		if (employeeLeaveApplied == null) {
			throw new LeaveIdNotFoundException("Leave Request Not Available");
		}
		EmployeePersonalInfo employeeInfo = employeeLeaveApplied.getEmployeePersonalInfo();
		if (!employeeLeaveApplied.getStatus().equalsIgnoreCase(LEAVE_STATUS_PENDING)) {
			throw new LeaveIdNotFoundException("Unable to edit leave request!!! Kindly rasie a new request.");
		}

		CompanyInfo companyInfo = employeeInfo.getCompanyInfo();
		CompanyRuleInfo companyRuleInfo = companyInfo == null ? null : companyInfo.getCompanyRuleInfo();
		if (companyRuleInfo == null) {
			throw new DataNotFoundException("Company Rules Not Defined");
		}

		List<LocalDate> startEndDate = employeeLoginServiceImpl.getStartEndDate(LocalDate.now(),
				companyRuleInfo.getFiscalYear());

		List<EmployeeLeaveApplied> employeeLeaveAppliedList = leaveAppliedRepository
				.findByEmployeePersonalInfoEmployeeInfoIdAndStartDateBetween(employeeInfo.getEmployeeInfoId(),
						startEndDate.get(0), startEndDate.get(1));

		Double appliedCount = employeeLeaveAppliedList.stream()
				.filter(e -> (!e.getLeaveAppliedId().equals(leaveAppliedId)) && e.getRejectionReason() != null
						&& e.getLeaveOfType().equals(employeeDTO.getLeaveOfType()))
				.map(EmployeeLeaveApplied::getLeaveDuration).reduce(0.0, (a, b) -> (a + b))
				- employeeLeaveApplied.getLeaveDuration();

		CompanyShiftInfo companyShiftInfo = employeeInfo.getEmployeeOfficialInfo().getCompanyShiftInfo();
		double shiftMinutes = companyShiftInfo.getLoginTime().until(companyShiftInfo.getLogoutTime(), MINUTES);
		EmployeeLeaveAllocated leaveAlloted = allocatedRepository
				.findByEmployeePersonalInfoEmployeeInfoId(employeeInfoId).orElse(null);
		String allotedLeavecount = leaveAlloted == null ? "0"
				: leaveAlloted.getLeavesDetails().get(employeeDTO.getLeaveOfType());
		List<WorkOffDetails> employeeWorkOffDetails = employeeInfo.getEmployeeOfficialInfo().getCompanyWorkWeekRule()
				.getWorkOffDetailsList();
		Double currentLeave;
		Double availableLeaves = Double.parseDouble(allotedLeavecount) - appliedCount.doubleValue();

		AtomicDouble[] leaveCounts = currentLeaveCount(employeeDTO, employeeWorkOffDetails);

		double leaveCount = leaveCounts[0].doubleValue();

		if (employeeDTO.getLeaveType().equalsIgnoreCase(LEAVE_DURATION_FULL)) {
			currentLeave = 1.0 * leaveCount;
		} else if (employeeDTO.getLeaveType().equalsIgnoreCase(LEAVE_DURATION_HALF)) {
			currentLeave = 0.5 * leaveCount;
		} else {
			double leaveMinutes = employeeDTO.getStartTime().until(employeeDTO.getEndTime(), MINUTES);
			currentLeave = Math.round((leaveMinutes / shiftMinutes) * leaveCount * 100) / 100.0;
		}

		if (currentLeave + leaveCounts[1].doubleValue() > availableLeaves) {
			throw new InsufficientLeavesException("Insufficient Leaves for current type");
		}

		BeanUtils.copyProperties(employeeDTO, employeeLeaveApplied);
		employeeLeaveApplied.setLeaveDuration(currentLeave + leaveCounts[1].doubleValue());
		employeeLeaveApplied.setStatus(LEAVE_STATUS_PENDING);
		BeanUtils.copyProperties(leaveAppliedRepository.save(employeeLeaveApplied), employeeDTO);
		return employeeDTO;
	}

	private AtomicDouble[] currentLeaveCount(EmployeeApplyLeaveDTO employeeDTO,
			List<WorkOffDetails> employeeWorkOffDetails) {
		AtomicDouble atomicLeaveCount = new AtomicDouble(0.0);
		AtomicDouble extraLeaveCount = new AtomicDouble(0.0);

		if (!employeeDTO.getStartDate().isAfter(employeeDTO.getEndDate())) {
			employeeDTO.getStartDate().datesUntil(employeeDTO.getEndDate().plusDays(1))
					.forEach(leaveDate -> employeeWorkOffDetails.stream().forEach(e -> {
						if (e.getWeekNumber() == leaveDate.get(ChronoField.ALIGNED_WEEK_OF_MONTH)) {
							if (!(e.getFullDayWorkOff().stream().anyMatch(
									day -> day.equalsIgnoreCase(leaveDate.getDayOfWeek().toString().substring(0, 3))))
									&& !(e.getHalfDayWorkOff().stream().anyMatch(day -> day
											.equalsIgnoreCase(leaveDate.getDayOfWeek().toString().substring(0, 3))))) {
								atomicLeaveCount.getAndAdd(1.0);
							} else if (e.getHalfDayWorkOff().stream().anyMatch(
									day -> day.equalsIgnoreCase(leaveDate.getDayOfWeek().toString().substring(0, 3)))
									&& employeeDTO.getLeaveType().equalsIgnoreCase(LEAVE_DURATION_FULL)) {
								extraLeaveCount.getAndAdd(0.5);
							}
						}
					}));
		}
		return new AtomicDouble[] { atomicLeaveCount, extraLeaveCount };
	}

	@Override
	public Boolean deleteLeave(Long leaveAppliedId, Long employeeInfoId) {
		EmployeeLeaveApplied employeeLeaveApplied = leaveAppliedRepository
				.findByLeaveAppliedIdAndEmployeePersonalInfoEmployeeInfoId(leaveAppliedId, employeeInfoId).orElse(null);
		if (employeeLeaveApplied != null) {
			if (employeeLeaveApplied.getStatus().equalsIgnoreCase(LEAVE_STATUS_PENDING)) {
				leaveAppliedRepository.deleteById(leaveAppliedId);
				return Boolean.TRUE;
			} else {
				throw new LeaveIdNotFoundException("Unable to delete leave request!!!");
			}
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public EmployeeDropdownDTO getReportingManager(Long employeeInfoId) {
		EmployeeReportingInfo employeeReportingInfo = employeeReportingInfoRepository
				.findByEmployeePersonalInfoEmployeeInfoId(employeeInfoId);
		if (employeeReportingInfo == null || employeeReportingInfo.getReportingHR() == null
				|| employeeReportingInfo.getReportingHR().getEmployeeOfficialInfo() == null) {
			return null;
		}
		EmployeePersonalInfo reportingHR = employeeReportingInfo.getReportingHR();
		return new EmployeeDropdownDTO(employeeReportingInfo.getReportId(),
				reportingHR.getEmployeeOfficialInfo().getEmployeeId(), reportingHR.getFirstName());
	}

	@Override
	public List<EmployeeApplyLeaveDTO> getLeavesList(String status, Long employeeInfoId,
			CanlenderRequestDTO calCanlenderRequestDTO, List<LocalDate> startEndDate) {
		if (startEndDate.isEmpty()) {
			startEndDate = companyEventDetailsServiceImpl.getStartEndDate(calCanlenderRequestDTO.getYear(),
					calCanlenderRequestDTO.getFiscalMonth());
		}
		List<EmployeeApplyLeaveDTO> employeeLeaveDTOList = new ArrayList<>();

		List<EmployeeLeaveApplied> employeeLeaveAppliedList;

		switch (status.toUpperCase()) {
		case LEAVE_STATUS_ALL:
			employeeLeaveAppliedList = leaveAppliedRepository
					.findByEmployeePersonalInfoEmployeeInfoIdAndStartDateBetweenOrderByStartDateDesc(employeeInfoId,
							startEndDate.get(0), startEndDate.get(1));
			break;
		case LEAVE_STATUS_APPLIED:
			employeeLeaveAppliedList = leaveAppliedRepository
					.findByEmployeePersonalInfoEmployeeInfoIdAndStatusAndStartDateBetweenOrderByStartDateDesc(
							employeeInfoId, LEAVE_STATUS_PENDING, startEndDate.get(0), startEndDate.get(1));
			break;
		case LEAVE_STATUS_APPROVED:
			employeeLeaveAppliedList = leaveAppliedRepository
					.findByEmployeePersonalInfoEmployeeInfoIdAndStatusAndStartDateBetweenOrderByStartDateDesc(
							employeeInfoId, LEAVE_STATUS_APPROVED, startEndDate.get(0), startEndDate.get(1));
			break;
		case LEAVE_STATUS_REJECTED:
			employeeLeaveAppliedList = leaveAppliedRepository
					.findByEmployeePersonalInfoEmployeeInfoIdAndStatusAndStartDateBetweenOrderByStartDateDesc(
							employeeInfoId, LEAVE_STATUS_REJECTED, startEndDate.get(0), startEndDate.get(1));
			break;
		default:
			throw new DataNotFoundException(LEAVE_STATUS_INVALID);
		}

		employeeLeaveAppliedList.stream().forEach(leaveApplied -> {

			EmployeeApplyLeaveDTO applyLeaveDto = new EmployeeApplyLeaveDTO();
			BeanUtils.copyProperties(leaveApplied, applyLeaveDto);
			if (leaveApplied.getEmployeeReportingInfo() != null
					&& leaveApplied.getEmployeeReportingInfo().getReportingManager() != null) {
				EmployeePersonalInfo reportingManager = leaveApplied.getEmployeeReportingInfo().getReportingManager();
				applyLeaveDto.setReportingManager(reportingManager.getFirstName());
			}
			employeeLeaveDTOList.add(applyLeaveDto);

		});

		return employeeLeaveDTOList;
	}

	@Override
	public List<EmployeeAllotedLeavesDTO> getAllotedLeavesList(Long employeeInfoId) {
		List<EmployeeAllotedLeavesDTO> employeeLeaveDTOList = new ArrayList<>();
		EmployeeLeaveAllocated employeeLeaveAllocated = allocatedRepository
				.findByEmployeePersonalInfoEmployeeInfoId(employeeInfoId).orElse(null);
		if (employeeLeaveAllocated == null) {
			return employeeLeaveDTOList;
		}
		EmployeePersonalInfo employeePersonalInfo = employeeLeaveAllocated.getEmployeePersonalInfo();
		List<EmployeeLeaveApplied> employeeLeaveAppliedList = employeeLeaveAllocated.getEmployeePersonalInfo()
				.getEmployeeLeaveAppliedList().stream()
				.filter(e -> e.getStartDate().getYear() == LocalDate.now().getYear()).collect(Collectors.toList());

		if (employeePersonalInfo.getEmployeeOfficialInfo() == null
				|| employeePersonalInfo.getEmployeeOfficialInfo().getCompanyShiftInfo() == null)
			throw new DataNotFoundException("No shift information found!!!");
		Map<String, String> leavesDetails = employeeLeaveAllocated.getLeavesDetails();
		for (Map.Entry<String, String> entry : leavesDetails.entrySet()) {
			EmployeeAllotedLeavesDTO allotedLeavesDTO = new EmployeeAllotedLeavesDTO();
			allotedLeavesDTO.setLeaveType(entry.getKey());
			allotedLeavesDTO.setAllottedLeave(Double.parseDouble(entry.getValue()));
			Double appliedCount = employeeLeaveAppliedList.stream()
					.filter(leave -> !leave.getStatus().equalsIgnoreCase("Rejected"))
					.filter(e -> e.getLeaveOfType().equals(entry.getKey())).map(EmployeeLeaveApplied::getLeaveDuration)
					.reduce(0.0, (a, b) -> (a + b));
			allotedLeavesDTO.setRemainingLeave((Double.parseDouble(entry.getValue()) - appliedCount));
			employeeLeaveDTOList.add(allotedLeavesDTO);
		}
		return employeeLeaveDTOList;
	}

	@Override
	public List<EmployeeCalenderLeaveInfoDTO> getAllCalenderDetails(Long employeeInfoId, Long companyId,
			CanlenderRequestDTO calCanlenderRequestDTO) {

		EmployeePersonalInfo employeeInfo = personalInfoRepository.findById(employeeInfoId)
				.orElseThrow(() -> new DataNotFoundException("Employee Not Found"));

		List<LocalDate> startEndDate = companyEventDetailsServiceImpl.getStartEndDate(calCanlenderRequestDTO.getYear(),
				calCanlenderRequestDTO.getFiscalMonth());

		List<EmployeeApplyLeaveDTO> employeeLeaveDTO = getLeavesList(LEAVE_STATUS_ALL, employeeInfoId,
				calCanlenderRequestDTO, startEndDate);

		List<EmployeeCalenderLeaveInfoDTO> leaveInfoList = new ArrayList<>();

		List<WorkOffDetails> employeeWorkOffDetails;

		if (employeeInfo.getEmployeeOfficialInfo() != null
				&& employeeInfo.getEmployeeOfficialInfo().getCompanyWorkWeekRule() != null
				&& employeeInfo.getEmployeeOfficialInfo().getCompanyWorkWeekRule().getWorkOffDetailsList() != null) {

			employeeWorkOffDetails = employeeInfo.getEmployeeOfficialInfo().getCompanyWorkWeekRule()
					.getWorkOffDetailsList();
		} else {
			throw new DataNotFoundException("Work week deatils not found");
		}

		employeeLeaveDTO.stream().forEach(leave -> {
			if (leave.getStartDate().equals(leave.getEndDate())) {
				leaveInfoList.add(EmployeeCalenderLeaveInfoDTO.builder().startDate(leave.getStartDate())
						.leaveType(leave.getLeaveType()).leaveOfType(leave.getLeaveOfType()).reason(leave.getReason())
						.status(leave.getStatus()).build());
			} else if (leave.getStartDate().isBefore(leave.getEndDate())) {
				leave.getStartDate().datesUntil(leave.getEndDate().plusDays(1)).forEach(leaveDate ->

				employeeWorkOffDetails.stream().forEach(week -> {
					if (week.getWeekNumber() == leaveDate.get(ChronoField.ALIGNED_WEEK_OF_MONTH)
							&& !(week.getFullDayWorkOff().stream().anyMatch(day -> day
									.equalsIgnoreCase(leaveDate.getDayOfWeek().toString().substring(0, 3))))) {
						leaveInfoList.add(EmployeeCalenderLeaveInfoDTO.builder().startDate(leaveDate)
								.leaveType(leave.getLeaveType()).leaveOfType(leave.getLeaveOfType())
								.reason(leave.getReason()).status(leave.getStatus()).build());
					}
				}));
			}
		});

		Optional<List<CompanyHolidayDetails>> holidayList = companyHolidayDetailsRepository
				.findByHolidayDateBetweenAndCompanyInfoCompanyId(startEndDate.get(0), startEndDate.get(1), companyId);

		if (holidayList.isPresent()) {
			holidayList.get().stream().forEach(holiday -> {
				EmployeeCalenderLeaveInfoDTO applyLeaveDTO = EmployeeCalenderLeaveInfoDTO.builder()
						.startDate(holiday.getHolidayDate()).reason(holiday.getHolidayName())
						.status(LEAVE_STATUS_HOLIDAY).build();
				leaveInfoList.add(applyLeaveDTO);
			});
		}

		return leaveInfoList;
	}

	@Override
	public EmployeeLeaveDTO getLeaveById(Long leaveAppliedId, Long employeeInfoId) {
		EmployeeLeaveApplied leaveApplied = leaveAppliedRepository
				.findByLeaveAppliedIdAndEmployeePersonalInfoEmployeeInfoId(leaveAppliedId, employeeInfoId).orElse(null);
		if (leaveApplied == null) {
			return null;
		}
		EmployeeLeaveDTO employeeLeaveDTO = new EmployeeLeaveDTO();
		BeanUtils.copyProperties(leaveApplied, employeeLeaveDTO);
		if (leaveApplied.getEmployeeReportingInfo() != null) {
			employeeLeaveDTO.setReportingManagerId(
					leaveApplied.getEmployeeReportingInfo().getReportingManager().getEmployeeInfoId());
			employeeLeaveDTO.setReportingManagerName(
					leaveApplied.getEmployeeReportingInfo().getReportingManager().getFirstName());
		}
		return employeeLeaveDTO;
	}

	@Override
	public List<String> getLeaveTypesDropdown(Long employeeInfoId) {
		Optional<EmployeeLeaveAllocated> allocatedLeaves = allocatedRepository
				.findByEmployeePersonalInfoEmployeeInfoId(employeeInfoId);
		if (allocatedLeaves.isEmpty()) {
			return new ArrayList<>();
		}
		return allocatedLeaves.get().getLeavesDetails().keySet().stream().collect(Collectors.toList());
	}

}