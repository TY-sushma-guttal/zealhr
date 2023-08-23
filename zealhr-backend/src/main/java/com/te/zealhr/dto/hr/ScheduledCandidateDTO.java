package com.te.zealhr.dto.hr;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ScheduledCandidateDTO {
	
	private Long candidateId;

	private String firstName;

	private String emailId;

	private Long mobileNumber;

	private String designationName;

	private String employementStatus;

	private String interviewType;

	private String interviewDetails;

	private LocalDate interviewDate;

	private LocalTime startTime;

	private Integer duration;

	private Long employeePersonalInfo;

	private Integer roundOfInterview;

	private String interviewerName;

	private String roundName;

	@Override
	public String toString() {
		return "ShowData [candidateId=" + candidateId + "firstName=" + firstName + ", emailId=" + emailId
				+ ", mobileNumber=" + mobileNumber + ", designationName=" + designationName + ", employementStatus="
				+ employementStatus + ",interviewType=" + interviewType + ",interviewDetails=" + interviewDetails
				+ ",interviewDate=" + interviewDate + ",startTime=" + startTime + ",duration=" + duration
				+ ",employeePersonalInfo=" + employeePersonalInfo + ",roundOfInterview=" + roundOfInterview
				+ ",interviewerName=" + interviewerName + ",roundName=" + roundName + "]";
	}

}
