package com.te.zealhr.service.employee;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.zealhr.common.admin.mongo.EmployeeTimesheetDetailsConstants;
import com.te.zealhr.dto.employee.EmployeeRegularizationDetailsDTO;
import com.te.zealhr.dto.employee.PunchInPunchOutDTO;
import com.te.zealhr.dto.employee.RegularizationCalenderDTO;
import com.te.zealhr.entity.admin.CompanyHolidayDetails;
import com.te.zealhr.entity.admin.CompanyShiftInfo;
import com.te.zealhr.entity.employee.EmployeeLeaveApplied;
import com.te.zealhr.entity.employee.EmployeeOfficialInfo;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.employee.EmployeeRegularizationDetails;
import com.te.zealhr.entity.employee.mongo.AttendanceDetails;
import com.te.zealhr.entity.employee.mongo.EmployeeAttendanceDetails;
import com.te.zealhr.exception.DataAlreadyExistsException;
import com.te.zealhr.exception.DataNotFoundException;
import com.te.zealhr.exception.InvalidInputException;
import com.te.zealhr.repository.admin.CompanyHolidayDetailsRepository;
import com.te.zealhr.repository.employee.EmployeeLeaveAppliedRepository;
import com.te.zealhr.repository.employee.EmployeePersonalInfoRepository;
import com.te.zealhr.repository.employee.EmployeeRegularizationDetailsRepository;
import com.te.zealhr.repository.employee.mongo.EmployeeAttendanceDetailsRepository;

@Service
public class EmployeeRegularizationServiceImpl implements EmployeeRegularizationService {

	@Autowired
	private EmployeeRegularizationDetailsRepository employeeRegularizationDetailsRepository;

	@Autowired
	private EmployeePersonalInfoRepository employeePersonalInfoRepository;

	@Autowired
	private EmployeeAttendanceDetailsRepository employeeAttendanceDetailsRepository;

	@Autowired
	private CompanyHolidayDetailsRepository companyHolidayDetailsRepository;

	@Autowired
	private EmployeeLeaveAppliedRepository employeeLeaveAppliedRepository;

	/**
	 * This method is used to apply the regularization
	 */
	@Override
	public String applyRegularization(EmployeeRegularizationDetailsDTO regularizationDetailsDTO, Long companyId,
			Long employeeInfoId) {
		// Checking whether or not employee data is present
		EmployeePersonalInfo employeePersonalInfo = employeePersonalInfoRepository
				.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId, companyId);
		if (employeePersonalInfo == null)
			throw new DataNotFoundException("Employee Not Found");

		EmployeeRegularizationDetails regularizationDetails = null;

		if (regularizationDetailsDTO.getRegularizationId() != null) {
			Optional<EmployeeRegularizationDetails> existedRegularizationDetails = employeeRegularizationDetailsRepository
					.findById(regularizationDetailsDTO.getRegularizationId());
			if (existedRegularizationDetails.isPresent()) {
				// Updating existed regularization details
				regularizationDetails = existedRegularizationDetails.get();
				if (regularizationDetails.getStatus() != null && regularizationDetails.getStatus()
						.equalsIgnoreCase(EmployeeTimesheetDetailsConstants.PENDING)) {

					employeeRegularizationDetailsRepository
							.save(setPunchInAndPunchOut(regularizationDetailsDTO, regularizationDetails));
				} else
					throw new InvalidInputException("Regularization Cannot Be Updated!!");
			} else
				throw new DataNotFoundException("Regularization Not Found");
		} else {

			// Checking for multiple regularization for same day
			List<EmployeeRegularizationDetails> filteredRegularizarionList = employeeRegularizationDetailsRepository
					.findByEmployeePersonalInfoEmployeeInfoIdAndPunchInIsNotNullAndPunchOutIsNotNull(employeeInfoId)
					.stream()
					.filter(regularization -> regularization.getPunchIn().toLocalDate()
							.equals(regularizationDetailsDTO.getDate())
							&& regularization.getPunchOut().toLocalDate()
									.equals(validatePunchInAndPunchOut(regularizationDetailsDTO, regularization).get(1)
											.toLocalDate()))
					.collect(Collectors.toList());
			if (!filteredRegularizarionList.isEmpty())
				throw new DataAlreadyExistsException("Multiple Regularizations Cannot Be Applied for the Same Day");

			// Adding new regularization details
			regularizationDetails = new EmployeeRegularizationDetails();
//			BeanUtils.copyProperties(regularizationDetailsDTO, regularizationDetails);
			regularizationDetails.setReason(regularizationDetailsDTO.getReason());
			regularizationDetails.setEmployeePersonalInfo(employeePersonalInfo);
			regularizationDetails.setStatus(EmployeeTimesheetDetailsConstants.PENDING);
			employeeRegularizationDetailsRepository
					.save(setPunchInAndPunchOut(regularizationDetailsDTO, regularizationDetails));
			BeanUtils.copyProperties(regularizationDetails, regularizationDetailsDTO);
		}

