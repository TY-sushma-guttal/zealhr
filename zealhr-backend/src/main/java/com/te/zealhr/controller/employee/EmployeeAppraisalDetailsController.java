package com.te.zealhr.controller.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.employee.AppraisalMeetingDTO;
import com.te.zealhr.dto.employee.EmployeePerformanceDetailsDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.employee.EmployeeAppraisalDetailsService;

@RestController
@RequestMapping("/api/v1/appraisal-details")
@CrossOrigin(origins = "*")
public class EmployeeAppraisalDetailsController {

	@Autowired
	private EmployeeAppraisalDetailsService employeeApprisalDetailsService;

	@GetMapping("/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> getAdvanceSalary(@PathVariable("employeeInfoId") Long employeeInfoId) {
		AppraisalMeetingDTO employeeApprisalDetails = employeeApprisalDetailsService
				.getEmployeeApprisalDetails(employeeInfoId);
		return new ResponseEntity<>(SuccessResponse.builder().data(employeeApprisalDetails).error(false)
				.message("Data Fetched Successfully").build(), HttpStatus.OK);
	}

}
