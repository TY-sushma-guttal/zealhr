package com.te.zealhr.service.employee;

import java.util.List;

import com.te.zealhr.dto.hr.CanlenderRequestDTO;
import com.te.zealhr.dto.hr.mongo.HRTicketsBasicDTO;

public interface RaisedTicketService {

	List<HRTicketsBasicDTO> getAllRaisedTickets(CanlenderRequestDTO canlenderRequestDTO, Long userId, Long companyId);
	
	List<HRTicketsBasicDTO> getTeamPendingTickets(Long employeeInfoId);

}
