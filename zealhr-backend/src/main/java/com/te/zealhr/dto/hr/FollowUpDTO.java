package com.te.zealhr.dto.hr;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FollowUpDTO {
	private Long candidateId;
	
	private String firstName;

	private String emailId;
	
	private Long mobileNumber;
	
	private String designationName;
	
	private BigDecimal yearOfExperience;

	private String interviewType;

	private String interviewDetails;

	private LocalDate interviewDate;

	private LocalTime startTime;

	private Integer duration;

	private Long employeePersonalInfo;

	private Integer roundOfInterview;

	private Long interviewId;

	@Override
	public String toString() {
		return "FollowUpDto [candidateId=" + candidateId + ",interviewId="+interviewId+", firstName=" + firstName + ", emailId=" + emailId
				+ ", mobileNumber=" + mobileNumber + ", designationName=" + designationName + ", yearOfExperience="
				+ yearOfExperience + ",interviewType=" + interviewType + ",interviewDetails=" + interviewDetails
				+ ",interviewDate=" + interviewDate + ",startTime=" + startTime + ",duration=" + duration
				+ ",employeePersonalInfo=" + employeePersonalInfo + "roundOfInterview=" + roundOfInterview +",]";
	}

}
