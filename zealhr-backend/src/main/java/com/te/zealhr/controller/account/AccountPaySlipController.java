package com.te.zealhr.controller.account;

import static com.te.zealhr.common.account.AccountConstants.EMPLOYEES_SALARY_DETAILS_FETCHED_SUCCESSFULLY;
import static com.te.zealhr.common.hr.HrConstants.SALARY_RECORDS_NOT_FOUND;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.account.AccountPaySlipInputDTO;
import com.te.zealhr.dto.account.AccountPaySlipListDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.account.AccountPaySlipService;
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
public class AccountPaySlipController extends BaseConfigController {
	@Autowired
	AccountPaySlipService service;

	@PostMapping("/pay-slip")
	public ResponseEntity<SuccessResponse> paySlip(@RequestBody AccountPaySlipInputDTO accountPaySlipInputDTO) {
		List<AccountPaySlipListDTO> paySlip = service.paySlip(accountPaySlipInputDTO);
		if (paySlip == null || paySlip.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, SALARY_RECORDS_NOT_FOUND, paySlip),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new SuccessResponse(false, EMPLOYEES_SALARY_DETAILS_FETCHED_SUCCESSFULLY, paySlip),
				HttpStatus.OK);
	}

}
