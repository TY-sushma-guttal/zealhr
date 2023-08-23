package com.te.zealhr.dto.employee;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeTeamDTO {

	private Long employeeInfoId;

	private String designation;

	private String name;
	
	private String pictureURL;

	private List<EmployeeTeamDTO> managerOf;

	public EmployeeTeamDTO(Long employeeInfoId, String designation, String name, String pictureURL) {
		super();
		this.employeeInfoId = employeeInfoId;
		this.designation = designation;
		this.name = name;
		this.pictureURL = pictureURL;
	}

}
