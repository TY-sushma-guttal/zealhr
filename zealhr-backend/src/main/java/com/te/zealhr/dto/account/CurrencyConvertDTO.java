package com.te.zealhr.dto.account;

import com.te.zealhr.entity.account.mongo.CurrencyDetails;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyConvertDTO {
	
	private String base;
	
	private CurrencyDetails currencyDetails;
	
}
