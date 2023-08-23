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
import com.te.zealhr.service.admin.AdminReimbursementInfoService;
import com.te.zealhr.audit.BaseConfigController;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminReimbursementController extends BaseConfigController {

	private final AdminReimbursementInfoService employeeReimbursementInfoService;

	@GetMapping("/reimbursement-approvals/{status}")
	public ResponseEntity<SuccessResponse> getAllEmployeeReimbursement(@PathVariable String status) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Reimbursement Approval Details")
						.data(employeeReimbursementInfoService.getAllEmployeeReimbursement(getCompanyId(), status))
						.build());
	}

	@GetMapping("/reimbursement-approval/{reimbursmentId}")
	public ResponseEntity<SuccessResponse> getEmployeeReimbursement(@PathVariable Long reimbursmentId) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Reimbursement Approval Details")
						.data(employeeReimbursementInfoService.getEmployeeReimbursement(getCompanyId(), reimbursmentId))
						.build());
	}

	@PutMapping("/reimbursement-approval/{reimbursmentId}/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> employeeLeaveDetails(@PathVariable Long reimbursmentId,
			@PathVariable Long employeeInfoId, @RequestBody AdminApprovedRejectDto adminApprovedRejectDto) {
		return ResponseEntity
				.status(HttpStatus.OK).body(
						SuccessResponse.builder().error(Boolean.FALSE)
								.message(employeeReimbursementInfoService.addEmployeeReimbursement(getCompanyId(),
										employeeInfoId, reimbursmentId, getEmployeeId(), adminApprovedRejectDto))
								.build());
	}

}
