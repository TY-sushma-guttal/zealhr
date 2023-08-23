package com.te.zealhr.service.admin;

import java.util.List;

import com.te.zealhr.dto.admin.CompanyDesignationInfoDto;
import com.te.zealhr.dto.admin.DeleteCompanyDesignationDto;
import com.te.zealhr.dto.admin.DesignationUploadDTO;
import com.te.zealhr.dto.admin.RoleDTO;

public interface CompanyDesignationService {

	CompanyDesignationInfoDto addCompanyDesignation(long companyId, long parentDesignationId, CompanyDesignationInfoDto companyDesignationInfoDto);

	CompanyDesignationInfoDto updateCompanyDesignation(long companyId, CompanyDesignationInfoDto companyDesignationInfoDto);

	List<CompanyDesignationInfoDto> getAllDepartmentDesignation(long companyId, String departmentName);

	String deleteCompanyDesignation(DeleteCompanyDesignationDto deleteCompanyDesignationDto);
	
	Object getRoleForDesinagtion(RoleDTO roleDTO);

	String uploadCompanyDesignation(Long companyId, List<DesignationUploadDTO> designationUploadDTOList);

}
 