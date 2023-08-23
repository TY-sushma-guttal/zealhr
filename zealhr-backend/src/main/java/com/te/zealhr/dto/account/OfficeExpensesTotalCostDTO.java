package com.te.zealhr.dto.account;

import lombok.Data;

@Data
public class OfficeExpensesTotalCostDTO {
	
	private Double totalCost;
	private String type;
	private Long expenseCategoryId;
	
}
