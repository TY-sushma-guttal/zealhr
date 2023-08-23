package com.te.zealhr.service.reportingmanager;

import static com.te.zealhr.common.admin.EmployeeAdvanceSalaryConstants.APPROVED;
import static com.te.zealhr.common.admin.EmployeeAdvanceSalaryConstants.PENDING;
import static com.te.zealhr.common.admin.EmployeeAdvanceSalaryConstants.REJECTED;
import static com.te.zealhr.common.admin.EmployeeAdvanceSalaryConstants.RM;
import static com.te.zealhr.common.admin.EmployeeAdvanceSalaryConstants.ADMIN;
import static com.te.zealhr.common.admin.EmployeeAdvanceSalaryConstants.RM_APPROVAL_NOT_REQUIRED;
import static com.te.zealhr.common.admin.EmployeeAdvanceSalaryConstants.STATUS_IS_NOT_VALIED;
import static com.te.zealhr.common.admin.EmployeeReimbursementInfoConstants.COMPANY_NOT_FOUND;
import static com.te.zealhr.common.admin.EmployeeReimbursementInfoConstants.EMPLOYEE_NOT_FOUND;
import static com.te.zealhr.common.admin.EmployeeAdvanceSalaryConstants.HR;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.dto.admin.EmployeeReimbursementInfoDTO;
import com.te.zealhr.entity.admin.LevelsOfApproval;
import com.te.zealhr.entity.employee.EmployeeOfficialInfo;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.employee.EmployeeReimbursementInfo;
import com.te.zealhr.entity.employee.EmployeeReportingInfo;
import com.te.zealhr.exception.DataNotFoundException;
import com.te.zealhr.exception.hr.CompanyNotFoundException;
import com.te.zealhr.repository.admin.LevelsOfApprovalRepository;
import com.te.zealhr.repository.employee.EmployeeReimbursementInfoRepository;
import com.te.zealhr.repository.employee.EmployeeReportingInfoRepository;
import com.te.zealhr.service.notification.employee.InAppNotificationServiceImpl;
import com.te.zealhr.service.notification.employee.PushNotificationService;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class ReportingManagerReimbursementApprovalServiceImpl implements ReportingManagerReimbursementApprovalService {

	private final LevelsOfApprovalRepository levelsOfApprovalRepository;

	private final EmployeeReportingInfoRepository employeeReportingInfoRepository;

	private final EmployeeReimbursementInfoRepository employeeReimbursementRepository;
	
	private final InAppNotificationServiceImpl notificationServiceImpl;
	
	private final PushNotificationService pushNotificationService;

	private Optional<String> optional = Optional.of("optional");

	@Override
	public List<EmployeeReimbursementInfoDTO> getReimbursementByStatus(Long companyId, String status,
			Long employeeInfoId) {

		List<String> reimbursementLevel = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getReimbursement)
				.orElseThrow(() -> new DataNotFoundException(COMPANY_NOT_FOUND));

		if (!reimbursementLevel.contains(RM)) {
			throw new DataNotFoundException(RM_APPROVAL_NOT_REQUIRED);
		}

		List<Long> employeeInfoIdList = employeeReportingInfoRepository
				.findByReportingManagerEmployeeInfoId(employeeInfoId).stream()
				.map(employee -> employee.getEmployeePersonalInfo().getEmployeeInfoId()).collect(Collectors.toList());

		List<EmployeeReimbursementInfo> reimbursementList = employeeReimbursementRepository
				.findByStatusAndEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoIdIn(status,
						companyId, employeeInfoIdList);

		List<EmployeeReimbursementInfo> filteredList = reimbursementList.stream()
				.filter(ReimbursementRequest -> ReimbursementRequest.getStatus().equalsIgnoreCase(status))
				.collect(Collectors.toList());

		return filteredList.stream()
				.filter(reimbursement -> reimbursement.getEmployeePersonalInfo() != null
						&& reimbursement.getEmployeePersonalInfo().getEmployeeOfficialInfo() != null)
				.map(reimbursementInfo -> {
					EmployeePersonalInfo employeePersonalInfo = reimbursementInfo.getEmployeePersonalInfo();
					EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();

					String pendingAt = null;
					if (reimbursementInfo.getStatus().equalsIgnoreCase(PENDING)) {
						if (reimbursementLevel.size() == 1) {
							pendingAt = RM;
						} else {
							pendingAt = (reimbursementInfo.getApprovedBy().keySet().contains(RM)
									&& reimbursementInfo.getApprovedBy().keySet().contains(HR)
											? ADMIN
											: reimbursementInfo.getApprovedBy().keySet().contains(RM)
													? reimbursementLevel.get(1)
													: RM);
						}
					}
					List<EmployeeReportingInfo> employeeInfoList = employeePersonalInfo.getEmployeeInfoList();
					EmployeePersonalInfo reportingManagerDetails = null;
					if(!employeeInfoList.isEmpty()) {
						reportingManagerDetails =employeeInfoList.get(employeeInfoList.size()-1).getReportingManager();
					}
					String reportingManager = (reportingManagerDetails == null) ? null
							: reportingManagerDetails.getFirstName();
					return EmployeeReimbursementInfoDTO.builder()
							.employeeReimbursementId(reimbursementInfo.getReimbursementId())
							.employeeInfoId(reimbursementInfo.getEmployeePersonalInfo().getEmployeeInfoId())
							.amount(reimbursementInfo.getAmount())
							.department(employeeOfficialInfo.getDepartment())
							.designation(employeeOfficialInfo.getDesignation())
							.branch(employeeOfficialInfo.getCompanyBranchInfo().getBranchName())
							.description(reimbursementInfo.getDescription())
							.employeeId(employeeOfficialInfo.getEmployeeId())
							.employeeName(
									employeePersonalInfo.getFirstName())
							.expenseDate(reimbursementInfo.getExpenseDate())
							.isActionRequired(status.equalsIgnoreCase(PENDING)
									? !(reimbursementInfo.getApprovedBy().keySet().contains(RM))
									: null)
							.link(reimbursementInfo.getAttachmentUrl()).pendingAt(pendingAt)
							.status(reimbursementInfo.getStatus()).rejectedBy(reimbursementInfo.getRejectedBy())
							.reason(reimbursementInfo.getRejectedReason())
							.reimbursementType(reimbursementInfo.getCompanyExpenseCategories().getExpenseCategoryName())
							.reportingManager(reportingManager)
							.build();

				}).collect(Collectors.toList());
	}

	@Override
	public EmployeeReimbursementInfoDTO getReimbursementDetails(Long companyId, Long reimbursementId) {

		List<String> reimbursementLevel = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getReimbursement)
				.orElseThrow(() -> new DataNotFoundException(COMPANY_NOT_FOUND));

		return employeeReimbursementRepository
				.findByReimbursementIdAndEmployeePersonalInfoCompanyInfoCompanyId(reimbursementId, companyId)
				.map(reimbursementInfo -> {
					EmployeePersonalInfo employeePersonalInfo = reimbursementInfo.getEmployeePersonalInfo();
					if (employeePersonalInfo == null) {
						throw new DataNotFoundException(EMPLOYEE_NOT_FOUND);
					}
					EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
					if (employeeOfficialInfo == null) {
						throw new DataNotFoundException(EMPLOYEE_NOT_FOUND);
					}
					String pendingAt = null;
					if (reimbursementInfo.getStatus().equalsIgnoreCase(PENDING)) {
						if (reimbursementLevel.size() == 1) {
							pendingAt = RM;
						} else {
							pendingAt = (reimbursementInfo.getApprovedBy().keySet().contains(RM)
									&& reimbursementInfo.getApprovedBy().keySet().contains(HR)
											? ADMIN
											: reimbursementInfo.getApprovedBy().keySet().contains(RM)
													? reimbursementLevel.get(1)
													: RM);
						}
					}
					List<EmployeeReportingInfo> employeeInfoList = employeePersonalInfo.getEmployeeInfoList();
					EmployeePersonalInfo reportingManagerDetails = null;
					if(!employeeInfoList.isEmpty()) {
						reportingManagerDetails =employeeInfoList.get(employeeInfoList.size()-1).getReportingManager();
					}
					String reportingManager = (reportingManagerDetails == null) ? null
							: reportingManagerDetails.getFirstName();
					return EmployeeReimbursementInfoDTO.builder().employeeReimbursementId(reimbursementId)
							.employeeInfoId(employeePersonalInfo.getEmployeeInfoId())
							.amount(reimbursementInfo.getAmount()).department(employeeOfficialInfo.getDepartment())
							.designation(employeeOfficialInfo.getDesignation())
							.branch(employeeOfficialInfo.getCompanyBranchInfo().getBranchName())
							.description(reimbursementInfo.getDescription())
							.employeeId(employeeOfficialInfo.getEmployeeId())
							.employeeName(
									employeePersonalInfo.getFirstName())
							.expenseDate(reimbursementInfo.getExpenseDate())
							.isActionRequired(reimbursementInfo.getStatus().equalsIgnoreCase(PENDING)
									? !(reimbursementInfo.getApprovedBy().keySet().contains(RM))
									: null)
							.link(reimbursementInfo.getAttachmentUrl()).pendingAt(pendingAt)
							.status(reimbursementInfo.getStatus()).rejectedBy(reimbursementInfo.getRejectedBy())
							.reason(reimbursementInfo.getRejectedReason())
							.reimbursementType(reimbursementInfo.getCompanyExpenseCategories().getExpenseCategoryName())
							.reportingManager(reportingManager)
							.build();

				}).orElseThrow(() -> new DataNotFoundException("data not found or request got responsed already"));
	}

	
	@Override
	@Transactional
	public String addEmployeeReimbursementInfo(Long companyId, Long reimbursementId, Long employeeInfoId,
			String employeeId, AdminApprovedRejectDto adminApprovedRejectDto) {
		List<String> levelsOfApproval = levelsOfApprovalRepository.findByCompanyInfoCompanyId(companyId)
				.map(LevelsOfApproval::getReimbursement)
				.orElseThrow(() -> new CompanyNotFoundException(COMPANY_NOT_FOUND));
		if (!levelsOfApproval.contains(RM)) {
			throw new DataNotFoundException(RM_APPROVAL_NOT_REQUIRED);
		}
		return employeeReimbursementRepository
				.findByReimbursementIdAndEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeInfoId(
						reimbursementId, companyId, employeeInfoId)
				.filter(reimbursementInfo -> reimbursementInfo.getStatus().equalsIgnoreCase(PENDING))
				.map(reimbursementInfo -> {
					return optional
							.filter(rejectStatus -> adminApprovedRejectDto.getStatus().equalsIgnoreCase(REJECTED))
							.map(rej -> {
								reimbursementInfo.setStatus(adminApprovedRejectDto.getStatus());
								reimbursementInfo.setRejectedBy(RM);
								reimbursementInfo.setRejectedReason(adminApprovedRejectDto.getReason());
								notificationServiceImpl.saveNotification("Reimbursement request for the amount "+reimbursementInfo.getAmount()+" is Rejected by Reporting Manager",
										reimbursementInfo.getEmployeePersonalInfo().getEmployeeInfoId());
								
								
								if (reimbursementInfo.getEmployeePersonalInfo().getExpoToken() != null) {
									pushNotificationService.pushMessage("zealhr","Reimbursement request for the amount "+reimbursementInfo.getAmount()+" is Rejected by Reporting Manager",
											reimbursementInfo.getEmployeePersonalInfo().getExpoToken());
									}
								return "Request is rejected";
							})
							.orElseGet(() -> optional.filter(
									selectStatus -> adminApprovedRejectDto.getStatus().equalsIgnoreCase(APPROVED))
									.map(sel -> {
										LinkedHashMap<String, String> approvedBy = new LinkedHashMap<>();
										approvedBy.put(RM, employeeId);
										reimbursementInfo.setApprovedBy(approvedBy);
										if (levelsOfApproval.size() == 1) {
											reimbursementInfo.setStatus(APPROVED);
										}
										
										notificationServiceImpl.saveNotification("Reimbursement request for the amount "+reimbursementInfo.getAmount()+" is Approved by Reporting Manager",
												reimbursementInfo.getEmployeePersonalInfo().getEmployeeInfoId());
										
										if (reimbursementInfo.getEmployeePersonalInfo().getExpoToken() != null) {
											pushNotificationService.pushMessage("zealhr","Reimbursement request for the amount "+reimbursementInfo.getAmount()+" is Approved by Reporting Manager",
													reimbursementInfo.getEmployeePersonalInfo().getExpoToken());
											}
										
										return "Request is approved";
									}).orElseGet(() -> STATUS_IS_NOT_VALIED));
				}).orElseThrow(() -> new DataNotFoundException("data not found or request got responsed already"));
	}

}
