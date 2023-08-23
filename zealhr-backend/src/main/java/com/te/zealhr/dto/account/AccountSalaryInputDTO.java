package com.te.zealhr.dto.account;

import java.util.List;

import lombok.Data;
@Data
public class AccountSalaryInputDTO {
	private List<Integer> month;
	private List<String> department;
	private Long companyId;

}
