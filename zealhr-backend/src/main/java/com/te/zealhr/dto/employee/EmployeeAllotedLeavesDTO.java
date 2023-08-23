package com.te.zealhr.dto.employee;

import lombok.Data;

@Data
public class EmployeeAllotedLeavesDTO {
	
	private String leaveType;
	
	private Double allottedLeave;
	
	private Double remainingLeave;

}
