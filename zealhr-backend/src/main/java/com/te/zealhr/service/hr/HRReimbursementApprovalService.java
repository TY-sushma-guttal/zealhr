package com.te.zealhr.service.hr;

import java.util.List;

import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.dto.admin.EmployeeReimbursementInfoDTO;
import com.te.zealhr.dto.hr.CanlenderRequestDTO;

public interface HRReimbursementApprovalService {

	List<EmployeeReimbursementInfoDTO> getAllEmployeeReimbursement(Long companyId, Long employeeInfoId , CanlenderRequestDTO canlenderRequestDTO);

	EmployeeReimbursementInfoDTO getEmployeeReimbursement(Long companyId, Long employeeReimbursementId);

	String updateReimbursementStatus(Long companyId, Long employeeInfoId, Long employeeReimbursementId,
			String employeeId, AdminApprovedRejectDto adminApprovedRejectDto);

}
