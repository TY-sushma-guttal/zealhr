package com.te.zealhr.dto.employee.mongo;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class EmployeeCalendarDTO {
	
	private List<LocalDate> weekOffDetails;
	
	private List<LocalDate> leaveDetails;
	
	private List<EmployeeTimesheetDetailsDTO> timesheetDetails;
	
	private List<LocalDate> holidayDetails;

}
