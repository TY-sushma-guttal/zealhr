package com.te.zealhr.service.account;

import java.util.List;

import com.te.zealhr.dto.DashboardRequestDTO;
import com.te.zealhr.dto.DashboardResponseDTO;
import com.te.zealhr.dto.account.AccountPaySlipListDTO;
import com.te.zealhr.dto.account.PurchasedOrderDisplayDTO;
import com.te.zealhr.dto.account.VendorBasicDetailsDTO;
import com.te.zealhr.dto.hr.mongo.HRTicketsBasicDTO;

public interface AccountDashboardService {
	
	DashboardResponseDTO getTicketDetails(DashboardRequestDTO dashboardRequestDTO, Long companyId);
	
	DashboardResponseDTO getSalesAndPurchaseDetails(DashboardRequestDTO dashboardRequestDTO, Long companyId);
	
	DashboardResponseDTO getVendorDetails(DashboardRequestDTO dashboardRequestDTO, Long companyId);
	
	DashboardResponseDTO getSalaryDetails(DashboardRequestDTO dashboardRequestDTO, Long companyId);
	
	List<HRTicketsBasicDTO> getTicketDetailsByStatus(Long companyId, String type, String filterValue);
	
	List<VendorBasicDetailsDTO> getVendorDetailsByStatus(Long companyId, String type);
	
	List<AccountPaySlipListDTO> getSalaryDetailsByStatus(Long companyId,
			DashboardRequestDTO dashboardRequestDTO);
	
	List<PurchasedOrderDisplayDTO> getAllPurchaseOrder(Long companyId);

}
