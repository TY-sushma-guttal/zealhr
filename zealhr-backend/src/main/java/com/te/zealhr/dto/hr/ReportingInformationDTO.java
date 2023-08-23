package com.te.zealhr.dto.hr;

import lombok.Data;

@Data
public class ReportingInformationDTO {
	
	private Long employeeInfoId;
	private Long reportId;
	private Long reportingManagerId;
	private String reportingManagerName;
	private Long reportingHRid;
	private String reportingHrName;
}
