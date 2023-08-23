package com.te.zealhr.service.employee;

import com.te.zealhr.dto.employee.EmployeeExtraWorkDTO;

public interface EmployeeExtraWorkService {
	
	EmployeeExtraWorkDTO saveExtraWorkDetails(EmployeeExtraWorkDTO employeeExtraWorkDTO, Long employeeInfoId);

}
