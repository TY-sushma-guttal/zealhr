package com.te.zealhr.service.admin;

import java.util.List;

import com.te.zealhr.dto.admin.CompanyLeadCategoriesDTO;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

public interface CompanyLeadCategoriesService {


	List<CompanyLeadCategoriesDTO> getLead(Long companyId);

	String updateLead(List<CompanyLeadCategoriesDTO> companyLeadCategoriesDtoList, Long companyId);


}
