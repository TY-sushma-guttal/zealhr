package com.te.zealhr.controller.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.audit.BaseConfigController;
import com.te.zealhr.dto.employee.EmployeeRegularizationDetailsDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.employee.EmployeeRegularizationService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/regularization")
@CrossOrigin(origins = "*")
public class EmployeeRegularizationController extends BaseConfigController {

	@Autowired
	private EmployeeRegularizationService employeeRegularizationService;

	@Operation(description = "This API is used to apply the regularizarion")
	@PostMapping
	public ResponseEntity<SuccessResponse> applyRegularization(
			@RequestBody EmployeeRegularizationDetailsDTO regularizationDetailsDTO) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Regularization Applied Successfully!!")
						.data(employeeRegularizationService.applyRegularization(regularizationDetailsDTO,
								getCompanyId(), getUserId()))
						.build());
	}

	@Operation(description = "This API is used to fetch all the regularizarions of an employee")
	@GetMapping(path = {"/all/{year}/{month}", "/all"})
	public ResponseEntity<SuccessResponse> getAllRegularizations(@PathVariable(required = false) Integer year,
			@PathVariable(required = false) Integer month) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Regularizations Fetched Successfully!!")
						.data(employeeRegularizationService.getAllRegularizations(getCompanyId(), getUserId(),
								year, month))
						.build());
	}

	@Operation(description = "This API is used to delete the regularizarion of an employee")
	@DeleteMapping({ "/{regularizationId}" })
	public ResponseEntity<SuccessResponse> deleteRegularizations(@PathVariable Long regularizationId) {
		String deleteRegularization = employeeRegularizationService.deleteRegularization(regularizationId);
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false)
				.message(deleteRegularization).data(deleteRegularization).build());
	}

}
