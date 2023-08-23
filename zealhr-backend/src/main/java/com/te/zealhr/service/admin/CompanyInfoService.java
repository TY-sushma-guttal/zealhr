package com.te.zealhr.service.admin;

/**
 * 
 * @author Brunda
 *
 */

import org.springframework.web.multipart.MultipartFile;

import com.te.zealhr.dto.admin.CompanyInfoDTO;


public interface CompanyInfoService {

	CompanyInfoDTO getCompanyInfoDetails(Long companyId) ;

	String updateCompanyInfo(CompanyInfoDTO companyInfoDto, MultipartFile companylogo);
}
