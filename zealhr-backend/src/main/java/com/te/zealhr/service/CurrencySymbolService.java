package com.te.zealhr.service;

import javax.servlet.http.HttpServletRequest;

import com.te.zealhr.dto.account.CurrencyConvertDTO;

public interface CurrencySymbolService {
	
	CurrencyConvertDTO getSymbol(HttpServletRequest request);

}
