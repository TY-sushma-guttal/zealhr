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
import com.te.zealhr.service.reportingmanager.ReportingManagerResignationApprovalService;
import com.te.zealhr.audit.BaseConfigController;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/api/v1/rm/resignation-approval")
public class ReportingManagerResignationApprovalController extends BaseConfigController {

	private final ReportingManagerResignationApprovalService service;

	@GetMapping("/all/{status}/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> getResignationRequestList(@PathVariable String status,
			@PathVariable Long employeeInfoId) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Data Fetched Successfully")
						.data(service.getResignationRequestList(status, employeeInfoId, getCompanyId())).build());
	}

	@GetMapping("/{resignationId}")
	public ResponseEntity<SuccessResponse> getResignationRequest(@PathVariable Long resignationId) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Data Fetched Successfully")
						.data(service.getResignationRequest(resignationId, getCompanyId())).build());
	}

	@PutMapping("/{resignationId}/{employeeId}")
	public ResponseEntity<SuccessResponse> addResignationResponse(@PathVariable Long resignationId,
			@PathVariable String employeeId, @RequestBody AdminApprovedRejectDto adminApprovedRejectDto) {

		String message = service.addResignationResponse(getCompanyId(), resignationId, employeeId,
				adminApprovedRejectDto);

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message(message).data(message).build());
	}
}