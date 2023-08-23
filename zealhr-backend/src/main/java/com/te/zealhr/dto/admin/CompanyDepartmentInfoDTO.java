package com.te.zealhr.dto.admin;

import java.util.Set;

import com.te.zealhr.dto.employee.CompanyRegistrationDto;

import lombok.Data;

@Data
public class CompanyDepartmentInfoDTO {
	private Long companyDepartmentId;
	private String companyDepartmentName;
	private Boolean isSubmited;
	private Object companyDepartmentRoles;
	private CompanyRegistrationDto companyInfo;
	private Set<CompanyDepartmentInfoDTO> childCompanyDepartmentInfoDTO;

}
