package com.te.zealhr.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.admin.TermsAndConditionDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.admin.TermsAndConditionService;
import com.te.zealhr.audit.BaseConfigController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/terms/conditions")
public class TermsAndConditionController extends BaseConfigController{
	
	@Autowired
	private TermsAndConditionService termsAndConditionService;
	
	@PostMapping
	public ResponseEntity<SuccessResponse> saveTermsAndCondition(@RequestBody TermsAndConditionDTO termsAndConditionDTO) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Terms and Condition Added Successfully")
						.data(termsAndConditionService.addTermsAndCondition(termsAndConditionDTO, getCompanyId())).build());
	}
	
	@GetMapping
	public ResponseEntity<SuccessResponse> getAllTermsAndCondition() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Terms and Condition Added Successfully")
						.data(termsAndConditionService.getAllTermsAndConditions(getCompanyId())).build());
	}

}
