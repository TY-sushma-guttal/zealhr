package com.te.zealhr.service.hr;

import com.te.zealhr.dto.hr.EmployeeSalaryAllDetailsDTO;

public interface EmployeeSalaryCalcService {
	
	Boolean calculateAllEmployeeSalary ();
	
	EmployeeSalaryAllDetailsDTO editEmployeeSalary (EmployeeSalaryAllDetailsDTO employeeSalaryAllDetailsDTO);
	
	Boolean calculateReviseSalary ();
	
}
