package com.te.zealhr.service.employee;

import java.util.List;

import com.te.zealhr.dto.employee.EmployeeProjectDetailsDTO;
import com.te.zealhr.dto.employee.EmployeeProjectListDTO;

public interface EmployeeDashboardService {
	
	List<EmployeeProjectListDTO> getAllProjectNames(Long employeeInfoId);
	
	EmployeeProjectDetailsDTO getProjectDetailsById(Long projectId, Long companyId);

}
