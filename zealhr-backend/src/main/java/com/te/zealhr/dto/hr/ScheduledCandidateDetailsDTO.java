package com.te.zealhr.dto.hr;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ScheduledCandidateDetailsDTO {
	private Long candidateId;
	private String firstName;
	private String emailId;
	private Long mobileNumber;
	private Long departmentId;
	private String department;
	private String designationName;
	private String interviewType;
	private String interviewDetails;
	private LocalDate interviewDate;
	private String interviewerName;
	private LocalTime startTime;
	private Integer duration;
	private Integer roundOfInterview;
	private String status;
	private Map<String, String> feedback;
	private String resumeUrl;
	private Long employeePersonalInfo;
	private Long interviewId;
	private String roundName;
	private List<String> others;
	private Long requirementId;
	private String jobRole;
	private String jobDescription;
	private List<String> skills;

}
