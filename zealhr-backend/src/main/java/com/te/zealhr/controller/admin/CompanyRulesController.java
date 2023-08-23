package com.te.zealhr.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.admin.CompanyRulesDto;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.admin.CompanyRuleService;
import com.te.zealhr.audit.BaseConfigController;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Brunda
 *
 */

@CrossOrigin(origins = "*")
@Slf4j
@RestController
@RequestMapping("/api/v1/company/rule")
public class CompanyRulesController extends BaseConfigController {

	@Autowired
	private CompanyRuleService companyRuleService;

	@PutMapping
	public ResponseEntity<SuccessResponse> updateCompanyRules(@RequestBody CompanyRulesDto companyRulesDto) {
		log.info("updateCompanyRules Method Start");
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(Boolean.FALSE)
				.message(companyRuleService.updateCompanyRule(getCompanyId(), companyRulesDto)).build());
	}

	@GetMapping
	public ResponseEntity<SuccessResponse> getCompanyRule() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Fetch Company Rules Successfully!!!")
						.data(companyRuleService.getCompanyRules(getCompanyId())).build());
	}
}
