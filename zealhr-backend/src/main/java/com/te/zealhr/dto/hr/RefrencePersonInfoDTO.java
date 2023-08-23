package com.te.zealhr.dto.hr;

import lombok.Data;

@Data
public class RefrencePersonInfoDTO {
	
	private Long referenceId;
	private Long employeeId;
	private String referralName;
	private Long referedEmployeeId;
	private String referedEmployeeOfficialId;
	
}
