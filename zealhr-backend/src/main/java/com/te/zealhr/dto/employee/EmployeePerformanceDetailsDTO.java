package com.te.zealhr.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePerformanceDetailsDTO {
	private String month;
	private Long employeeInfoId;
	private String year;
	private String fiscalMonth;
}
