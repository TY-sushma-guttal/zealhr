package com.te.zealhr.service.employee;

import java.util.List;
import java.util.Map;

import com.te.zealhr.dto.DashboardRequestDTO;

public interface HRLeaveService {
	
	List<Map<String, String>> getLeaveDetails(DashboardRequestDTO dashboardRequestDTO, Long companyId);

}
