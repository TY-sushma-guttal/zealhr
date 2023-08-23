package com.te.zealhr.dto.account;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MarkAsPaidSalaryListDTO {
	private Long employeeSalaryId;
	private BigDecimal totalSalary;
}
