package com.te.zealhr.service.reportingmanager;

import java.util.List;

import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.dto.admin.AdvancedSalaryDTO;

public interface ReportingManagerAdvanceSalaryApprovalService {

	List<AdvancedSalaryDTO> getAdvanceSalaryByStatus(Long companyId, String status, Long employeeInfoId);
	
	AdvancedSalaryDTO getEmployeeAdvanceSalary(Long companyId, Long employeeAdvanceSalaryId);
	
	String addEmployeeAdvanceSalary(Long companyId, Long advanceSalaryId, Long employeeInfoId,
			String employeeId, AdminApprovedRejectDto adminApprovedRejectDto);
}
