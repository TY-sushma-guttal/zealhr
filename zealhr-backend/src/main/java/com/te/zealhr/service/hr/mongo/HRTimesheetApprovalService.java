package com.te.zealhr.service.hr.mongo;

import java.util.List;

import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.dto.admin.mongo.EmployeeTimeSheetDTO;

public interface HRTimesheetApprovalService {
	
	List<EmployeeTimeSheetDTO> getAllEmployeeTimesheetDetails(Long companyId, String status, Long employeeInfoId);
	
	EmployeeTimeSheetDTO getEmployeeTimesheetDetail(String timesheetObjectId, Long companyId);
	
	String updateEmployeeTimesheetDetails(Long companyId, String timesheetObjectId, String employeeId,
			AdminApprovedRejectDto adminApprovedRejectDto);

}
