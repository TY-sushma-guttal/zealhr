/**
 * 
 */
package com.te.zealhr.service.employee;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.te.zealhr.dto.employee.EmployeeIdDto;
import com.te.zealhr.dto.employee.EmployeeLoginDto;
import com.te.zealhr.dto.employee.EmployeeLoginResponseDto;
import com.te.zealhr.dto.employee.VerifyOTPDto;

/**
 * @author Sahid
 *
 */
public interface EmployeeLoginService {
	
	String resendOTP(EmployeeIdDto employeeIdDto);
	
	String sendOTP(EmployeeIdDto employeeIdDto);
	
	String punchedIn(EmployeeLoginDto employeeLoginDto, HttpServletRequest request, String type);
	
	String punchedOut(EmployeeLoginDto employeeLoginDto, HttpServletRequest request, String type);
	
	String validateOTP(VerifyOTPDto verifyOTPDto );
	
	EmployeeLoginResponseDto validateLoginOTP(VerifyOTPDto verifyOTPDto,HttpServletRequest request, HttpServletResponse response);
	
}
