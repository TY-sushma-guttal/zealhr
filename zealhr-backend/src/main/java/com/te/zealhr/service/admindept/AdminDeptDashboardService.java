package com.te.zealhr.service.admindept;

import java.util.List;

import com.te.zealhr.dto.DashboardRequestDTO;
import com.te.zealhr.dto.DashboardResponseDTO;
import com.te.zealhr.dto.admindept.GetOtherStockGroupItemDto;
import com.te.zealhr.dto.hr.mongo.HRTicketsBasicDTO;

public interface AdminDeptDashboardService {
	
	DashboardResponseDTO getTicketDetails(DashboardRequestDTO dashboardRequestDTO, Long companyId);
	
	DashboardResponseDTO getStockGroupDetails(DashboardRequestDTO dashboardRequestDTO, Long companyId);
	
	List<HRTicketsBasicDTO> getTicketDetailsByStatus(Long companyId, String type, String filterValue);
	
	List<GetOtherStockGroupItemDto> getStockItemDetailsByStatus(Long companyId, String type);

}
