package com.te.zealhr.service.employee;

import java.util.List;

import com.te.zealhr.dto.employee.EmployeeTeamDTO;

public interface EmployeeHierarchyService {

	//Map<String,String> getHierarchy(Long employeeInfoId);
	EmployeeTeamDTO getEmployeeHierarchy(Long employeeInfoId, Long companyId);


}
