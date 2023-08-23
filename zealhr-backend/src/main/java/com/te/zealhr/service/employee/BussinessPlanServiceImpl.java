package com.te.zealhr.service.employee;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.te.zealhr.dto.admin.BussinessPlanDTO;
import com.te.zealhr.dto.admin.PlanDTO;
import com.te.zealhr.exception.DataNotFoundException;
import com.te.zealhr.repository.admin.CompanyInfoRepository;
import com.te.zealhr.util.CacheStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BussinessPlanServiceImpl implements BussinessPlanService {

	private final CacheStore<BussinessPlanDTO> cacheStore;

	private final CacheStore<PlanDTO> cacheStorePlanDTO;

	private final CompanyInfoRepository companyInfoRepository;

	@Override
	public String addBussinessPlan(BussinessPlanDTO bussinessPlanDTO, String terminalId, Long comapnyId) {
		return Optional
				.ofNullable(companyInfoRepository
						.findByCompanyNameAndGstinNotNull(bussinessPlanDTO.getCompanyName()) == null)
				.filter(x -> x).map(com -> {
					if (cacheStore.get(terminalId) != null)
						cacheStore.invalidate(terminalId);
					bussinessPlanDTO.setCompanyId(comapnyId);
					cacheStore.add(terminalId, bussinessPlanDTO);
					return "Bussiness Plan Added";
				}).orElseThrow(() -> new DataNotFoundException("Company Already Exist Please Contact Super Admin"));
	}

	@Override
	public String addUserPlan(BussinessPlanDTO bussinessPlanDTO, String terminalId) {
		if (cacheStore.get(terminalId) != null)
			cacheStore.invalidate(terminalId);
		return terminalId;
	}

	@Override
	public BussinessPlanDTO bussinessRegistration(String terminalId) {
		return Optional.ofNullable(cacheStore.get(terminalId) == null).filter(x -> !x)
				.map(y -> cacheStore.get(terminalId)).orElseGet(BussinessPlanDTO::new);
	}

	@Override
	public String planDTO(String terminalId, PlanDTO planDTO) {
		if (cacheStorePlanDTO.get(terminalId) != null)
			cacheStorePlanDTO.invalidate(terminalId);
		if (cacheStore.get(terminalId) != null)
			cacheStore.invalidate(terminalId);
		cacheStorePlanDTO.add(terminalId, planDTO);
		return "Plan Added";
	}

}
