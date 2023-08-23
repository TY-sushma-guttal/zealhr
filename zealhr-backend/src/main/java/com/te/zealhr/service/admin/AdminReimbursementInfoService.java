package com.te.zealhr.service.admin;

import java.util.List;

import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.dto.admin.EmployeeReimbursementInfoDTO;

public interface AdminReimbursementInfoService {

	List<EmployeeReimbursementInfoDTO> getAllEmployeeReimbursement(Long companyId,String status);

	EmployeeReimbursementInfoDTO getEmployeeReimbursement(Long companyId, Long employeeReimbursementId);

	String addEmployeeReimbursement(Long companyId, Long employeeInfoId, Long employeeReimbursementId,
			String employeeId, AdminApprovedRejectDto adminApprovedRejectDto);

}
