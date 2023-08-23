package com.te.zealhr.controller.employee;

import static com.te.zealhr.common.employee.EmployeeLoginConstants.INVALID_OTP;
import static com.te.zealhr.common.employee.EmployeeLoginConstants.VALID_OTP;
import static org.springframework.http.HttpStatus.OK;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.employee.EmployeeIdDto;
import com.te.zealhr.dto.employee.EmployeeLoginDto;
import com.te.zealhr.dto.employee.EmployeeLoginResponseDto;
import com.te.zealhr.dto.employee.VerifyOTPDto;
import com.te.zealhr.response.employee.EmployeeLoginResponse;
import com.te.zealhr.service.employee.EmployeeLoginService;
import com.te.zealhr.audit.BaseConfigController;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/employee")
@RequiredArgsConstructor
public class EmployeeLoginController extends BaseConfigController {

	private final EmployeeLoginService employeeLoginService;

	@PostMapping("punchin/{type}")
	public ResponseEntity<EmployeeLoginResponse> punchedIn(@RequestBody EmployeeLoginDto employeeLoginDto,
			HttpServletRequest request, @PathVariable String type) {
		return ResponseEntity.status(OK).body(EmployeeLoginResponse.builder().error(Boolean.FALSE)
				.message(employeeLoginService.punchedIn(employeeLoginDto, request, type)).build());
	}

	@PostMapping("punchout/{type}")
	public ResponseEntity<EmployeeLoginResponse> punchedOut(@RequestBody EmployeeLoginDto employeeLoginDto,
			HttpServletRequest request, @PathVariable String type) {
		return ResponseEntity.status(OK).body(EmployeeLoginResponse.builder().error(Boolean.FALSE)
				.message(employeeLoginService.punchedOut(employeeLoginDto, request, type)).build());
	}

	@PostMapping("send-otp")
	public ResponseEntity<EmployeeLoginResponse> sendOTP(@Valid @RequestBody EmployeeIdDto employeeIdDto) {
		return ResponseEntity.status(HttpStatus.OK).body(EmployeeLoginResponse.builder().error(Boolean.FALSE)
				.message(employeeLoginService.sendOTP(employeeIdDto)).build());
	}

	@PostMapping("resend-otp")
	public ResponseEntity<EmployeeLoginResponse> resendOTP(@Valid @RequestBody EmployeeIdDto employeeIdDto) {
		return ResponseEntity.status(HttpStatus.OK).body(EmployeeLoginResponse.builder().error(Boolean.FALSE)
				.message(employeeLoginService.resendOTP(employeeIdDto)).build());
	}

	@PostMapping("varify-login-otp")
	public ResponseEntity<EmployeeLoginResponse> validateLoginOTP(@Valid @RequestBody VerifyOTPDto verifyOTPDto,
			HttpServletRequest request, HttpServletResponse response) {
		EmployeeLoginResponseDto employeeLoginResponseDto = employeeLoginService.validateLoginOTP(verifyOTPDto, request,
				response);
		return ResponseEntity.status(OK).body(EmployeeLoginResponse.builder().error(Boolean.FALSE).message(VALID_OTP)
				.data(employeeLoginResponseDto).build());
	}

	@PostMapping("varify-otp")
	public ResponseEntity<EmployeeLoginResponse> validateOTP(@Valid @RequestBody VerifyOTPDto verifyOTPDto) {
		return ResponseEntity.status(HttpStatus.OK).body(EmployeeLoginResponse.builder().error(Boolean.FALSE)
				.message(employeeLoginService.validateOTP(verifyOTPDto)).build());
	}

}
