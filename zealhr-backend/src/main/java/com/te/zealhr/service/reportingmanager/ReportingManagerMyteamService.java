package com.te.zealhr.service.reportingmanager;

import java.util.List;

import com.te.zealhr.dto.reportingmanager.EmployeePersonalInfoDTO;
import com.te.zealhr.dto.reportingmanager.EmployeeTaskDetailsDTO;
import com.te.zealhr.dto.reportingmanager.ReportingmanagerMyTeamDTO;

public interface ReportingManagerMyteamService {

	List<ReportingmanagerMyTeamDTO> getEmployeeList(Long employeeInfoId,Long companyId);
	
	EmployeePersonalInfoDTO getEmployeeInfo(Long reportingManagerId,Long employeeInfoId,Long companyId);
		
	List<EmployeeTaskDetailsDTO> getEmployeeTaskList(Long reportingManagerId,Long employeeInfoId,Long companyId,String status);
}
