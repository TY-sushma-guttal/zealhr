package com.te.zealhr.controller.hr;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.audit.BaseConfigController;
import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.dto.hr.CanlenderRequestDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.hr.HRReimbursementApprovalService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/hr/reimbursement-approval")
@RequiredArgsConstructor
public class HRReimbursementApprovalController extends BaseConfigController {

	private final HRReimbursementApprovalService hrReimbursementApprovalService;

	@PostMapping
	public ResponseEntity<SuccessResponse> getAllEmployeeReimbursement(@RequestBody CanlenderRequestDTO canlenderRequestDTO) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Reimbursement Approval Details")
						.data(hrReimbursementApprovalService.getAllEmployeeReimbursement(getCompanyId(), getUserId(), canlenderRequestDTO))
						.build());
	}

	@GetMapping("by-id/{reimbursmentId}")
	public ResponseEntity<SuccessResponse> getEmployeeReimbursement(@PathVariable Long reimbursmentId) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Reimbursement Approval Details")
						.data(hrReimbursementApprovalService.getEmployeeReimbursement(getCompanyId(), reimbursmentId))
						.build());
	}

	@PutMapping("/{reimbursmentId}/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> updateReimbursementStatus(@PathVariable Long reimbursmentId,
			@PathVariable Long employeeInfoId, @RequestBody AdminApprovedRejectDto adminApprovedRejectDto) {
		String result = hrReimbursementApprovalService.updateReimbursementStatus(getCompanyId(), employeeInfoId,
				reimbursmentId, getEmployeeId(), adminApprovedRejectDto);
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message(result).data(result).build());
	}

}
