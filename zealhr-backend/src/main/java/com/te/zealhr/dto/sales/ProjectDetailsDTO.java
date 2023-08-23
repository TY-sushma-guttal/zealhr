package com.te.zealhr.dto.sales;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.te.zealhr.entity.admin.CompanyInfo;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.sales.CompanyClientInfo;

import lombok.Data;

@Data

public class ProjectDetailsDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long projectId;
	private String projectName;
	private String projectDescription;
	
	@JsonFormat(pattern = "MM-dd-yyyy"  , timezone = "Asia/kolkata")
	private LocalDate startDate;
//	private List<EmployeePersonalInfo> employeePersonalInfoList;
	private CompanyClientInfo companyClientInfo;
	private CompanyInfo companyInfo;
	private EmployeePersonalInfo projectManager;
	private EmployeePersonalInfo reportingManager;
//	private ProjectEstimationDetails projectEstimationDetails;
}
