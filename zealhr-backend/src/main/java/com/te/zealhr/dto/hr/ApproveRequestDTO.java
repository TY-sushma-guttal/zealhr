package com.te.zealhr.dto.hr;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ApproveRequestDTO {

	private Long personalId;
	private String employeeId;
	private Object roles;
	private String officialEmailId;
	private LocalDate doj;

}
