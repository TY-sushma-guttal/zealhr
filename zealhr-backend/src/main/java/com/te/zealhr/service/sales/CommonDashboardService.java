package com.te.zealhr.service.sales;

import com.te.zealhr.dto.DepartmentProjectDTO;
import com.te.zealhr.dto.sales.EventBirthdayOtherDetailsDTO;

public interface CommonDashboardService {
	EventBirthdayOtherDetailsDTO getEventsBirthdayOtherDetails(Long companyId, Long employeeInfoId,DepartmentProjectDTO departmentProjectDTO);

	String sendWishes(Long companyId, Long employeeInfoIdFrom,Long employeeInfoIdTo);
}
