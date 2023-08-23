package com.te.zealhr.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.admin.CompanyLeadCategoriesDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.admin.CompanyLeadCategoriesService;
import com.te.zealhr.audit.BaseConfigController;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/company")
public class CompanyLeadCategoriesController extends BaseConfigController {

	@Autowired
	CompanyLeadCategoriesService companyLeadCategoriesService;

	@GetMapping("/lead")
	public ResponseEntity<SuccessResponse> getLead() {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message(" Company lead catagories fetched successfully ")
						.data(companyLeadCategoriesService.getLead(getCompanyId())).build());
	}

	@PutMapping("/lead")
	public ResponseEntity<SuccessResponse> updateLead(
			@RequestBody List<CompanyLeadCategoriesDTO> companyLeadCategoriesDto) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Lead Updated Successfully")
						.data(companyLeadCategoriesService.updateLead(companyLeadCategoriesDto, getCompanyId()))
						.build());
	}

}
