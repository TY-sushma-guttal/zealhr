package com.te.zealhr.controller.employee;

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

import com.te.zealhr.dto.employee.EmployeeReimbursementDTO;
import com.te.zealhr.dto.employee.EmployeeReimbursementExpenseCategoryDTO;
import com.te.zealhr.dto.hr.CanlenderRequestDTO;
import com.te.zealhr.entity.employee.EmployeeReimbursementInfo;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.employee.EmployeeReimbursementService;
import com.te.zealhr.audit.BaseConfigController;

@RestController
@RequestMapping("/api/v1/reimbursement")
@CrossOrigin(origins = "*")
public class EmployeeReimbursementController extends BaseConfigController {

	@Autowired
	EmployeeReimbursementService service;

	@GetMapping("/get-type")
	public ResponseEntity<?> getCategories() {

		List<EmployeeReimbursementExpenseCategoryDTO> categoriesList = service.findByExpenseCategoryId(getCompanyId());
		if (categoriesList != null)
			return new ResponseEntity<>(SuccessResponse.builder().data(categoriesList).error(false)
					.message("Data fetched Successfully").build(), HttpStatus.OK);
		else
			return new ResponseEntity<>(
					SuccessResponse.builder().data(categoriesList).error(true).message("Data not available").build(),
					HttpStatus.OK);
	}

	@PostMapping("/{param}")
	public ResponseEntity<SuccessResponse> saveEmployeeReimbursement(
			@RequestBody EmployeeReimbursementDTO reimbursementDTO, @PathVariable("param") Long employeeInfoId) {
		EmployeeReimbursementDTO employeeReimbursementDTO = service.saveEmployeeReimbursement(reimbursementDTO,
				employeeInfoId, getCompanyId());
		if (employeeReimbursementDTO != null) {

			return new ResponseEntity<>(SuccessResponse.builder().data(employeeReimbursementDTO).error(false)
					.message("Reimbursement requested successfully").build(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(SuccessResponse.builder().data(employeeReimbursementDTO).error(true)
					.message("Cannot save the data").build(), HttpStatus.OK);
		}
	}

	@PostMapping("/by-status/{employeeInfoId}/{status}")
	public ResponseEntity<SuccessResponse> getReimbursementDTOList(@RequestBody CanlenderRequestDTO canlenderRequestDTO,
			@PathVariable Long employeeInfoId, @PathVariable String status) {

		List<EmployeeReimbursementDTO> reimbursementDTOList = service.getReimbursementDTOList(canlenderRequestDTO,
				employeeInfoId, getCompanyId(), status);

		if (reimbursementDTOList != null)
			return new ResponseEntity<>(SuccessResponse.builder().data(reimbursementDTOList).error(false)
					.message("Data fetched Successfully").build(), HttpStatus.OK);
		else
			return new ResponseEntity<>(SuccessResponse.builder().data(reimbursementDTOList).error(false)
					.message("Data not available").build(), HttpStatus.OK);
	}

	@GetMapping("/{employeeInfoId}/{reimbursementId}")
	public ResponseEntity<SuccessResponse> getReimbursementDetails(@PathVariable Long employeeInfoId,
			@PathVariable Long reimbursementId) {

		EmployeeReimbursementDTO employeeReimbursement = service.getEmployeeReimbursement(employeeInfoId,
				reimbursementId, getCompanyId());

		if (employeeReimbursement != null)
			return new ResponseEntity<>(SuccessResponse.builder().data(employeeReimbursement).error(false)
					.message("Data fetched Successfully").build(), HttpStatus.OK);
		else
			return new ResponseEntity<>(SuccessResponse.builder().data(employeeReimbursement).error(false)
					.message("Data not available").build(), HttpStatus.OK);
	}

	@DeleteMapping("/{reimbursementId}")
	public ResponseEntity<SuccessResponse> deleteReimbursementRequest(@PathVariable Long reimbursementId) {

		service.deleteReimbursementRequest(reimbursementId);

		return new ResponseEntity<>(SuccessResponse.builder().data(null).error(false).message("Data Deleted").build(),
				HttpStatus.OK);
	}

	@PutMapping("/{reimbursementId}")
	public ResponseEntity<SuccessResponse> editReimbursement(@RequestBody EmployeeReimbursementDTO reimbursementDTO,
			@PathVariable Long reimbursementId) {

		EmployeeReimbursementDTO reimbursementRequest = service.editReimbursementRequest(reimbursementDTO,
				reimbursementId);

		if (reimbursementRequest != null)
			return new ResponseEntity<>(SuccessResponse.builder().data(reimbursementRequest).error(false)
					.message("Request edited Successfully").build(), HttpStatus.OK);
		else
			return new ResponseEntity<>(SuccessResponse.builder().data(reimbursementRequest).error(false)
					.message("Cannot edit the request").build(), HttpStatus.OK);

	}
}
