package com.te.zealhr.service.account;

import java.util.ArrayList;
import java.util.List;

import com.te.zealhr.dto.account.AccountSalaryDTO;
import com.te.zealhr.dto.account.AccountSalaryInputDTO;
import com.te.zealhr.dto.hr.EmployeeSalaryAllDetailsDTO;

public interface AccountSalaryService {

	List<AccountSalaryDTO> salaryDetailsList(AccountSalaryInputDTO accountSalaryInputDTO);

	EmployeeSalaryAllDetailsDTO salaryDetailsById(Long salaryId, Long companyId);

}
