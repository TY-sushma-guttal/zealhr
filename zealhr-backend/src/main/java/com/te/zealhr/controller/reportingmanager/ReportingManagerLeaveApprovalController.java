package com.te.zealhr.controller.reportingmanager;

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
import com.te.zealhr.service.reportingmanager.ReportingManagerLeaveApprovalService;
import com.te.zealhr.audit.BaseConfigController;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/rm/leave-approval")
@RequiredArgsConstructor
public class ReportingManagerLeaveApprovalController extends BaseConfigController {

	private final ReportingManagerLeaveApprovalService service;

	@GetMapping("/all/{status}/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> getLeaveDetailsByStatus(@PathVariable String status,
			@PathVariable Long employeeInfoId) {

		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false).message("Data Fetched")
				.data(service.getLeaveDetailsByStatus(getCompanyId(), status, employeeInfoId)).build());
	}

	@GetMapping("/{leaveAppliedId}/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> getLeaveDetailsById(@PathVariable Long leaveAppliedId,
			@PathVariable Long employeeInfoId) {

		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false).message("Data Fetched")
				.data(service.getLeaveDetailsById(leaveAppliedId, employeeInfoId, getCompanyId())).build());
	}

	@PutMapping("/{leaveAppliedId}/{employeeInfoId}/{employeeId}")
	public ResponseEntity<SuccessResponse> updateLeaveStatus(@PathVariable Long employeeInfoId,
			@PathVariable Long leaveAppliedId, @PathVariable String employeeId,
			@RequestBody AdminApprovedRejectDto adminApprovedRejectDto) {

		String message = service.updateLeaveStatus(getCompanyId(), employeeInfoId, leaveAppliedId, employeeId,
				adminApprovedRejectDto);
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message(message).data(message).build());
	}
}
