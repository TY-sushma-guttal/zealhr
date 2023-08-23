package com.te.zealhr.service.admin;

import java.util.List;

import com.te.zealhr.dto.admin.TermsAndConditionDTO;

public interface TermsAndConditionService {
	
	TermsAndConditionDTO addTermsAndCondition(TermsAndConditionDTO termsAndConditionDTO, Long companyId);
	
	List<TermsAndConditionDTO> getAllTermsAndConditions(Long companyId);

}
