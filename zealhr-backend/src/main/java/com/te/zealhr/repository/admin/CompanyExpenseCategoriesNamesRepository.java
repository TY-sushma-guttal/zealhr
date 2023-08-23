package com.te.zealhr.repository.admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.te.zealhr.entity.admin.CompanyExpenseCategories;

@Repository
public interface CompanyExpenseCategoriesNamesRepository extends JpaRepository<CompanyExpenseCategories, Long>{
	
	List<CompanyExpenseCategories> findByCompanyInfoCompanyId(Long companyId);

	
}
