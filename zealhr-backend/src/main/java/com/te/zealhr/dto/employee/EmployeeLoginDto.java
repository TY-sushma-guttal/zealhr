package com.te.zealhr.dto.employee;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@JsonInclude(value = Include.NON_DEFAULT)
public class EmployeeLoginDto implements Serializable {

	private Long loginId;
	
	private String companyCode;
	
	private String emailId;
	
	private Object roles;
	
	private Double latitude; 	
	private Double longitude;
	
	private List<EmployeeCapabilityDTO> role;
	
	private EmployeePersonalInfoDto employeePersonalInfo;
	
	private String deviceId;

	@Override
	public String toString() {
		return "EmployeeLoginDto [loginId=" + loginId + ", employeeId=" + emailId + ", roles=" + roles + "]";
	}
	
}
