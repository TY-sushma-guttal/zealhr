package com.te.zealhr.service.admin;

import java.util.List;

import com.te.zealhr.dto.admin.CompanyExpenseCategoriesDTO;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

public interface CompanyExpenseCategoriesService {

	List<CompanyExpenseCategoriesDTO> getExpense(CompanyExpenseCategoriesDTO companyExpenseCategoriesDto,
			Long companyId);

	String updateExpense(List<CompanyExpenseCategoriesDTO> companyExpenseCategoriesDto,
			Long companyId);

}
