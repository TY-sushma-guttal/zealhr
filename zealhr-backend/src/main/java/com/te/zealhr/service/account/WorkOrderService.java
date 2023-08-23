package com.te.zealhr.service.account;

import java.util.List;

import com.te.zealhr.dto.account.CreatWorkOrderDealDropdownDto;
import com.te.zealhr.dto.account.WorkOrderDTO;
import com.te.zealhr.dto.account.WorkOrderListDto;

public interface WorkOrderService {

	List<WorkOrderListDto> getWorkOrderList(Long companyId);

	List<CreatWorkOrderDealDropdownDto> getCompanyDeals(Long companyId);
	
	public WorkOrderDTO getWorkOrderDetails(Long companyId,Long workOrderId,Long employeeInfoId);
}
