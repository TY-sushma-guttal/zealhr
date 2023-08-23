package com.te.zealhr.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardRequestDTO {

	private String type;

	private LocalDate startDate;

	private LocalDate endDate;

	private String filterValue;

	private Integer month;

	private Integer year;
	
	private String department;
	
	private List<String> departmentList;

}
