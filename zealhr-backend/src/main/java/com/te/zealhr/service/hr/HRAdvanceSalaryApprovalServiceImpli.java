package com.te.zealhr.service.hr;

import static com.te.zealhr.common.admin.EmployeeAdvanceSalaryConstants.ADMIN;
import static com.te.zealhr.common.admin.EmployeeAdvanceSalaryConstants.APPROVED;
import static com.te.zealhr.common.admin.EmployeeAdvanceSalaryConstants.APPROVE_ADVANCE_SALARY_SUCCESSFULLY;
import static com.te.zealhr.common.admin.EmployeeAdvanceSalaryConstants.DATA_NOT_FOUND_OR_ALREADY_APPROVED_OR_REJECTED;
import static com.te.zealhr.common.admin.EmployeeAdvanceSalaryConstants.HR;
import static com.te.zealhr.common.admin.EmployeeAdvanceSalaryConstants.PENDING;
import static com.te.zealhr.common.admin.EmployeeAdvanceSalaryConstants.REJECTED;
import static com.te.zealhr.common.admin.EmployeeAdvanceSalaryConstants.REJECT_ADVANCE_SALARY_SUCCESSFULLY;
import static com.te.zealhr.common.admin.EmployeeAdvanceSalaryConstants.RM;
import static com.te.zealhr.common.admin.EmployeeAdvanceSalaryConstants.STATUS_IS_NOT_VALIED;
import static com.te.zealhr.common.admin.EmployeeLeaveDetailsConstants.LEVEL_OF_APPROVAL_NOT_FOUND;
import static com.te.zealhr.common.admin.EmployeeReimbursementInfoConstants.EMPLOYEE_NOT_FOUND;
import static com.te.zealhr.common.admin.EmployeeReimbursementInfoConstants.HR_APPROVAL_NOT_REQUIRED;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.dto.admin.AdvancedSalaryDTO;
import com.te.zealhr.dto.hr.CanlenderRequestDTO;
import com.te.zealhr.entity.admin.LevelsOfApproval;
import com.te.zealhr.entity.employee.EmployeeAdvanceSalary;
import com.te.zealhr.entity.employee.EmployeeOfficialInfo;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.employee.EmployeeReportingInfo;
import com.te.zealhr.exception.employee.DataNotFoundException;
import com.te.zealhr.repository.admin.LevelsOfApprovalRepository;
import com.te.zealhr.repository.employee.EmployeeAdvanceSalaryRepository;
import com.te.zealhr.repository.employee.EmployeePersonalInfoRepository;
import com.te.zealhr.repository.employee.EmployeeReportingInfoRepository;
import com.te.zealhr.service.notification.employee.InAppNotificationServiceImpl;
import com.te.zealhr.service.notification.employee.PushNotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class HRAdvanceSalaryApprovalServiceImpli implements HRAdvanceSalaryApprovalService {

	private final EmployeePersonalInfoRepository employeePersonalInfoRepository;

	private final LevelsOfApprovalRepository levelsOfApprovalRepository;

	private final EmployeeAdvanceSalaryRepository employeeAdvanceSalaryRepository;

	private final EmployeeReportingInfoRepository employeeReportingInfoRepository;

	private final InAppNotificationServiceImpl notificationServiceImpl;

	private final PushNotificationService pushNotificationService;

	private final CompanyEventDetailsServiceImpl companyEventDetailsServiceImpl;

	private Optional<String> optional = Optional.of("optional");

	@Override
	public List<AdvancedSalaryDTO> getAdvanceSalaryByStatus(Long companyId, Long employeeInfoId,
			CanlenderRequestDTO canlenderRequestDTO) {

		List<String> advanceSalaryLevels = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getAdvanceSalary)
				.orElseThrow(() -> new DataNotFoundException(LEVEL_OF_APPROVAL_NOT_FOUND));

		if (!advanceSalaryLevels.contains(HR)) {
			throw new DataNotFoundException(HR_APPROVAL_NOT_REQUIRED);
		}

		List<Long> employeeInfoIdList = employeeReportingInfoRepository.findByReportingHREmployeeInfoId(employeeInfoId)
				.stream().map(employee -> employee.getEmployeePersonalInfo().getEmployeeInfoId())
				.collect(Collectors.toList());

		List<LocalDate> startEndDate = companyEventDetailsServiceImpl.getStartEndDate(canlenderRequestDTO.getYear(),
				canlenderRequestDTO.getFiscalMonth());
		List<EmployeeAdvanceSalary> employeeAdvanceSalaryList = employeeAdvanceSalaryRepository
				.findByEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoIdInAndCreatedDateBetween(
						companyId, employeeInfoIdList, startEndDate.get(0).atStartOfDay(),
						startEndDate.get(1).plusDays(1).atStartOfDay());

		employeeAdvanceSalaryList = filterAdvanceSalaryBasedOnCondition(employeeAdvanceSalaryList, advanceSalaryLevels);

		return employeeAdvanceSalaryList.stream()
				.filter(advanceSalary -> advanceSalary.getEmployeePersonalInfo() != null
						&& advanceSalary.getEmployeePersonalInfo().getEmployeeOfficialInfo() != null)
				.map(employeeAdvancedSalary -> {
					EmployeePersonalInfo employee = employeeAdvancedSalary.getEmployeePersonalInfo();
					EmployeeOfficialInfo employeeOfficialInfo = employee.getEmployeeOfficialInfo();
					String pendingAt = null;
					if (employeeAdvancedSalary.getStatus().equalsIgnoreCase(PENDING)) {
						pendingAt = (employeeAdvancedSalary.getApprovedBy().keySet().contains(HR)) ? ADMIN : HR;
					}
					List<EmployeeReportingInfo> employeeInfoList = employee.getEmployeeInfoList();
					EmployeePersonalInfo reportingManagerDetails = null;
					if (!employeeInfoList.isEmpty()) {
						reportingManagerDetails = employeeInfoList.get(employeeInfoList.size() - 1)
								.getReportingManager();
					}
					String reportingManager = (reportingManagerDetails == null) ? null
							: reportingManagerDetails.getFirstName();
					return AdvancedSalaryDTO.builder().employeeInfoId(employee.getEmployeeInfoId())
							.advanceSalaryId(employeeAdvancedSalary.getAdvanceSalaryId())
							.employeeId(employeeOfficialInfo.getEmployeeId()).employeeName(employee.getFirstName())
							.department(employeeOfficialInfo.getDepartment())
							.designation(employeeOfficialInfo.getDesignation())
							.amount(employeeAdvancedSalary.getAmount())
							.requestedOn(employeeAdvancedSalary.getCreatedDate())
							.createdDate(employeeAdvancedSalary.getCreatedDate() != null
									? employeeAdvancedSalary.getCreatedDate().toLocalDate()
									: null)
							.isActionRequired(employeeAdvancedSalary.getStatus().equalsIgnoreCase(PENDING)
									? !(employeeAdvancedSalary.getApprovedBy().keySet().contains(HR))
									: null)
							.reportingManager(reportingManager).pendingAt(pendingAt)
							.rejectedBy(employeeAdvancedSalary.getRejectedBy())
							.reason(employeeAdvancedSalary.getReason())
							.rejectionReason(employeeAdvancedSalary.getRejectedReason()).build();
				}).collect(Collectors.toList());
	}

	private List<EmployeeAdvanceSalary> filterAdvanceSalaryBasedOnCondition(
			List<EmployeeAdvanceSalary> employeeAdvanceSalaryList, List<String> levels) {
		if (levels.get(0).equalsIgnoreCase(HR)) {
			return employeeAdvanceSalaryList;
		} else {
			return employeeAdvanceSalaryList.stream()
					.filter(advanceSalary -> advanceSalary.getApprovedBy().keySet().contains(RM))
					.collect(Collectors.toList());
		}
	}

	@Override
	public AdvancedSalaryDTO getEmployeeAdvanceSalary(Long companyId, Long employeeAdvanceSalaryId) {
		return employeeAdvanceSalaryRepository
				.findByAdvanceSalaryIdAndEmployeePersonalInfoCompanyInfoCompanyId(employeeAdvanceSalaryId, companyId)
				.map(employeeAdvanceSalary -> {
					EmployeePersonalInfo employeePersonalInfo = employeeAdvanceSalary.getEmployeePersonalInfo();
					if (employeePersonalInfo == null) {
						throw new DataNotFoundException(EMPLOYEE_NOT_FOUND);
					}
					EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
					if (employeeOfficialInfo == null) {
						throw new DataNotFoundException(EMPLOYEE_NOT_FOUND);
					}
					String pendingAt = null;
					if (employeeAdvanceSalary.getStatus().equalsIgnoreCase(PENDING)) {
						pendingAt = (employeeAdvanceSalary.getApprovedBy().keySet().contains(HR)) ? ADMIN : HR;
					}
					return AdvancedSalaryDTO.builder().employeeInfoId(employeePersonalInfo.getEmployeeInfoId())
							.advanceSalaryId(employeeAdvanceSalary.getAdvanceSalaryId())
							.employeeId(employeeOfficialInfo.getEmployeeId())
							.employeeName(employeePersonalInfo.getFirstName())
							.department(employeeOfficialInfo.getDepartment())
							.branch(employeePersonalInfo.getCompanyInfo().getCompanyBranchInfoList().stream()
									.filter(company -> company.getCompanyInfo().getCompanyId().equals(companyId))
									.findFirst().orElseThrow().getBranchName())
							.designation(employeeOfficialInfo.getDesignation())
							.requestedOn(employeeAdvanceSalary.getCreatedDate()).emi(employeeAdvanceSalary.getEmi())
							.reason(employeeAdvanceSalary.getReason()).amount(employeeAdvanceSalary.getAmount())
							.isActionRequired(employeeAdvanceSalary.getStatus().equalsIgnoreCase(PENDING)
									? !(employeeAdvanceSalary.getApprovedBy().keySet().contains(HR))
									: null)
							.pendingAt(pendingAt).rejectedBy(employeeAdvanceSalary.getRejectedBy())
							.rejectionReason(employeeAdvanceSalary.getRejectedReason()).build();
				}).orElseThrow();
	}

	@Override
	@Transactional
	public String addEmployeeAdvanceSalary(Long companyId, Long advanceSalaryId, Long employeeInfoId, String employeeId,
			AdminApprovedRejectDto adminApprovedRejectDto) {
		List<String> advanceSalaryLevels = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getAdvanceSalary)
				.orElseThrow(() -> new DataNotFoundException(LEVEL_OF_APPROVAL_NOT_FOUND));
		return employeeAdvanceSalaryRepository
				.findByAdvanceSalaryIdAndEmployeePersonalInfoEmployeeInfoIdAndEmployeePersonalInfoCompanyInfoCompanyId(
						advanceSalaryId, employeeInfoId, companyId)
				.filter(advanceSalary -> !advanceSalary.getStatus().equals(REJECTED)
						&& !advanceSalary.getStatus().equals(APPROVED))
				.map(employeeAdvanceSalary -> {
					return optional.filter(rejectStatus -> adminApprovedRejectDto.getStatus().equals(REJECTED))
							.map(t -> {
								employeeAdvanceSalary.setStatus(adminApprovedRejectDto.getStatus());
								employeeAdvanceSalary.setRejectedBy(ADMIN);
								employeeAdvanceSalary.setRejectedReason(adminApprovedRejectDto.getReason());

								notificationServiceImpl.saveNotification(
										"Advance salary request for the amount " + employeeAdvanceSalary.getAmount()
												+ " is Rejected by HR",
										employeeAdvanceSalary.getEmployeePersonalInfo().getEmployeeInfoId());

								if (employeeAdvanceSalary.getEmployeePersonalInfo().getExpoToken() != null) {
									pushNotificationService.pushMessage("zealhr",
											"Advance salary request for the amount " + employeeAdvanceSalary.getAmount()
													+ " is Rejected by HR",
											employeeAdvanceSalary.getEmployeePersonalInfo().getExpoToken());
								}

								return REJECT_ADVANCE_SALARY_SUCCESSFULLY;
							})
							.orElseGet(() -> optional
									.filter(approvedStatus -> adminApprovedRejectDto.getStatus().equals(APPROVED))
									.map(o -> {
										if (advanceSalaryLevels.get(advanceSalaryLevels.size() - 1)
												.equalsIgnoreCase(HR)) {
											employeeAdvanceSalary.setStatus(adminApprovedRejectDto.getStatus());
										}
										LinkedHashMap<String, String> previousAprovedBy = (employeeAdvanceSalary
												.getApprovedBy().isEmpty()) ? new LinkedHashMap<>()
														: employeeAdvanceSalary.getApprovedBy();
										previousAprovedBy.put(HR, employeeId);
										employeeAdvanceSalary.setApprovedBy(previousAprovedBy);
										notificationServiceImpl.saveNotification(
												"Advance salary request for the amount "
														+ employeeAdvanceSalary.getAmount() + " is Approved by HR",
												employeeAdvanceSalary.getEmployeePersonalInfo().getEmployeeInfoId());

										if (employeeAdvanceSalary.getEmployeePersonalInfo().getExpoToken() != null) {
											pushNotificationService.pushMessage("zealhr",
													"Advance salary request for the amount "
															+ employeeAdvanceSalary.getAmount() + " is Approved by HR",
													employeeAdvanceSalary.getEmployeePersonalInfo().getExpoToken());
										}

										return APPROVE_ADVANCE_SALARY_SUCCESSFULLY;
									}).orElseGet(() -> STATUS_IS_NOT_VALIED));
				}).orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND_OR_ALREADY_APPROVED_OR_REJECTED));

	}

}
