package com.te.zealhr.controller.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.employee.EmployeeTeamDTO;
import com.te.zealhr.dto.employee.GetReportingManagerDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.employee.EmployeeHierarchyService;
import com.te.zealhr.audit.BaseConfigController;

@RestController
@RequestMapping("api/v1/employee-hierarchy")
@CrossOrigin(origins = "*")
public class EmployeeHierarchyController extends BaseConfigController{
	@Autowired
	private EmployeeHierarchyService hierarchyService;

	@GetMapping("/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> getHierarchy(@PathVariable Long employeeInfoId) {
		
		EmployeeTeamDTO employeeHierarchy = hierarchyService.getEmployeeHierarchy(employeeInfoId, getCompanyId());
		if (employeeHierarchy != null) {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(employeeHierarchy).error(false).message(null).build(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(employeeHierarchy).error(true).message(null).build(), HttpStatus.NOT_FOUND);

		}
	}
}
