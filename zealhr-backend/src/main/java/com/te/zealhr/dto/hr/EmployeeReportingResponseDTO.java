package com.te.zealhr.dto.hr;

import lombok.Data;

@Data
public class EmployeeReportingResponseDTO {
	
	private Long reportId;
	private String employeeName;
	private String reportingManagerName;
	private String reportingHrName;
	
}
