package com.te.zealhr.service.account;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.te.zealhr.dto.account.OfficeExpensesDTO;
import com.te.zealhr.dto.account.OfficeExpensesTotalCostDTO;

public interface OfficeExpenesesService {
	
	List<OfficeExpensesTotalCostDTO> getOfficeExpenseDetails(Long companyId);

	OfficeExpensesDTO addOfficeExpenses(OfficeExpensesDTO addOfficeExpensesDTO, MultipartFile multipartFile,Long companyId);

	OfficeExpensesDTO getReimbursementById(Long reimbursementId);
	
	List<OfficeExpensesDTO> getReimbursementByCategory(Long expenseCategoryId, Long companyId);
	
	OfficeExpensesDTO updateOfficeExpenses(Long reimbursementId, String status);
	
}
