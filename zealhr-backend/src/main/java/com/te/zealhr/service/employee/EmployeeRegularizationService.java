package com.te.zealhr.service.employee;

import com.te.zealhr.dto.employee.EmployeeRegularizationDetailsDTO;
import com.te.zealhr.dto.employee.RegularizationCalenderDTO;

public interface EmployeeRegularizationService {

	String applyRegularization(EmployeeRegularizationDetailsDTO regularizationDetailsDTO,
			Long companyId, Long employeeInfoId);

	RegularizationCalenderDTO getAllRegularizations(Long companyId, Long employeeInfoId, Integer year, Integer month);
	
	String deleteRegularization(Long regularizationId);

}
