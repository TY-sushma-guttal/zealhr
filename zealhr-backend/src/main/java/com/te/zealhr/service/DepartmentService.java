package com.te.zealhr.service;

import java.util.List;

import com.te.zealhr.dto.hr.EventManagementDepartmentNameDTO;

public interface DepartmentService {

	List<EventManagementDepartmentNameDTO> fetchDepartmentFromPlan(Long companyId);
	
}
