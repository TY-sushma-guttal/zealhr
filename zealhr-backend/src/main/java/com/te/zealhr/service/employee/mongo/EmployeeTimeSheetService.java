package com.te.zealhr.service.employee.mongo;

import java.util.List;

import com.te.zealhr.dto.employee.mongo.EmployeeCalendarDTO;
import com.te.zealhr.dto.employee.mongo.EmployeeProjectTaskDetailsDTO;
import com.te.zealhr.dto.employee.mongo.EmployeeTaskListDTO;
import com.te.zealhr.dto.employee.mongo.EmployeeTimesheetConfigurationDTO;
import com.te.zealhr.dto.employee.mongo.EmployeeTimesheetDetailsDTO;
import com.te.zealhr.entity.project.mongo.ProjectTaskDetails;

public interface EmployeeTimeSheetService {

	List<EmployeeProjectTaskDetailsDTO> getProjectList(Long employeeInfoId, Long companyId);

	ProjectTaskDetails saveProjectTaskDetails(ProjectTaskDetails projectTaskDetails);

	List<EmployeeTaskListDTO> getTaskList(Long employeeInfoId, List<Long> projectIdList, Long companyId);

	EmployeeTimesheetDetailsDTO saveEmployeeTimesheetDetails(EmployeeTimesheetDetailsDTO employeeTimesheetDetailsDTO,
			Long employeeInfoId, Long companyId);

	EmployeeCalendarDTO getTimesheetDetail(EmployeeTimesheetConfigurationDTO employeeTimesheetConfigurationDTO);

	String deleteEmployeeTimeSheet(Long employeeInfoId, String timesheetObjectId, Long companyId);
}
