package com.te.zealhr.dto.reportingmanager;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class AppraisalMeetingFeedbackDTO {
	private BigDecimal revisedSalary;
	private String reason;
	private Long meetingId;
//	private String employeeId;
	
		
}
