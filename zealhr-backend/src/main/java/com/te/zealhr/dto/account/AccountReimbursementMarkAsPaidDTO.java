package com.te.zealhr.dto.account;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class AccountReimbursementMarkAsPaidDTO {
	private Long reimbursementId;
	private BigDecimal amount;

}
