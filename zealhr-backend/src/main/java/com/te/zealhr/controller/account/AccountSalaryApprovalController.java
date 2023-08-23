package com.te.zealhr.controller.account;


import static com.te.zealhr.common.account.AccountConstants.EMPLOYEES_SALARY_DETAILS_FETCHED_SUCCESSFULLY;
import static com.te.zealhr.common.account.AccountConstants.EMPLOYEE_SALARY_DETAILS_NOT_FOUND;
import static com.te.zealhr.common.account.AccountConstants.EMPLOYEE_SALARY_FINALIZE_SUCCESSFULLY;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.account.AccountSalaryDTO;
import com.te.zealhr.dto.account.AccountSalaryInputDTO;
import com.te.zealhr.dto.account.MarkAsPaidInputDTO;
import com.te.zealhr.dto.account.MarkAsPaidSalaryListDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.account.AccountSalaryApprovalService;
import com.te.zealhr.audit.BaseConfigController;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")
@Slf4j
@RequestMapping(path = "/api/v1/account")
@RestController

/**
 * 
 * @author Ravindra
 *
 */
public class AccountSalaryApprovalController extends BaseConfigController{
	@Autowired 
	AccountSalaryApprovalService service;
	
	
	@PostMapping("/finalize-salary")
	public ResponseEntity<SuccessResponse> finalizeSalary(@RequestBody MarkAsPaidInputDTO finalizesalaryInputDTO){
		log.info("finalizeSalary method execution started");
		List<MarkAsPaidSalaryListDTO> finalizeSalary = service.finalizeSalary(finalizesalaryInputDTO,getCompanyId());
		return new ResponseEntity<>(
				new SuccessResponse(false, EMPLOYEE_SALARY_FINALIZE_SUCCESSFULLY, finalizeSalary),
					HttpStatus.OK);
	}
   @PostMapping("/salary-approval")
   public ResponseEntity<SuccessResponse> salaryApproval(@RequestBody AccountSalaryInputDTO accountSalaryInputDTO){
		log.info("salaryApproval method execution started");
	   List<AccountSalaryDTO> salaryApproval = service.salaryApproval(accountSalaryInputDTO);
		if (salaryApproval == null || salaryApproval.isEmpty()) {
			return new ResponseEntity<>(
					new SuccessResponse(false, EMPLOYEE_SALARY_DETAILS_NOT_FOUND, salaryApproval),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(
				new SuccessResponse(false, EMPLOYEES_SALARY_DETAILS_FETCHED_SUCCESSFULLY, salaryApproval),
				HttpStatus.OK);
   }
}
