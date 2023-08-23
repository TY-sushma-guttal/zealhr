package com.te.zealhr.controller.admin;

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

import com.te.zealhr.dto.admin.CompanyDepartmentInfoDTO;
import com.te.zealhr.dto.admin.DeleteCompanyDepartmentDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.admin.CompanyDepartmentService;
import com.te.zealhr.audit.BaseConfigController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/company-info/department")
public class CompanyDepartmentController extends BaseConfigController {

	@Autowired
	private CompanyDepartmentService companyDepartmentService;

	@PostMapping("/{parentDepartmentId}")
	public ResponseEntity<SuccessResponse> addCompanyDepartment(@PathVariable Long parentDepartmentId,
			@RequestBody CompanyDepartmentInfoDTO companyDepartmentInfoDTO) {

		return ResponseEntity
				.status(HttpStatus.CREATED).body(SuccessResponse.builder().error(Boolean.FALSE)
						.message("Department Added Successfully").data(companyDepartmentService
								.addCompanyDepartment(getCompanyId(), parentDepartmentId, companyDepartmentInfoDTO))
						.build());

	}

	@PutMapping
	public ResponseEntity<SuccessResponse> updateCompanyDepartment(
			@RequestBody CompanyDepartmentInfoDTO companyDepartmentInfoDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Department Updated SuccessFully").data(
						companyDepartmentService.updateCompanyDepartment(getCompanyId(), companyDepartmentInfoDTO))
						.build());

	}

	@GetMapping
	public ResponseEntity<SuccessResponse> getAllDepartments() {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Department Fetched Successfully")
						.data(companyDepartmentService.getAllDepartments(getCompanyId())).build());

	}

	@GetMapping("/roles")
	public ResponseEntity<SuccessResponse> getAllRole() {
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(Boolean.FALSE)
				.message("Roles Feteched Successfully").data(companyDepartmentService.getRoleForDepartment()).build());

	}

	@DeleteMapping
	public ResponseEntity<SuccessResponse> deleteCompanyDesignation(
			@RequestBody DeleteCompanyDepartmentDTO deleteCompanyDepartmentDTO) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Department Deleted SuccessFully")
						.data(companyDepartmentService.deleteCompanyDesignation(deleteCompanyDepartmentDTO)).build());

	}

}
