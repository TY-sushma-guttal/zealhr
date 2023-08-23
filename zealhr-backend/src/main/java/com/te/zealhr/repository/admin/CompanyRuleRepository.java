package com.te.zealhr.repository.admin;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author Brunda
 *
 */
import org.springframework.data.jpa.repository.JpaRepository;

import com.te.zealhr.entity.admin.CompanyRuleInfo;

public interface CompanyRuleRepository extends JpaRepository<CompanyRuleInfo,Long> {

	Optional<List<CompanyRuleInfo>> findByCompanyInfoCompanyId(Long companyId);
	
	Optional<List<CompanyRuleInfo>> findByCompanyInfoIsActiveTrue();

//	List<CompanyRuleInfo> findByCompanyInfoCompanyId(Long companyId);
}
