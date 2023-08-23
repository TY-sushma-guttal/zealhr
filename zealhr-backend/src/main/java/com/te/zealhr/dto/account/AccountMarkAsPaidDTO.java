package com.te.zealhr.dto.account;

import java.math.BigDecimal;

import lombok.Data;
@Data

public class AccountMarkAsPaidDTO {
	private Long advanceSalaryId;
	private BigDecimal amount;
}
