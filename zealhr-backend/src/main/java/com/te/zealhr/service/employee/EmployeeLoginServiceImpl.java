package com.te.zealhr.service.employee;

import static com.te.zealhr.common.employee.EmployeeLeaveConstants.LEAVE_STATUS_APPROVED;
import static com.te.zealhr.common.employee.EmployeeLeaveConstants.LEAVE_STATUS_REJECTED;
import static com.te.zealhr.common.employee.EmployeeLoginConstants.EMPLYOEE_DOES_NOT_EXIST;
import static com.te.zealhr.common.employee.EmployeeLoginConstants.INVALID_OTP;
import static com.te.zealhr.common.employee.EmployeeLoginConstants.SUCCESSFULLY_LOGGED_IN;
import static com.te.zealhr.common.employee.EmployeeLoginConstants.VALID_OTP;
import static com.te.zealhr.common.employee.EmployeeRegistrationConstants.SESSION_TIME_EXPIRED;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.te.zealhr.audit.BaseConfigController;
import com.te.zealhr.beancopy.BeanCopy;
import com.te.zealhr.dto.employee.EmployeeCapabilityDTO;
import com.te.zealhr.dto.employee.EmployeeIdDto;
import com.te.zealhr.dto.employee.EmployeeLoginDto;
import com.te.zealhr.dto.employee.EmployeeLoginResponseDto;
import com.te.zealhr.dto.employee.LeaveDetailsDTO;
import com.te.zealhr.dto.employee.MailDto;
import com.te.zealhr.dto.employee.VerifyOTPDto;
import com.te.zealhr.entity.admin.CompanyAddressInfo;
import com.te.zealhr.entity.admin.CompanyBranchInfo;
import com.te.zealhr.entity.admin.CompanyInfo;
import com.te.zealhr.entity.admin.CompanyRuleInfo;
import com.te.zealhr.entity.admin.CompanyShiftInfo;
import com.te.zealhr.entity.employee.EmployeeLeaveAllocated;
import com.te.zealhr.entity.employee.EmployeeLeaveApplied;
import com.te.zealhr.entity.employee.EmployeeLoginInfo;
import com.te.zealhr.entity.employee.EmployeeOfficialInfo;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.employee.mongo.AttendanceDetails;
import com.te.zealhr.entity.employee.mongo.EmployeeAttendanceDetails;
import com.te.zealhr.exception.InvalidInputException;
import com.te.zealhr.exception.auth.CustomAccessDeniedException;
import com.te.zealhr.exception.employee.DataNotFoundException;
import com.te.zealhr.exception.employee.EmployeeLoginException;
import com.te.zealhr.exception.employee.EmployeeNotRegisteredException;
import com.te.zealhr.repository.employee.EmployeeLeaveAllocatedRepository;
import com.te.zealhr.repository.employee.EmployeeLeaveAppliedRepository;
import com.te.zealhr.repository.employee.EmployeeLoginInfoRepository;
import com.te.zealhr.repository.employee.EmployeePersonalInfoRepository;
import com.te.zealhr.repository.employee.EmployeeReportingInfoRepository;
import com.te.zealhr.repository.employee.mongo.EmployeeAttendanceDetailsRepository;
import com.te.zealhr.service.mail.employee.EmailService;
import com.te.zealhr.util.CacheStore;
import com.te.zealhr.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sahid
 * @author Trupthi
 *
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeLoginServiceImpl extends BaseConfigController implements EmployeeLoginService {

	private static final String YOUR_ACCOUNT_IS_NOT_ACTIVATE_PLEASE_CONTACT_ADMIN_OR_HR = "Your Account Is Inactive Please Contact Admin Or HR!!!";

	private final EmployeeLoginInfoRepository employeeLoginRepository;

	private final EmployeePersonalInfoRepository employeePersonalInfoRepository;

	private final EmployeeAttendanceDetailsRepository employeeAttendanceDetailsRepository;

	private final CacheStore<EmployeeLoginInfo> cacheStoreEmployeeLogin;

	private final CacheStore<Long> cacheStoreOTP;

	private final CacheStore<Boolean> cacheStoreValidOTP;

	private final EmailService emailService;

	private List<EmployeeCapabilityDTO> roles;

	private final JwtUtil jwtService;

	private final CustomAccessDeniedException accessDenied;

	private final EmployeeLeaveAllocatedRepository employeeLeaveAllocatedRepository;

	private final EmployeeReportingInfoRepository employeeReportingInfoRepository;

	private final EmployeeLeaveAppliedRepository employeeLeaveAppliedRepository;

	private static final String TOTAL_LEAVE = "Total Leave";

	private static final String USED_LEAVE = "Used Leave";

	private static final String APPLIED_LEAVES = "Applied Leaves";

	private static final String REJECTED_LEAVES = "Rejected Leaves";

	private static final String JSON_FILE_PATH = "/data/reporting-manager-roles.json";

	String msg;

	private EmployeeLoginResponseDto login(String emailId, String companyCode) {
		msg = null;
		roles = new ArrayList<>();
		Boolean isReportingManager;
		EmployeeLoginInfo employeeLoginInfo = employeeLoginRepository
				.findByEmailIdAndEmployeePersonalInfoCompanyInfoCompanyCode(emailId, companyCode);

		if (employeeLoginInfo == null) {
			throw new EmployeeNotRegisteredException(EMPLYOEE_DOES_NOT_EXIST);
		}

		if (Boolean.FALSE.equals(employeeLoginInfo.getEmployeePersonalInfo().getIsActive())) {
			throw new DataNotFoundException(YOUR_ACCOUNT_IS_NOT_ACTIVATE_PLEASE_CONTACT_ADMIN_OR_HR);
		}
		EmployeePersonalInfo info = employeeLoginInfo.getEmployeePersonalInfo();

		CompanyInfo companyInfo = info.getCompanyInfo();

		if (companyInfo.getIsActive().equals(Boolean.FALSE)) {
			throw new DataNotFoundException("Company is In-Active");
		}

		CompanyRuleInfo companyRuleInfo = companyInfo.getCompanyRuleInfo();

		if (employeeLoginInfo.getRoles() != null && (employeeLoginInfo.getRoles().equals("{}")
				|| employeeLoginInfo.getRoles().toString().equalsIgnoreCase("[]"))) {
			msg = "Role Is Not Set To The Employee.";
		} else {
			roles = BeanCopy.objectProperties(employeeLoginInfo.getRoles(),
					new TypeReference<List<EmployeeCapabilityDTO>>() {
					});
		}
		if (!employeeReportingInfoRepository
				.findByReportingManagerEmployeeInfoIdAndReportingManagerCompanyInfoCompanyId(info.getEmployeeInfoId(),
						companyInfo.getCompanyId())
				.isEmpty()) {
			isReportingManager = true;
			roles.addAll(getReportingManagerRoles(companyRuleInfo));
		} else {
			isReportingManager = false;
		}
		var jwtToken = jwtService.generateToken(employeeLoginInfo, getTerminalId());

		List<LeaveDetailsDTO> leaveDetailsDTOList = new ArrayList<>();
		Map<String, Double> leaveDetails = getLeaveDetails(info, companyRuleInfo, leaveDetailsDTOList);
		return Optional.ofNullable(employeeLoginInfo).map(employeeLogedin -> {
			EmployeeOfficialInfo employeeOfficialInfo = info.getEmployeeOfficialInfo();
			return EmployeeLoginResponseDto.builder().companyId(info.getCompanyInfo().getCompanyId())
					.employeeId(employeeOfficialInfo.getEmployeeId()).employeeInfoId(info.getEmployeeInfoId())
					.name(info.getFirstName()).designation(employeeOfficialInfo.getDesignation())
					.logo(info.getCompanyInfo().getCompanyLogoUrl()).accessToken(jwtToken[0]).refreshToken(jwtToken[1])
					.department(employeeOfficialInfo.getDepartment()).msg(msg != null ? msg : SUCCESSFULLY_LOGGED_IN)
					.roles(roles).availableLeaves(leaveDetails.get(TOTAL_LEAVE) - leaveDetails.get(USED_LEAVE))
					.usedLeaves(leaveDetails.get(USED_LEAVE))
					.isChatEnabled(companyRuleInfo != null && companyRuleInfo.getIsChatBoxEnabled() != null
							? companyRuleInfo.getIsChatBoxEnabled().booleanValue()
							: Boolean.FALSE)
					.isReportingManager(isReportingManager)
					.fiscalMonth(companyRuleInfo != null ? companyRuleInfo.getFiscalYear() : null)
					.appliedLeaves(leaveDetails.get(APPLIED_LEAVES)).allottedLeaves(leaveDetails.get(TOTAL_LEAVE))
					.rejectedLeaves(leaveDetails.get(REJECTED_LEAVES)).pictureURL(info.getPictureURL())
					.leaveDetails(leaveDetailsDTOList)
					.attendanceType(companyRuleInfo != null ? companyRuleInfo.getAttendanceType() : null)
					.isRegularizationEnabled(
							companyRuleInfo != null && companyRuleInfo.getIsRegularizationEnabled() != null
									? companyRuleInfo.getIsRegularizationEnabled()
									: Boolean.FALSE)
					.isIdAutoGenerated(companyRuleInfo != null && companyRuleInfo.getIsIdAutoGenerated() != null
							? companyRuleInfo.getIsIdAutoGenerated()
							: Boolean.FALSE)
					.isDateRequiredInId(companyRuleInfo != null && companyRuleInfo.getIdGenerationRule() != null
							&& companyRuleInfo.getIdGenerationRule().get("dateOfJoining") != null
							&& !companyRuleInfo.getIdGenerationRule().get("dateOfJoining").equals("null") ? Boolean.TRUE
									: Boolean.FALSE)
					.build();
		}).orElseThrow(() -> new EmployeeLoginException(EMPLYOEE_DOES_NOT_EXIST));

	}

	private Map<String, Double> getLeaveDetails(EmployeePersonalInfo info, CompanyRuleInfo companyRuleInfo,
			List<LeaveDetailsDTO> leaveDetailsDTOList) {
		Double totalLeave = 0.0;
		Double usedLeave = 0.0;
		Double rejectedLeave = 0.0;
		Double appliedLeave = 0.0;
		Map<String, Double> counts = new LinkedHashMap<>();

		EmployeeLeaveAllocated employeeLeaveAllocated = employeeLeaveAllocatedRepository
				.findByEmployeePersonalInfoEmployeeInfoId(info.getEmployeeInfoId()).orElse(null);
		if (employeeLeaveAllocated != null && companyRuleInfo != null) {
			List<LocalDate> startEndDate = getStartEndDate(LocalDate.now(), companyRuleInfo.getFiscalYear());
			List<EmployeeLeaveApplied> employeeLeaveAppliedList = employeeLeaveAppliedRepository
					.findByEmployeePersonalInfoEmployeeInfoIdAndStartDateBetween(info.getEmployeeInfoId(),
							startEndDate.get(0), startEndDate.get(1));
			usedLeave = usedLeave + employeeLeaveAppliedList.stream()
					.filter(e -> e.getStatus() != null && LEAVE_STATUS_APPROVED.equalsIgnoreCase(e.getStatus()))
					.map(EmployeeLeaveApplied::getLeaveDuration).reduce(0.0, (a, b) -> (a + b));
			rejectedLeave = rejectedLeave + employeeLeaveAppliedList.stream()
					.filter(e -> e.getStatus() != null && LEAVE_STATUS_REJECTED.equalsIgnoreCase(e.getStatus()))
					.map(EmployeeLeaveApplied::getLeaveDuration).reduce(0.0, (a, b) -> (a + b));
			appliedLeave = appliedLeave + employeeLeaveAppliedList.stream().map(EmployeeLeaveApplied::getLeaveDuration)
					.reduce(0.0, (a, b) -> (a + b));
			if (info.getEmployeeOfficialInfo() != null
					&& info.getEmployeeOfficialInfo().getCompanyShiftInfo() != null) {

				Map<String, String> leavesDetails = employeeLeaveAllocated.getLeavesDetails();
				for (Map.Entry<String, String> entry : leavesDetails.entrySet()) {
					leaveDetailsDTOList.add(new LeaveDetailsDTO(entry.getKey(), Double.parseDouble(entry.getValue()),
							Double.parseDouble(entry.getValue()) - employeeLeaveAppliedList.stream()
									.filter(e -> e.getLeaveOfType() != null
											&& e.getLeaveOfType().equalsIgnoreCase(entry.getKey()))
									.map(EmployeeLeaveApplied::getLeaveDuration).reduce(0.0, (a, b) -> (a + b))));
					totalLeave = totalLeave + Double.parseDouble(entry.getValue());
				}
			}
		}
		counts.put(TOTAL_LEAVE, totalLeave);
		counts.put(USED_LEAVE, usedLeave);
		counts.put(REJECTED_LEAVES, rejectedLeave);
		counts.put(APPLIED_LEAVES, appliedLeave);
		return counts;
	}

	public List<LocalDate> getStartEndDate(LocalDate now, String fiscalMonth) {
		Month month = Month.valueOf(fiscalMonth.toUpperCase(Locale.ENGLISH));
		int startMonth = month.getValue();
		int endMonth;
		int startYear;
		int endYear;
		if (startMonth == 1) {
			startYear = now.getYear();
			endYear = now.getYear();
			endMonth = 12;
		} else {
			if (now.getMonthValue() >= startMonth) {
				startYear = now.getYear();
				endYear = now.getYear() + 1;
			} else {
				startYear = now.getYear() - 1;
				endYear = now.getYear();
			}
			endMonth = startMonth - 1;
		}
		List<LocalDate> dates = new ArrayList<>();
		dates.add(LocalDate.of(startYear, startMonth, 1));
		LocalDate of = LocalDate.of(endYear, endMonth, 1);
		dates.add(of.withDayOfMonth(of.getMonth().length(of.isLeapYear())));
		return dates;
	}

	private List<EmployeeCapabilityDTO> getReportingManagerRoles(CompanyRuleInfo companyRuleInfo) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			InputStream transactionReportStream = getClass().getResourceAsStream(JSON_FILE_PATH);
			List<EmployeeCapabilityDTO> reportingManagerRoles = BeanCopy.objectProperties(
					mapper.readValue(IOUtils.toString(transactionReportStream, StandardCharsets.UTF_8), Object.class),
					new TypeReference<List<EmployeeCapabilityDTO>>() {
					});
			return reportingManagerRoles.stream().map(role -> {
				if (role.getCapabilityType().equalsIgnoreCase("Reporting Manager Approval")
						&& role.getChildCapabilityNameList() != null) {
					role.setChildCapabilityNameList(role.getChildCapabilityNameList().stream().map(childRole -> {
						if (childRole.getCapabilityType().equalsIgnoreCase("Regularization Request")) {
							childRole.setIsEnable(
									companyRuleInfo != null && companyRuleInfo.getIsRegularizationEnabled() != null
											? companyRuleInfo.getIsRegularizationEnabled()
											: Boolean.FALSE);
						}
						return childRole;
					}).collect(Collectors.toList()));
				}
				return role;
			}).collect(Collectors.toList());
		} catch (IOException e) {
			log.error("Reporting Manager Roles Not Fetched");
			return new ArrayList<>();
		}
	}

	@Override
	public String sendOTP(EmployeeIdDto employeeIdDto) {

		EmployeeLoginInfo employeeLoginInfo = employeeLoginRepository
				.findByEmailIdAndEmployeePersonalInfoCompanyInfoCompanyCode(employeeIdDto.getEmailId(),
						employeeIdDto.getCompanyCode());

		if (employeeLoginInfo == null) {
			throw new EmployeeNotRegisteredException(EMPLYOEE_DOES_NOT_EXIST);
		}

		if (Boolean.FALSE.equals(employeeLoginInfo.getEmployeePersonalInfo().getIsActive())) {
			throw new DataNotFoundException(YOUR_ACCOUNT_IS_NOT_ACTIVATE_PLEASE_CONTACT_ADMIN_OR_HR);
		}

		if (cacheStoreEmployeeLogin.get(employeeIdDto.getEmailId()) != null)
			cacheStoreEmployeeLogin.invalidate(employeeIdDto.getEmailId());
		cacheStoreEmployeeLogin.add(employeeIdDto.getEmailId(), employeeLoginInfo);

		return sendOTP(employeeIdDto.getEmailId(), employeeLoginInfo.getEmployeePersonalInfo().getFirstName());
	}

	@Override
	public String resendOTP(EmployeeIdDto employeeIdDto) {
		EmployeeLoginInfo employeeLoginInfo = Optional
				.ofNullable(cacheStoreEmployeeLogin.get(employeeIdDto.getEmailId()))
				.orElseThrow(() -> new DataNotFoundException(SESSION_TIME_EXPIRED));
		return Objects.nonNull(employeeLoginInfo)
				? sendOTP(employeeIdDto.getEmailId(), employeeLoginInfo.getEmployeePersonalInfo().getFirstName())
				: SESSION_TIME_EXPIRED;
	}

	@Override
	public String validateOTP(VerifyOTPDto verifyOTPDto) {
		EmployeeLoginInfo employeeLoginInfo = cacheStoreEmployeeLogin.get(verifyOTPDto.getEmailId());
		Boolean valiedEmployee = Objects.nonNull(employeeLoginInfo) ? Boolean.TRUE : Boolean.FALSE;
		Boolean valiedOTP = Optional.ofNullable(cacheStoreOTP.get(verifyOTPDto.getEmailId()))
				.orElseThrow(() -> new DataNotFoundException(SESSION_TIME_EXPIRED)).equals(verifyOTPDto.getOtp())
						? valiedEmployee
						: Boolean.FALSE;
		if (cacheStoreValidOTP.get(verifyOTPDto.getEmailId()) != null)
			cacheStoreValidOTP.invalidate(verifyOTPDto.getEmailId());
		cacheStoreValidOTP.add(verifyOTPDto.getEmailId(), valiedOTP);
		return Optional.of(valiedOTP).filter(Boolean::booleanValue).map(y -> VALID_OTP)
				.orElseThrow(() -> new DataNotFoundException(INVALID_OTP));
	}

	@Override
	public EmployeeLoginResponseDto validateLoginOTP(VerifyOTPDto verifyOTPDto, HttpServletRequest request,
			HttpServletResponse response) {
		EmployeeLoginInfo employeeLoginInfo = cacheStoreEmployeeLogin.get(verifyOTPDto.getEmailId());
		Boolean valiedEmployee = Objects.nonNull(employeeLoginInfo) ? Boolean.TRUE : Boolean.FALSE;
		Boolean valiedOTP = Optional.ofNullable(cacheStoreOTP.get(verifyOTPDto.getEmailId()))
				.orElseThrow(() -> new DataNotFoundException(SESSION_TIME_EXPIRED)).equals(verifyOTPDto.getOtp())
						? valiedEmployee
						: Boolean.FALSE;
		if (cacheStoreValidOTP.get(verifyOTPDto.getEmailId()) != null)
			cacheStoreValidOTP.invalidate(verifyOTPDto.getEmailId());
		cacheStoreValidOTP.add(verifyOTPDto.getEmailId(), valiedOTP);
		if (Boolean.TRUE.equals(valiedOTP)) {
			try {
				return login(verifyOTPDto.getEmailId(), verifyOTPDto.getCompanyCode());
			} catch (Exception exception) {
				try {
					response.setStatus(HttpStatus.BAD_REQUEST.value());
					log.error(exception.getMessage().equalsIgnoreCase("Bad credentials")
							? "The Company Code or Email Is Incorrect"
							: exception.getMessage());
					accessDenied.handle(request, response,
							new AccessDeniedException(exception.getMessage().equalsIgnoreCase("Bad credentials")
									? "The Company Code or Email Is Incorrect"
									: exception.getMessage()));
				} catch (Exception exception2) {
					log.error(exception2.getMessage());
					throw new DataNotFoundException(exception2.getMessage(), exception2);
				}
				throw new DataNotFoundException(exception.getMessage(), exception);
			}
		} else {
			throw new InvalidInputException("Invalid OTP");
		}
	}

	String sendOTP(String emailId, String userName) {
		Long otp = ThreadLocalRandom.current().nextLong(1000, 10000);
		Integer emailStatus = emailService.sendMail(new MailDto(emailId, "Your OTP For Login",
				"Dear " + userName + ",\r\n" + "\r\n" + "One Time Password for Login is :" + otp + "\r\n"
						+ "Please use this OTP to Login to the Application." + "\r\n" + "\r\n"
						+ "Do not share OTP with anyone." + "\r\n" + "\r\n" + "Thanks and Regards," + "\r\n"
						+ "Team ZealHR"));

		if (emailStatus >= 400)
			throw new DataNotFoundException("OTP Not Sent to " + emailId);

		if (cacheStoreOTP.get(emailId) != null)
			cacheStoreOTP.invalidate(emailId);
		cacheStoreOTP.add(emailId, otp);

		return "OTP Sent Successfully";
	}

	private Long userId;

	private MailDto setMailDto(String email, String employeeName) {
		return new MailDto(email, "Your Password Was Reset Successfully", "Dear " + employeeName + ",\r\n" + "\r\n"
				+ "We wanted to let you know that your zealhr password was reset." + "\r\n"
				+ " If you run into problems, please contact support team" + ",\r\n" + "\r\n"
				+ "Please do not reply to this email with your password. We will never ask for your password, and we strongly discourage you from sharing it with anyone."
				+ "\r\n" + "\r\n" + "\r\n" + "Thanks and Regards," + "\r\n" + "Team zealhr");
	}

	private void cleanUp(String emailId) {
		try {
			cacheStoreOTP.invalidate(emailId);
			cacheStoreEmployeeLogin.invalidate(emailId);
			cacheStoreValidOTP.invalidate(emailId);
		} catch (Exception e) {
			throw new DataNotFoundException(SESSION_TIME_EXPIRED);
		}
	}

	private Boolean isValidRedius(EmployeeLoginDto employeeLoginDto, List<CompanyBranchInfo> companyBranchInfos,
			HttpServletRequest request) {
		Boolean isValidRadius = Boolean.FALSE;
		for (CompanyBranchInfo companyBranchInfo : companyBranchInfos) {
			CompanyAddressInfo companyAddressInfo = companyBranchInfo.getCompanyAddressInfoList().get(0);
			final double R = 6371.01; // Radius of the earth
			Double lat2 = employeeLoginDto.getLatitude();
			Double lon2 = employeeLoginDto.getLongitude();
			Double lat1 = companyAddressInfo.getLatitude();
			Double lon1 = companyAddressInfo.getLongitude();

			double latDistance = Math.toRadians(lat2 - lat1);
			double lonDistance = Math.toRadians(lon2 - lon1);

			double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
					* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			if (companyAddressInfo.getRadius() >= R * c * 1000) {
				isValidRadius = Boolean.TRUE;
				break;
			}
		}
		return isValidRadius;
//		if (!isValidRadius.booleanValue())
//			throw new DataNotFoundException("Out of range can't be logged in");
	}

	public Long getUserId() {
		return userId;
	}

	@Override
	public String punchedIn(EmployeeLoginDto employeeLoginDto, HttpServletRequest request, String type) {
		Long employeeInfoId = getEmployeeInfoId();
		return employeePersonalInfoRepository.findByEmployeeInfoIdAndIsActiveTrue(getEmployeeInfoId()).map(emp -> {
			if (Boolean.FALSE.equals(validateEmployeeDevice(type, emp, employeeLoginDto.getDeviceId()))) {
				throw new DataNotFoundException("Your Device Id Does Not Match");
			}
			Boolean validRedius = isValidRedius(employeeLoginDto, emp.getCompanyInfo().getCompanyBranchInfoList(),
					request);
			LocalDate now = LocalDate.now();
			int monthValue = now.getMonthValue();
			int year = now.getYear();
			EmployeeOfficialInfo employeeOfficialInfo = emp.getEmployeeOfficialInfo();
			if (employeeOfficialInfo == null) {
				throw new DataNotFoundException("Employee Official Details Not Found");
			}
			CompanyShiftInfo companyShiftInfo = employeeOfficialInfo.getCompanyShiftInfo();
			if (companyShiftInfo == null) {
				throw new DataNotFoundException("Employee Shift Details Not Found");
			}
			EmployeeAttendanceDetails employeeAttendanceDetails = employeeAttendanceDetailsRepository
					.findByEmployeeInfoIdAndMonthNoAndYear(employeeInfoId, monthValue, year).map(empLogin -> {
						List<AttendanceDetails> attendanceDetails = empLogin.getAttendanceDetails();
						AttendanceDetails attendanceDetails2 = attendanceDetails.get(attendanceDetails.size() - 1);
						if (attendanceDetails2.getPunchOut() != null && attendanceDetails2.getPunchOut()
								.getDayOfMonth() == LocalDate.now().getDayOfMonth()) {
							attendanceDetails2.setIsInsideLocation(
									attendanceDetails2.getIsInsideLocation().booleanValue() ? validRedius
											: attendanceDetails2.getIsInsideLocation());
							attendanceDetails2.setPunchIn(LocalDateTime.now());
							return empLogin;
						}

						if (attendanceDetails2.getPunchIn().getDayOfMonth()
								+ (companyShiftInfo.getLogoutTime().isAfter(companyShiftInfo.getLoginTime()) ? 1
										: 0) != LocalDate.now().getDayOfMonth()) {
							empLogin.getAttendanceDetails().add(AttendanceDetails.builder().punchIn(LocalDateTime.now())
									.isInsideLocation(validRedius).detailsId(attendanceDetails.size() + 1).build());
						}
						return empLogin;
					})
					.orElseGet(() -> EmployeeAttendanceDetails
							.builder().monthNo(monthValue).year(year).employeeInfoId(employeeInfoId)
							.companyId(getCompanyId()).attendanceDetails(List.of(AttendanceDetails.builder()
									.punchIn(LocalDateTime.now()).isInsideLocation(validRedius).detailsId(1).build()))
							.build());
			employeeAttendanceDetailsRepository.save(employeeAttendanceDetails);
			return "Attendance Added Successfully";
		}).orElseThrow(() -> new DataNotFoundException("Employee Does Not Exist"));
	}

	private Boolean validateEmployeeDevice(String type, EmployeePersonalInfo employeePersonalInfo, String deviceId) {
		if (type.equalsIgnoreCase("Mobile")) {
			return employeePersonalInfo.getModelNumber() == null ? Boolean.FALSE
					: employeePersonalInfo.getModelNumber().equals(deviceId);
		} else {
			return employeePersonalInfo.getCompanyPcLaptopDetailsList().stream()
					.anyMatch(details -> details.getSerialNumber().equals(deviceId));
		}
	}

	@Override
	public String punchedOut(EmployeeLoginDto employeeLoginDto, HttpServletRequest request, String type) {
		Long employeeInfoId = getEmployeeInfoId();
		return employeePersonalInfoRepository.findByEmployeeInfoIdAndIsActiveTrue(getEmployeeInfoId()).map(emp -> {
			if (Boolean.FALSE.equals(validateEmployeeDevice(type, emp, employeeLoginDto.getDeviceId()))) {
				throw new DataNotFoundException("Your Device Id Does Not Match");
			}
			Boolean validRedius = isValidRedius(employeeLoginDto, emp.getCompanyInfo().getCompanyBranchInfoList(),
					request);
			LocalDate now = LocalDate.now();
			int monthValue = now.getMonthValue();
			int year = now.getYear();
			EmployeeOfficialInfo employeeOfficialInfo = emp.getEmployeeOfficialInfo();
			if (employeeOfficialInfo == null) {
				throw new DataNotFoundException("Employee Official Details Not Found");
			}
			CompanyShiftInfo companyShiftInfo = employeeOfficialInfo.getCompanyShiftInfo();
			if (companyShiftInfo == null) {
				throw new DataNotFoundException("Employee Shift Details Not Found");
			}
			EmployeeAttendanceDetails employeeAttendanceDetails = employeeAttendanceDetailsRepository
					.findByEmployeeInfoIdAndMonthNoAndYear(employeeInfoId, monthValue, year).map(empLogin -> {
						List<AttendanceDetails> attendanceDetails = empLogin.getAttendanceDetails();
						AttendanceDetails attendanceDetails2 = attendanceDetails.get(attendanceDetails.size() - 1);
						if (attendanceDetails2.getPunchOut() != null && attendanceDetails2.getPunchOut().getDayOfMonth()
								+ (companyShiftInfo.getLogoutTime().isAfter(companyShiftInfo.getLoginTime()) ? 1
										: 0) != LocalDate.now().getDayOfMonth()) {
							attendanceDetails.add(AttendanceDetails.builder().punchOut(LocalDateTime.now())
									.isInsideLocation(validRedius).detailsId(attendanceDetails.size() + 1).build());
							return empLogin;
						}
						attendanceDetails2.setIsInsideLocation(
								attendanceDetails2.getIsInsideLocation().booleanValue() ? validRedius
										: attendanceDetails2.getIsInsideLocation());
						attendanceDetails2.setPunchOut(LocalDateTime.now());
						return empLogin;
					})
					.orElseGet(() -> EmployeeAttendanceDetails
							.builder().monthNo(monthValue).year(year).employeeInfoId(employeeInfoId)
							.companyId(getCompanyId()).attendanceDetails(List.of(AttendanceDetails.builder()
									.punchOut(LocalDateTime.now()).isInsideLocation(validRedius).detailsId(1).build()))
							.build());
			employeeAttendanceDetailsRepository.save(employeeAttendanceDetails);
			return "Attendance Added Successfully";
		}).orElseThrow(() -> new DataNotFoundException("Employee Does Not Exist"));
	}

}
