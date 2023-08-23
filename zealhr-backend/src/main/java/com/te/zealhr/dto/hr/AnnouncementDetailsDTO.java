package com.te.zealhr.dto.hr;

import java.util.List;

import lombok.Data;

@Data
public class AnnouncementDetailsDTO {

	private Long announcementId;

	private String discription;

	private String type;

	private List<String> employeeIdList;

	private Long employeePersonalInfoId;
	
	private List<EmployeeInformationDTO> selectedEmployees;
	
	private EmployeeInformationDTO relatedEmployee;

}
