package com.te.zealhr.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDropdownDTO {
	private Long employeeInfoId;
	private String employeeId;
	private String employeeFullName;
	private String pictureURL;
	
	public EmployeeDropdownDTO(Long employeeInfoId, String employeeId, String employeeFullName) {
		super();
		this.employeeInfoId = employeeInfoId;
		this.employeeId = employeeId;
		this.employeeFullName = employeeFullName;
	}
	
}
