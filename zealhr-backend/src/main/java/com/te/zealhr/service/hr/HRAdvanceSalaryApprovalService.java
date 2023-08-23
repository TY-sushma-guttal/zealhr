package com.te.zealhr.service.hr;

import java.util.List;

import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.dto.admin.AdvancedSalaryDTO;
import com.te.zealhr.dto.hr.CanlenderRequestDTO;

public interface HRAdvanceSalaryApprovalService {
	
	List<AdvancedSalaryDTO> getAdvanceSalaryByStatus(Long companyId, Long employeeInfoId, CanlenderRequestDTO canlenderRequestDTO);

	AdvancedSalaryDTO getEmployeeAdvanceSalary(Long companyId, Long employeeReimbursementId);

	String addEmployeeAdvanceSalary(Long companyId, Long advanceSalaryId, Long employeeInfoId,
			String employeeId, AdminApprovedRejectDto adminApprovedRejectDto);

}
