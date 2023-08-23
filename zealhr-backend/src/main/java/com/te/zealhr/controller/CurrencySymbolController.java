package com.te.zealhr.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.CurrencySymbolService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/symbol")
@RestController
public class CurrencySymbolController {

	private final CurrencySymbolService currencySymbolService;

	@PostMapping
	public ResponseEntity<SuccessResponse> getSymbol(HttpServletRequest request) {
		log.info("Fetch Currency Symbol In Controller ");
		return new ResponseEntity<>(new SuccessResponse(false, "Fetch Currency Symbol Successfully",
				currencySymbolService.getSymbol(request)), HttpStatus.OK);
	}

}
