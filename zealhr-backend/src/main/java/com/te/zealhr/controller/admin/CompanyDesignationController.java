package com.te.zealhr.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.audit.BaseConfigController;
import com.te.zealhr.dto.admin.CompanyDesignationInfoDto;
import com.te.zealhr.dto.admin.DeleteCompanyDesignationDto;
import com.te.zealhr.dto.admin.DesignationUploadDTO;
import com.te.zealhr.dto.admin.RoleDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.admin.CompanyDesignationService;

@CrossOrigin(origins = "*")
/**
 * @author Tanveer Ahmed
 * @author Trupthi
 *
 */

@RestController
@RequestMapping("/api/v1/company-info/designation")
public class CompanyDesignationController extends BaseConfigController {

	@Autowired
	private CompanyDesignationService companyDesignationService;

	@PostMapping("/{parentDesignationId}")
	public ResponseEntity<SuccessResponse> addCompanyDesignation(@PathVariable long parentDesignationId,
			@RequestBody CompanyDesignationInfoDto companyDesignationInfoDto) {

		return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.builder().error(Boolean.FALSE)
				.message("Designation Addedd Successfully").data(companyDesignationService
						.addCompanyDesignation(getCompanyId(), parentDesignationId, companyDesignationInfoDto))
				.build());

	}

	@PutMapping
	public ResponseEntity<SuccessResponse> updateCompanyDesignation(
			@RequestBody CompanyDesignationInfoDto companyDesignationInfoDto) {

		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(Boolean.FALSE)
				.message("Designation Name Updated SuccessFully")
				.data(companyDesignationService.updateCompanyDesignation(getCompanyId(), companyDesignationInfoDto))
				.build());

	}

	@PostMapping
	public ResponseEntity<SuccessResponse> getAllDepartmentDesignation(
			@RequestBody CompanyDesignationInfoDto companyDesignationInfoDto) {

		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(Boolean.FALSE)
				.message("Designations Feteched Successfully").data(companyDesignationService
						.getAllDepartmentDesignation(getCompanyId(), companyDesignationInfoDto.getDepartment()))
				.build());

	}

	@PostMapping("/roles")
	public ResponseEntity<SuccessResponse> getAllRole(@RequestBody RoleDTO roleDTO) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Roles Feteched Successfully")
						.data(companyDesignationService.getRoleForDesinagtion(roleDTO)).build());

	}

	@DeleteMapping
	public ResponseEntity<SuccessResponse> deleteCompanyDesignation(
			@RequestBody DeleteCompanyDesignationDto deleteCompanyDesignationDto) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Designation Deleted SuccessFully")
						.data(companyDesignationService.deleteCompanyDesignation(deleteCompanyDesignationDto)).build());

	}
	
	@PostMapping("/upload")
	public ResponseEntity<SuccessResponse> companyDesignationUpload(
			@RequestBody List<DesignationUploadDTO> designationUploadDTOList) {
		String uploadCompanyDesignation = companyDesignationService.uploadCompanyDesignation(getCompanyId(), designationUploadDTOList);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message(uploadCompanyDesignation).data(
						uploadCompanyDesignation)
						.build());

	}


}
