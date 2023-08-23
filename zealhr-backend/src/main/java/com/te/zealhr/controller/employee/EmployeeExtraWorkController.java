package com.te.zealhr.controller.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.employee.EmployeeExtraWorkDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.employee.EmployeeExtraWorkService;
import com.te.zealhr.audit.BaseConfigController;

@RestController
@RequestMapping("/api/v1/extra-work")
@CrossOrigin(origins = "*")
public class EmployeeExtraWorkController extends BaseConfigController {

	@Autowired
	private EmployeeExtraWorkService employeeExtraWorkService;

	@PostMapping("")
	public ResponseEntity<SuccessResponse> saveExtraWork(@RequestBody EmployeeExtraWorkDTO employeeExtraWorkDTO) {
		employeeExtraWorkDTO = employeeExtraWorkService.saveExtraWorkDetails(employeeExtraWorkDTO, getUserId());
		if (employeeExtraWorkDTO != null)
			return new ResponseEntity<>(SuccessResponse.builder().data(employeeExtraWorkDTO).error(false)
					.message("Extra Work Request Submitted Successfully").build(), HttpStatus.OK);
		else
			return new ResponseEntity<>(SuccessResponse.builder().data(employeeExtraWorkDTO).error(true)
					.message("Data Not Available").build(), HttpStatus.NOT_FOUND);
	}
	

}
