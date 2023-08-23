package com.te.zealhr.auth.dto;

import static com.te.zealhr.common.employee.EmployeeRegistrationConstants.EMPLOYEE_ID_CAN_NOT_BE_NULL_OR_BLANK;
import static com.te.zealhr.common.employee.EmployeeRegistrationConstants.PASSWORD_CAN_NOT_BE_NULL_OR_BLANK;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.te.zealhr.dto.employee.EmployeeCapabilityDTO;
import com.te.zealhr.dto.employee.EmployeePersonalInfoDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class AuthenticationRequest {

	private String token;

	private Long loginId;

	private String companyCode;

	@NotBlank(message = EMPLOYEE_ID_CAN_NOT_BE_NULL_OR_BLANK)
	private String employeeId;

	@NotBlank(message = PASSWORD_CAN_NOT_BE_NULL_OR_BLANK)
	private String password;

	private String oldPassword;

	private String currentPassword;

	private Object roles;

	private Double latitude;
	private Double longitude;

	private List<EmployeeCapabilityDTO> role;

	private EmployeePersonalInfoDto employeePersonalInfo;

	
}
