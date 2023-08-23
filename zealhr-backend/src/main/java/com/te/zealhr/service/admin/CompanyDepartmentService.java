package com.te.zealhr.service.admin;

import java.util.List;

import com.te.zealhr.dto.admin.CompanyDepartmentInfoDTO;
import com.te.zealhr.dto.admin.DeleteCompanyDepartmentDTO;

public interface CompanyDepartmentService {

	CompanyDepartmentInfoDTO addCompanyDepartment(Long companyId, Long parentDepartmentId,
			CompanyDepartmentInfoDTO companyDepartmentInfoDTO);
	
	CompanyDepartmentInfoDTO updateCompanyDepartment(Long companyId,
			CompanyDepartmentInfoDTO companyDepartmentInfoDTO);
	
	List<CompanyDepartmentInfoDTO> getAllDepartments(Long companyId);
	
	String deleteCompanyDesignation(DeleteCompanyDepartmentDTO deleteCompanyDepartmentDTO);

	Object getRoleForDepartment();
}
