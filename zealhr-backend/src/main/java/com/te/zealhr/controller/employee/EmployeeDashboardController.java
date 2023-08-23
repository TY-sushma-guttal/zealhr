package com.te.zealhr.controller.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.employee.EmployeeDashboardService;
import com.te.zealhr.audit.BaseConfigController;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/employee/dashboard")
@RequiredArgsConstructor
@RestController
public class EmployeeDashboardController extends BaseConfigController {

	@Autowired
	private EmployeeDashboardService dashboardService;

	@GetMapping("/drop-down")
	public ResponseEntity<SuccessResponse> getProjectDetails() {
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false)
				.message("Projects fetched").data(dashboardService.getAllProjectNames(getUserId())).build());

	}

	@GetMapping("/by-id/{projectId}")
	public ResponseEntity<SuccessResponse> getProjectDetails(@PathVariable Long projectId) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Projects fetched")
						.data(dashboardService.getProjectDetailsById(projectId, getCompanyId())).build());

	}

}
