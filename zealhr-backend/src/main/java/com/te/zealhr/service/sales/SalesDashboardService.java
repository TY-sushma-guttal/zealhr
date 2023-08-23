package com.te.zealhr.service.sales;

import java.util.List;

import com.te.zealhr.dto.DashboardRequestDTO;
import com.te.zealhr.dto.DashboardResponseDTO;
import com.te.zealhr.dto.sales.AllCompanyClientInfoResponseDTO;

public interface SalesDashboardService {
	
	DashboardResponseDTO getClientDetails(DashboardRequestDTO dashboardRequestDTO, Long companyId);
	
	List<AllCompanyClientInfoResponseDTO> getClientDetailsByStatus(Long companyId, String type);

}
