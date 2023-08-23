package com.te.zealhr.service.hr;

import java.util.List;

import com.te.zealhr.dto.hr.CompanyAddressListDTO;

public interface CompanyBranchInfoService {

	List<CompanyAddressListDTO> getCompanyAddressList(Long branchId);
}
