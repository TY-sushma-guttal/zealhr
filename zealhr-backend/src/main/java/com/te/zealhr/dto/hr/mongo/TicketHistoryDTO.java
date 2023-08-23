package com.te.zealhr.dto.hr.mongo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.te.zealhr.dto.hr.EmployeeInformationDTO;
import com.te.zealhr.dto.hr.EventManagementDepartmentNameDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketHistoryDTO {
	private String status;
	private LocalDate date;
	private EmployeeInformationDTO by;
	private String pictureURL;
	private EventManagementDepartmentNameDTO department;
}