		return "Regularization Applied Successfully";

	}

	/**
	 * This is the helper method to set the punchIn and punchOut
	 * 
	 * @param regularizationDetailsDTO
	 * @param regularizationDetails
	 * @return EmployeeRegularizationDetails
	 */
	private EmployeeRegularizationDetails setPunchInAndPunchOut(
			EmployeeRegularizationDetailsDTO regularizationDetailsDTO,
			EmployeeRegularizationDetails regularizationDetails) {
		List<LocalDateTime> dates = validatePunchInAndPunchOut(regularizationDetailsDTO, regularizationDetails);

		regularizationDetails.setPunchIn(dates.get(0));
		regularizationDetails.setPunchOut(dates.get(1));
		regularizationDetails.setReason(regularizationDetailsDTO.getReason());
		return regularizationDetails;
	}

	/**
	 * This is the helper method to validate punchIn and punchOut
	 * 
	 * @param regularizationDetailsDTO
	 * @param regularizationDetails
	 * @return List<LocalDateTime>
	 */
	private List<LocalDateTime> validatePunchInAndPunchOut(EmployeeRegularizationDetailsDTO regularizationDetailsDTO,
			EmployeeRegularizationDetails regularizationDetails) {

		return Arrays.asList(regularizationDetailsDTO.getDate().atTime(regularizationDetailsDTO.getPunchIn().getHour(),
				regularizationDetailsDTO.getPunchIn().getMinute(), regularizationDetailsDTO.getPunchIn().getSecond()),
				regularizationDetailsDTO.getPunchIn().isAfter(regularizationDetailsDTO.getPunchOut())
						? regularizationDetailsDTO.getDate().plusDays(1l).atTime(
								regularizationDetailsDTO.getPunchOut().getHour(),
								regularizationDetailsDTO.getPunchOut().getMinute(),
								regularizationDetailsDTO.getPunchOut().getSecond())
						: regularizationDetailsDTO.getDate().atTime(regularizationDetailsDTO.getPunchOut().getHour(),
								regularizationDetailsDTO.getPunchOut().getMinute(),
								regularizationDetailsDTO.getPunchOut().getSecond()));

	}

	/**
	 * This method is used to fetch all regularizations applied by an employee
	 */
	@Override
	public RegularizationCalenderDTO getAllRegularizations(Long companyId, Long employeeInfoId, Integer year,
			Integer month) {
		List<EmployeeRegularizationDetailsDTO> regularizationList = employeeRegularizationDetailsRepository
				.findByEmployeePersonalInfoEmployeeInfoId(employeeInfoId).stream().map(regularization -> {
					EmployeeRegularizationDetailsDTO regularizationDetailsDTO = new EmployeeRegularizationDetailsDTO();
					BeanUtils.copyProperties(regularization, regularizationDetailsDTO);
					regularizationDetailsDTO.setDate(regularization.getPunchIn().toLocalDate());
					regularizationDetailsDTO.setPunchIn(regularization.getPunchIn().toLocalTime());
					regularizationDetailsDTO.setPunchOut(regularization.getPunchOut().toLocalTime());
					return regularizationDetailsDTO;
				}).collect(Collectors.toList());
		return RegularizationCalenderDTO.builder()
				.attendanceDetails(
						(year == null || month == null) ? null : getAttendanceDetails(year, month, employeeInfoId))
				.regularizationList(regularizationList).build();
	}

	private Map<LocalDate, PunchInPunchOutDTO> getAttendanceDetails(int year, int month, Long employeeInfoId) {
		Map<LocalDate, PunchInPunchOutDTO> attendanceDetailsMap = new LinkedHashMap<>();
		EmployeePersonalInfo employeePersonalInfo = employeePersonalInfoRepository.findById(employeeInfoId)
				.orElseThrow(() -> new DataNotFoundException("Employee Not Found"));
		LocalDate startDate = LocalDate.of(year, month, 1);
		LocalDate endDate = LocalDate.of(year, month, startDate.getMonth().length(startDate.isLeapYear()));
		List<LocalDate> holidayList = companyHolidayDetailsRepository
				.findByHolidayDateBetweenAndCompanyInfoCompanyId(startDate, endDate,
						employeePersonalInfo.getCompanyInfo().getCompanyId())
				.orElse(new ArrayList<>()).stream().map(CompanyHolidayDetails::getHolidayDate)
				.collect(Collectors.toList());
		List<LocalDate> leaveList = getLeaveDetails(employeeInfoId, startDate, endDate);
		getAllDatesOfMonth(year, month, attendanceDetailsMap, leaveList, holidayList);
		EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
		CompanyShiftInfo companyShiftInfo = employeeOfficialInfo == null ? null
				: employeeOfficialInfo.getCompanyShiftInfo();
		Optional<EmployeeAttendanceDetails> attendanceOptional = employeeAttendanceDetailsRepository
				.findByEmployeeInfoIdAndMonthNoAndYear(employeeInfoId, month, year);
		if (attendanceOptional.isPresent() && companyShiftInfo != null) {
			List<AttendanceDetails> attendanceDetailsList = attendanceOptional.get().getAttendanceDetails();
			if (attendanceDetailsList != null) {
				for (AttendanceDetails attendanceDetails : attendanceDetailsList) {
					getPunchInPunchOutDTO(attendanceDetails, attendanceDetailsMap, companyShiftInfo);
				}
			}

		}
		return attendanceDetailsMap;
	}

	private List<LocalDate> getLeaveDetails(Long employeeInfoId, LocalDate startDate, LocalDate endDate) {
		List<EmployeeLeaveApplied> employeeLeaveAppliedList = employeeLeaveAppliedRepository
				.findByStatusIgnoreCaseAndEmployeePersonalInfoEmployeeInfoIdAndStartDateBetween("Approved",
						employeeInfoId, startDate, endDate);
		List<LocalDate> leaveList = new ArrayList<>();
		employeeLeaveAppliedList.stream().forEach(leave -> {
			if (leave.getStartDate().equals(leave.getEndDate())) {
				leaveList.add(leave.getStartDate());
			} else if (leave.getStartDate().isBefore(leave.getEndDate())) {
				leave.getStartDate().datesUntil(leave.getEndDate().plusDays(1)).forEach(leaveDate -> {
					leaveList.add(leaveDate);
				});
			}
		});
		return leaveList;
	}

	private void getPunchInPunchOutDTO(AttendanceDetails attendanceDetails,
			Map<LocalDate, PunchInPunchOutDTO> attendanceDetailsMap, CompanyShiftInfo companyShiftInfo) {
		Boolean isPresentInOffice = Boolean.TRUE;
		if (attendanceDetails.getIsInsideLocation().equals(Boolean.FALSE)) {
			isPresentInOffice = (attendanceDetails.getStatus() != null
					&& attendanceDetails.getStatus().containsKey("Approved")) ? Boolean.TRUE : Boolean.FALSE;
		}

		if (attendanceDetails.getPunchIn() != null) {
			attendanceDetailsMap.put(attendanceDetails.getPunchIn().toLocalDate(),
					PunchInPunchOutDTO.builder().punchIn(attendanceDetails.getPunchIn().toLocalTime())
							.punchOut(attendanceDetails.getPunchOut() != null
									? attendanceDetails.getPunchOut().toLocalTime()
									: null)
							.isPresentInOffice(isPresentInOffice).build());
		} else if (attendanceDetails.getPunchOut() != null) {
			attendanceDetailsMap.put(
					companyShiftInfo.getLoginTime().isAfter(companyShiftInfo.getLogoutTime())
							? attendanceDetails.getPunchOut().toLocalDate().minusDays(1)
							: attendanceDetails.getPunchOut().toLocalDate(),
					PunchInPunchOutDTO.builder().punchOut(attendanceDetails.getPunchOut().toLocalTime())
							.punchIn(attendanceDetails.getPunchIn() != null
									? attendanceDetails.getPunchIn().toLocalTime()
									: null)
							.isPresentInOffice(isPresentInOffice).build());
		}
	}

	private void getAllDatesOfMonth(int year, int month, Map<LocalDate, PunchInPunchOutDTO> attendanceDetailsMap,
			List<LocalDate> leaveList, List<LocalDate> holidayList) {
		LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
		LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());
		firstDayOfMonth.datesUntil(lastDayOfMonth).forEach(date -> {
			PunchInPunchOutDTO punchInPunchOutDTO = null;
			if (leaveList.contains(date)) {
				punchInPunchOutDTO = PunchInPunchOutDTO.builder().isOnLeave(Boolean.TRUE).build();
			}
			if (holidayList.contains(date)) {
				punchInPunchOutDTO = PunchInPunchOutDTO.builder().isHoliday(Boolean.TRUE).build();
			}
			attendanceDetailsMap.put(date, punchInPunchOutDTO);
		});
	}

	@Override
	/**
	 * This method is used to delete the regularization it it not approved yet
	 */
	public String deleteRegularization(Long regularizationId) {
		Optional<EmployeeRegularizationDetails> existedRegularizationDetails = employeeRegularizationDetailsRepository
				.findById(regularizationId);
		if (existedRegularizationDetails.isPresent()) {
			EmployeeRegularizationDetails employeeRegularizationDetails = existedRegularizationDetails.get();
			if (employeeRegularizationDetails.getStatus() != null && employeeRegularizationDetails.getStatus()
					.equalsIgnoreCase(EmployeeTimesheetDetailsConstants.PENDING)) {
				employeeRegularizationDetailsRepository.delete(employeeRegularizationDetails);
				return "Regularization Deleted Successfully";
			} else {
				throw new InvalidInputException("Regularization Cannot Be Deleted!!");
			}
		} else {
			throw new DataNotFoundException("Regularization Not Found");
		}
	}

}
