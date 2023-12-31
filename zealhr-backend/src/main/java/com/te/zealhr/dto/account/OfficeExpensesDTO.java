package com.te.zealhr.dto.account;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class OfficeExpensesDTO {

	private Long reimbursementId;
	private String expenseDate;
	private String description;
	private BigDecimal amount;
	private String status;
	private String expenseCategoryName;
	private String attachmentUrl;
	private Long expenseCategoryId;
	private Long employeeInfoId;
	private String employeeFullName;

}
