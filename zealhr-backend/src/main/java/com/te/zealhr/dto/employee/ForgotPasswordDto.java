package com.te.zealhr.dto.employee;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_DEFAULT)
public class ForgotPasswordDto implements Serializable {
	private String employeeId;
	private String newPassword;
	private String confirmPassword;
}
