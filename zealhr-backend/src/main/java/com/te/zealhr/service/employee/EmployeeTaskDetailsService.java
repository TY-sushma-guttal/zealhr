package com.te.zealhr.service.employee;

import java.util.List;

import com.te.zealhr.dto.employee.mongo.EmployeeTaskCommentDTO;
import com.te.zealhr.dto.reportingmanager.EmployeeTaskDetailsDTO;

public interface EmployeeTaskDetailsService {

	List<EmployeeTaskDetailsDTO> getAllTaskDetails(String employeeId, String status, Long companyId);

	Boolean editComment(EmployeeTaskCommentDTO detailsDTO);

}
