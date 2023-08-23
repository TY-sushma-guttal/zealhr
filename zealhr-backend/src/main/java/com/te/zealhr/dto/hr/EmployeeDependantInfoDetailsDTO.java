package com.te.zealhr.dto.hr;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EmployeeDependantInfoDetailsDTO {

	private Long dependentId;
	private String fullName;
	private String relationShip;
	private String gender;
	private LocalDate dob;
	private Long mobileNumber;
	
}
