package com.te.zealhr.controller.employee;

import static com.te.zealhr.common.hr.HrConstants.CANDIDATE_RECORD_NOT_FOUND;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.employee.EmployeeAdvanceSalaryDTO;
import com.te.zealhr.dto.employee.EmployeePerformanceDetailsDTO;
import com.te.zealhr.dto.employee.EmployeePerformanceInfoDTO;
import com.te.zealhr.dto.hr.CanlenderRequestDTO;
import com.te.zealhr.entity.employee.EmployeeAdvanceSalary;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.employee.EmployeesAdvanceSalaryService;
import com.te.zealhr.audit.BaseConfigController;

@RestController
@RequestMapping("/api/v1/advance-salary")
@CrossOrigin(origins = "*")
public class EmployeeAdvanceSalaryController extends BaseConfigController {

	@Autowired
	private EmployeesAdvanceSalaryService service;

	@PostMapping("/{param}")
	public ResponseEntity<SuccessResponse> saveAdvanceSalaryRequest(
			@RequestBody EmployeeAdvanceSalaryDTO advanceSalaryDTO, @PathVariable("param") Long employeeInfoId) {
		EmployeeAdvanceSalaryDTO DTO = service.saveAdvanceSalaryRequest(getCompanyId(), advanceSalaryDTO,
				employeeInfoId);
		if (DTO != null)
			return new ResponseEntity<>(SuccessResponse.builder().data(DTO).error(false)
					.message("Advance request added successfully").build(), HttpStatus.OK);
		else
			return new ResponseEntity<>(
					SuccessResponse.builder().data(DTO).error(true).message("Data Not Available").build(),
					HttpStatus.NOT_FOUND);
	}

	@PostMapping("/all/{param}")
	public ResponseEntity<SuccessResponse> getAdvanceSalaryDTOList(@RequestBody CanlenderRequestDTO canlenderRequestDTO,
			@PathVariable("param") Long employeeInfoId) {

		List<EmployeeAdvanceSalaryDTO> advanceSalaryDTOList = service.getAdvanceSalaryDTOList(canlenderRequestDTO, employeeInfoId,
				getCompanyId());
		if (advanceSalaryDTOList != null) {
			return new ResponseEntity<>(SuccessResponse.builder().data(advanceSalaryDTOList).error(false)
					.message("Data Fetched Successfully").build(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(SuccessResponse.builder().data(advanceSalaryDTOList).error(true)
					.message("Data Not Available").build(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/{param}")
	public ResponseEntity<SuccessResponse> getAdvanceSalary(@PathVariable("param") Long advanceSalaryId) {

		EmployeeAdvanceSalaryDTO advanceSalary = service.getAdvanceSalary(advanceSalaryId, getCompanyId());
		if (advanceSalary != null) {
			return new ResponseEntity<>(SuccessResponse.builder().data(advanceSalary).error(false)
					.message("Data Fetched Successfully").build(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(advanceSalary).error(true).message("Data Not Available").build(),
					HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/{param}")
	public ResponseEntity<SuccessResponse> deleteAdvanceSalaryRequest(@PathVariable("param") Long advanceSalaryId) {
		service.deleteAdvanceSalaryRequest(advanceSalaryId, getCompanyId());
		SuccessResponse response = new SuccessResponse(false, "Request Deleted Succesfully", null);
		return new ResponseEntity<SuccessResponse>(response, HttpStatus.OK);
	}

	@PutMapping("/{param}")
	public ResponseEntity<SuccessResponse> editAdvanceSalaryRequest(
			@RequestBody EmployeeAdvanceSalaryDTO advanceSalaryDTO, @PathVariable("param") Long advanceSalaryId) {

		EmployeeAdvanceSalaryDTO advanceSalary = service.editAdvanceSalaryRequest(advanceSalaryDTO, advanceSalaryId,
				getCompanyId());
		if (advanceSalary != null)
			return new ResponseEntity<>(SuccessResponse.builder().data(advanceSalary).error(false)
					.message("Request Edited Successfully").build(), HttpStatus.OK);
		else
			return new ResponseEntity<>(SuccessResponse.builder().data(advanceSalary).error(true).message(null).build(),
					HttpStatus.OK);

	}

	@PostMapping("/performance")
	public ResponseEntity<SuccessResponse> getPerformanceMonthly(
			@RequestBody EmployeePerformanceDetailsDTO performanceDto) {
		return new ResponseEntity<>(new SuccessResponse(false, "Employee monthly performance Fetched successfully",
				service.getPerformanceMonthly(performanceDto, getCompanyId())), HttpStatus.OK);
	}

}
