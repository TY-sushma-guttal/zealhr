package com.te.zealhr.service.reportingmanager.mongo;

import java.util.List;

import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.dto.admin.mongo.EmployeeTimeSheetDTO;
import com.te.zealhr.dto.reportingmanager.EmployeeTimesheetDetailsApprovalDTO;

public interface ReportingManagerTimesheetApprovalService {
	
	List<EmployeeTimeSheetDTO> getEmployeeTimesheetList( Long companyId,String status,Long employeeInfoId);

	EmployeeTimesheetDetailsApprovalDTO getEmployeetimesheetDetails(String timesheetObjectId, Long companyId);
	
	String addResponseToTimesheet(String timesheetObjectId,Long companyId,String employeeId , AdminApprovedRejectDto adminApprovedRejectDto);

}
