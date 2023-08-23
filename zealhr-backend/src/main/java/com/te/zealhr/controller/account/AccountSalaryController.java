package com.te.zealhr.controller.account;

import static com.te.zealhr.common.account.AccountConstants.EMPLOYEES_SALARY_DETAILS_FETCHED_SUCCESSFULLY;
import static com.te.zealhr.common.account.AccountConstants.EMPLOYEE_SALARY_DETAILS_FETCHED_SUCCESSFULLY;
import static com.te.zealhr.common.account.AccountConstants.EMPLOYEE_SALARY_DETAILS_NOT_FOUND;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.account.AccountSalaryDTO;
import com.te.zealhr.dto.account.AccountSalaryInputDTO;
import com.te.zealhr.dto.hr.EmployeeSalaryAllDetailsDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.account.AccountSalaryService;
import com.te.zealhr.audit.BaseConfigController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/account")
@RestController

/**
 * 
 * @author Ravindra
 *
 */
public class AccountSalaryController extends BaseConfigController {
	@Autowired
	AccountSalaryService service;
	
	@PostMapping("/salary")
	public ResponseEntity<SuccessResponse> salaryDetailsList(@RequestBody AccountSalaryInputDTO accountSalaryInputDTO) {
		List<AccountSalaryDTO> salaryDetailsList = service.salaryDetailsList(accountSalaryInputDTO);

		if (salaryDetailsList == null || salaryDetailsList.isEmpty()) {
			return new ResponseEntity<>(
					new SuccessResponse(false, EMPLOYEE_SALARY_DETAILS_NOT_FOUND, salaryDetailsList),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(
				new SuccessResponse(false, EMPLOYEES_SALARY_DETAILS_FETCHED_SUCCESSFULLY, salaryDetailsList),
				HttpStatus.OK);
	}

	@GetMapping("/salary-details")
	public ResponseEntity<SuccessResponse> salaryDetailsById(@RequestParam Long salaryId) {
		EmployeeSalaryAllDetailsDTO salaryDetailsById = service.salaryDetailsById(salaryId, getCompanyId());
		return new ResponseEntity<>(
				new SuccessResponse(false, EMPLOYEE_SALARY_DETAILS_FETCHED_SUCCESSFULLY, salaryDetailsById),
				HttpStatus.OK);
	}

}
