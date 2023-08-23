package com.te.zealhr.service.employee;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.te.zealhr.dto.admin.CompanyDesignationNamesDto;
import com.te.zealhr.dto.admin.CompanyInfoNamesDto;
import com.te.zealhr.dto.admin.PlanDTO;
import com.te.zealhr.dto.employee.EmployeeIdDto;
import com.te.zealhr.dto.employee.NewConfirmPasswordDto;
import com.te.zealhr.dto.employee.Registration;
import com.te.zealhr.dto.employee.VerifyOTPDto;
import com.te.zealhr.dto.superadmin.PlanDetailsDTO;

public interface EmployeeRegistrationService {
	
	List<CompanyInfoNamesDto> getAllCompany();
	
	List<CompanyDesignationNamesDto> getAllDesignation(Long companyId);

	String varifyEmployee(Registration employeeRegistrationDto,Long companyId,String terminalId);
	 
	String validateOTP(VerifyOTPDto verifyOTPDto);
	
	PlanDTO registration(NewConfirmPasswordDto newConfirmPasswordDto, String terminalId);
	
	String resendOTP(EmployeeIdDto employeeIdDto);
	
	List<PlanDetailsDTO> getAllPlanDetails(HttpServletRequest request,String terminalId);
}
