package com.te.zealhr.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.admin.AdminAdvanceSalaryService;
import com.te.zealhr.audit.BaseConfigController;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminAdvanceSalaryController extends BaseConfigController {

	private final AdminAdvanceSalaryService employeeAdvanceSalaryService;

	@GetMapping("advance-salary-approvals/{status}")
	public ResponseEntity<SuccessResponse> getAllEmployeeAdvanceSalary(@PathVariable String status) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Fetch All Advance Salary Details")
						.data(employeeAdvanceSalaryService.getAllEmployeeAdvanceSalary(getCompanyId(), status))
						.build());
	}

	@GetMapping("advance-salary-approval/{advanceSalaryId}")
	public ResponseEntity<SuccessResponse> getEmployeeAdvanceSalary(@PathVariable Long advanceSalaryId) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Advance Salary Details")
						.data(employeeAdvanceSalaryService.getEmployeeAdvanceSalary(getCompanyId(), advanceSalaryId))
						.build());
	}

	@PutMapping("advance-salary-approval/{advanceSalaryId}/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> addEmployeeAdvanceSalary(@PathVariable Long advanceSalaryId,
			@PathVariable Long employeeInfoId, @RequestBody AdminApprovedRejectDto adminApprovedRejectDto) {
		return ResponseEntity
				.status(HttpStatus.OK).body(
						SuccessResponse.builder().error(Boolean.FALSE)
								.message(employeeAdvanceSalaryService.addEmployeeAdvanceSalary(getCompanyId(),
										advanceSalaryId, employeeInfoId, getEmployeeId(), adminApprovedRejectDto))
								.build());
	}
}
