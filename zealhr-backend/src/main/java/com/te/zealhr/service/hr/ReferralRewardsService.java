package com.te.zealhr.service.hr;

import java.util.List;

import com.te.zealhr.dto.hr.ReferralRewardsListDTO;

public interface ReferralRewardsService {

	//Api for displaying referralRewardseligibleEmployees
	public List<ReferralRewardsListDTO> referralRewardseligibleemployee(Long companyId);
	//Api for displaying reference employee based on reference id
	public ReferralRewardsListDTO referalRewardforEmployee(Long referenceId);
}
