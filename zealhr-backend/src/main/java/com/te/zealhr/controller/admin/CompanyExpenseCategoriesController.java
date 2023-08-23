package com.te.zealhr.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.admin.CompanyExpenseCategoriesDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.admin.CompanyExpenseCategoriesService;
import com.te.zealhr.audit.BaseConfigController;

import lombok.RequiredArgsConstructor;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class CompanyExpenseCategoriesController extends BaseConfigController {

	private final CompanyExpenseCategoriesService companyExpenseCategoriesService;

	@GetMapping("/expense")
	public ResponseEntity<SuccessResponse> getExpense() {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Expense retrieved successfully")
						.data(companyExpenseCategoriesService.getExpense(null, getCompanyId())).build());
	}

	@PutMapping("/expense")
	public ResponseEntity<SuccessResponse> updateExpense(
			@RequestBody List<CompanyExpenseCategoriesDTO> companyExpenseCategoriesDto) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message(
						companyExpenseCategoriesService.updateExpense(companyExpenseCategoriesDto, getCompanyId()))
						.build());
	}
}
