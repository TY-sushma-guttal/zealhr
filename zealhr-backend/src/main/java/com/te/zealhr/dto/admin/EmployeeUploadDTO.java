package com.te.zealhr.dto.admin;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeUploadDTO {

	private String employeeId;
	private String firstName;
	private String officialEmailId;
	private String department;
	private String designation;
	private LocalDate doj;
}
