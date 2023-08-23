package com.te.zealhr.service.admin;

import java.util.Set;

import com.te.zealhr.dto.admin.EmployeeNotificationReferralRewardDto;

public interface AdminNotificationReferralRewardService {
	
	Set<EmployeeNotificationReferralRewardDto> getAllEmployeeNotificationReferralReward(Long companyId);
	
	EmployeeNotificationReferralRewardDto getEmployeeNotificationReferralReward(Long companyId,Long referenceId);
	
	String updateEmployeeNotificationReferralReward(Long companyId,EmployeeNotificationReferralRewardDto employeeNotificationReferralRewardDto );

}
