package com.te.zealhr.controller.reportingmanager;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.audit.BaseConfigController;
import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.dto.reportingmanager.RegularizationDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.reportingmanager.RegularizationApprovalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/regularization-approval")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RegularizationApprovalController extends BaseConfigController {

	private final RegularizationApprovalService regularizationApprovalService;

	@GetMapping
	ResponseEntity<SuccessResponse> getAllRegularizationDetails() {

		List<RegularizationDTO> allRegularizationDetails = regularizationApprovalService
				.getAllRegularizationDetails(getUserId(), getCompanyId());

		return new ResponseEntity<>(SuccessResponse.builder().data(allRegularizationDetails).error(false)
				.message(allRegularizationDetails.isEmpty() ? "No Regularization Applied"
						: "Regularization Fetched Successfully")
				.build(), HttpStatus.OK);

	}

	@PutMapping("/{regularizationId}")
	ResponseEntity<SuccessResponse> getAllRegularizationDetails(@PathVariable Long regularizationId,
			@RequestBody AdminApprovedRejectDto adminApprovedRejectDto) {

		String updateLeaveStatus = regularizationApprovalService.updateLeaveStatus(getCompanyId(), getEmployeeInfoId(),
				regularizationId, adminApprovedRejectDto);

		return new ResponseEntity<>(
				SuccessResponse.builder().data(updateLeaveStatus).error(false).message(updateLeaveStatus).build(),
				HttpStatus.OK);

	}

}
