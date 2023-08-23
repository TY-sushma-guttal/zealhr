package com.te.zealhr.service.helpandsupport.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

import com.te.zealhr.dto.admin.TicketCategoryDTO;
import com.te.zealhr.dto.helpandsupport.mongo.CompanyTicketDto;
import com.te.zealhr.dto.helpandsupport.mongo.ITProductNameDTO;
import com.te.zealhr.dto.helpandsupport.mongo.ProductNameDTO;
import com.te.zealhr.dto.helpandsupport.mongo.RaiseTicketDto;
import com.te.zealhr.dto.helpandsupport.mongo.ReportingManagerDto;
import com.te.zealhr.entity.it.CompanyHardwareItems;

public interface RaiseTicketService {


	public boolean createTickets(Long companyId, Long employeeInfoId, List<MultipartFile> files,
			RaiseTicketDto raiseTicketDto);

	public List<ReportingManagerDto> getAllReportingManagaer(Long companyId,String department);

	public ITProductNameDTO getProducts(Long companyId);

	public List<CompanyTicketDto> getDelayedTickets(Long companyId, CompanyTicketDto companyTicketDto);

	public List<CompanyTicketDto> getAllTickets(Long companyId, CompanyTicketDto companyTicketDto);

	public CompanyTicketDto getTicketsDetails(Long companyId, String id, String department);
	
	List<TicketCategoryDTO> getDepartmentCategories(Long companyId);

}
