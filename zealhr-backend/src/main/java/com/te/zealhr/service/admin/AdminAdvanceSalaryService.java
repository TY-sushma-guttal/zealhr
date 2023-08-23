package com.te.zealhr.service.admin;

import java.util.List;

import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.dto.admin.AdvancedSalaryDTO;

public interface AdminAdvanceSalaryService {
	
	List<AdvancedSalaryDTO> getAllEmployeeAdvanceSalary(Long companyId,String status);

	AdvancedSalaryDTO getEmployeeAdvanceSalary(Long companyId, Long employeeReimbursementId);

	String addEmployeeAdvanceSalary(Long companyId, Long advanceSalaryId, Long employeeInfoId,
			String employeeId, AdminApprovedRejectDto adminApprovedRejectDto);
}
