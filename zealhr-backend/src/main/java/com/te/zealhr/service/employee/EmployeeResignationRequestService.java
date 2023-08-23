package com.te.zealhr.service.employee;

import com.te.zealhr.dto.employee.CompanyEmployeeResignationDetailsDTO;
import com.te.zealhr.entity.employee.CompanyEmployeeResignationDetails;
import com.te.zealhr.repository.employee.EmployeeResignationRequestRepository;

public interface EmployeeResignationRequestService {

	CompanyEmployeeResignationDetailsDTO saveEmployeeResignation(CompanyEmployeeResignationDetailsDTO resignationDetails,Long employeeInfoId,Long companyId);

	CompanyEmployeeResignationDetailsDTO getEmployeeResignation(Long employeeInfoId,Long companyId);
}
