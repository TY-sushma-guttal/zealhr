package com.te.zealhr.controller.admin.mongo;

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

import com.te.zealhr.dto.hr.mongo.EmployeeLetterBasicDTO;
import com.te.zealhr.dto.hr.mongo.EmployeeLetterDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.admin.mongo.AdminLetterApprovalService;
import com.te.zealhr.audit.BaseConfigController;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RequestMapping("api/v1/admin/letter/approval")
@RestController
@RequiredArgsConstructor
public class AdminLetterApprovalController extends BaseConfigController {

	private final AdminLetterApprovalService adminLetterApprovalService;

	@GetMapping("{status}")
	public ResponseEntity<SuccessResponse> getLetters(@PathVariable String status) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Fetched Successfully")
						.data(adminLetterApprovalService.getLetters(getCompanyId(), status)).build());
	}

	@PostMapping
	public ResponseEntity<SuccessResponse> getLettersById(@RequestBody EmployeeLetterBasicDTO employeeLetterBasicDTO) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Fetched Successfully")
						.data(adminLetterApprovalService.getLettersById(employeeLetterBasicDTO)).build());
	}

	@PutMapping("{status}")
	public ResponseEntity<SuccessResponse> updateLetter(@RequestBody EmployeeLetterDTO employeeLetterDTO,
			@PathVariable String status) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Letter " + status + " Successfully")
						.data(adminLetterApprovalService.updateStatus(employeeLetterDTO, status)).build());
	}
}
