package com.te.zealhr.service.hr;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.zealhr.dto.hr.CompanyAddressListDTO;
import com.te.zealhr.entity.admin.CompanyBranchInfo;
import com.te.zealhr.repository.admin.CompanyBranchInfoRepository;

@Service
public class CompanyBranchInfoServiceImpl implements CompanyBranchInfoService {

	@Autowired
	private CompanyBranchInfoRepository repository;
	

	@Override
	public List<CompanyAddressListDTO> getCompanyAddressList(Long branchId) {

		Optional<CompanyBranchInfo> companyBranchInfo = repository.findById(branchId);

		List<CompanyAddressListDTO> companyAddressListDTO = new ArrayList<>();

		for (int i = 0; i < companyBranchInfo.get().getCompanyAddressInfoList().size(); i++) {
			companyAddressListDTO.add(new CompanyAddressListDTO(
					companyBranchInfo.get().getCompanyAddressInfoList().get(i).getCompanyAddressId(),
					companyBranchInfo.get().getCompanyAddressInfoList().get(i).getAddressDetails()));
		}
		
		return companyAddressListDTO;
	}

}
