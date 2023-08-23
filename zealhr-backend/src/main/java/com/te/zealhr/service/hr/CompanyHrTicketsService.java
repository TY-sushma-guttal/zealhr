package com.te.zealhr.service.hr;

import java.util.List;

import com.te.zealhr.dto.hr.UpdateTicketDTO;
import com.te.zealhr.dto.hr.mongo.CompanyHrTicketsDTO;

public interface CompanyHrTicketsService {
	
	public List<CompanyHrTicketsDTO> hrTicketsInfoList(Long companyId);
	
	CompanyHrTicketsDTO hrTicketsDTOInfo(String hrTicketObjectId, Long employeeInfoId);
	
	CompanyHrTicketsDTO updateTicketHistory(UpdateTicketDTO updateTicketDTO, Long employeeInfoId);
	
}
