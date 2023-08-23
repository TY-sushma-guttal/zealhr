package com.te.zealhr.dto.employee.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTaskListDTO {

	private String taskId;
	
	private String taskName;
}
