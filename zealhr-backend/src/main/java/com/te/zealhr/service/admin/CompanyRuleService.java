package com.te.zealhr.service.admin;

import com.te.zealhr.dto.admin.CompanyRulesDto;

public interface CompanyRuleService {

	CompanyRulesDto getCompanyRules(Long companyId);

	String updateCompanyRule(Long companyId, CompanyRulesDto companyRulesDto);

}
