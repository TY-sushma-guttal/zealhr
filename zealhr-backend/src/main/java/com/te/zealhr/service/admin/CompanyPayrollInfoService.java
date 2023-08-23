package com.te.zealhr.service.admin;

import com.te.zealhr.dto.admin.CompanyPayrollInfoDTO;
import com.te.zealhr.dto.admin.CompanyPayrollInfoResponseDTO;

//@author Rakesh Kumar Nayak
public interface CompanyPayrollInfoService  {

	public Boolean createPayrollInfo(CompanyPayrollInfoDTO companyPayrollInfoDto,Long companyId);
	
	public CompanyPayrollInfoResponseDTO getAllCompanyPayrollInfo(Long companyId);
	
	public Boolean updateCompanyPayrollInfo(CompanyPayrollInfoDTO companyPayrollInfoDto,Long companyId);
}
