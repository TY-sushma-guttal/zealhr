package com.te.zealhr.service.admin.mongo;

import java.util.List;

import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.dto.admin.mongo.EmployeeTimeSheetDTO;

public interface AdminTimesheetDetailsService {

	List<EmployeeTimeSheetDTO> getAllEmployeeTimesheetDetails(Long companyId, String status);

	EmployeeTimeSheetDTO getEmployeeTimesheetDetails(String timesheetObjectId, Long companyId);

	String updateEmployeeTimesheetDetails(Long companyId, String timesheetObjectId, String employeeId,
			AdminApprovedRejectDto adminApprovedRejectDto);
}
