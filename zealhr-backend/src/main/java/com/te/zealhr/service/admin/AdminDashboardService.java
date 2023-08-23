package com.te.zealhr.service.admin;

import java.util.List;

import com.te.zealhr.dto.DashboardRequestDTO;
import com.te.zealhr.dto.DashboardResponseDTO;
import com.te.zealhr.dto.TicketDetailsDTO;
import com.te.zealhr.dto.helpandsupport.mongo.CompanyTicketDto;
import com.te.zealhr.dto.hr.EmployeeDisplayDetailsDTO;
import com.te.zealhr.dto.hr.mongo.HRTicketsBasicDTO;

public interface AdminDashboardService {
	
	DashboardResponseDTO getTicketDetails(DashboardRequestDTO dashboardRequestDTO, Long companyId);
	
	DashboardResponseDTO getSalaryDetails(DashboardRequestDTO dashboardRequestDTO, Long companyId);
	
	List<HRTicketsBasicDTO> getTicketDetailsByStatus(DashboardRequestDTO dashboardRequestDTO, Long companyId);
	
	TicketDetailsDTO getTicketDetailsById(DashboardRequestDTO dashboardRequestDTO, String objectTicketId,
			Long companyId);

	public CompanyTicketDto getQuestionAndAnswer(CompanyTicketDto updateTicketDTO, Long companyId);

	public CompanyTicketDto updateTicketRemarks(CompanyTicketDto updateTicketDTO, Long companyId);
	
	DashboardResponseDTO getAttendanceDetails(DashboardRequestDTO dashboardRequestDTO, Long companyId);
	
	List<EmployeeDisplayDetailsDTO> getEmployeeDetailsByStatus(Long companyId, String type);

}
