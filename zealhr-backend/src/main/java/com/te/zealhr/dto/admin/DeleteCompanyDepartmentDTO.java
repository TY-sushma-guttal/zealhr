package com.te.zealhr.dto.admin;

import lombok.Data;

@Data
public class DeleteCompanyDepartmentDTO {
	
	private Long companyId;
	private Long companyDepartmentId;
	private String companyDepartmentName;

}
