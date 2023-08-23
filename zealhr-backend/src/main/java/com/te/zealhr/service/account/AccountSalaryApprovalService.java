package com.te.zealhr.service.account;

import java.util.ArrayList;
import java.util.List;

import com.te.zealhr.dto.account.AccountSalaryDTO;
import com.te.zealhr.dto.account.AccountSalaryInputDTO;
import com.te.zealhr.dto.account.MarkAsPaidInputDTO;
import com.te.zealhr.dto.account.MarkAsPaidSalaryListDTO;

public interface AccountSalaryApprovalService {

	List<MarkAsPaidSalaryListDTO> finalizeSalary(MarkAsPaidInputDTO finalizesalaryInputDTO, Long companyId);

	List<AccountSalaryDTO> salaryApproval(AccountSalaryInputDTO accountSalaryInputDTO);

}
