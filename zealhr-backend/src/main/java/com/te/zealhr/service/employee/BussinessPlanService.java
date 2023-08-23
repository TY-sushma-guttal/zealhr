package com.te.zealhr.service.employee;

import com.te.zealhr.dto.admin.BussinessPlanDTO;
import com.te.zealhr.dto.admin.PlanDTO;

public interface BussinessPlanService {

	public String addBussinessPlan(BussinessPlanDTO bussinessPlanDTO, String terminalId, Long comapnyId);

	public String addUserPlan(BussinessPlanDTO bussinessPlanDTO, String terminalId);

	public BussinessPlanDTO bussinessRegistration(String terminalId);

	public String planDTO(String terminalId,PlanDTO planDTO);
}
