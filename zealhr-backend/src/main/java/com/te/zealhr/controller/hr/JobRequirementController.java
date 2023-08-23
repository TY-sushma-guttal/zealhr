package com.te.zealhr.controller.hr;

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
import com.te.zealhr.dto.hr.JobRequirementDetailsDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.hr.JobRequirementService;

import io.swagger.v3.oas.annotations.Operation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/hr/job-requirement")
public class JobRequirementController extends BaseConfigController {

	@Autowired
	private JobRequirementService jobRequirementService;

	@PostMapping
	@Operation(description = "This API is used to add or update job requirement")
	public ResponseEntity<SuccessResponse> addJobRequirement(
			@RequestBody JobRequirementDetailsDTO jobRequirementDetailsDTO) {
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(Boolean.FALSE)
				.message("Job Requirement Added/Updated Successfully!!")
				.data(jobRequirementService.addJobRequirement(getCompanyId(), jobRequirementDetailsDTO)).build());
	}

	@GetMapping
	@Operation(description = "This API is used to fetch all the job requirements")
	public ResponseEntity<SuccessResponse> getAllJobRequirements() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message("Job Requirements Fetched Successfully!!")
						.data(jobRequirementService.getAllJobRequirements(getCompanyId())).build());
	}
	
	@DeleteMapping("/{requirementId}")
	@Operation(description = "This API is used to delete job requirement")
	public ResponseEntity<SuccessResponse> deleteJobRequirementById(@PathVariable Long requirementId) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message("Job Requirement Deleted Successfully!!")
						.data(jobRequirementService.deleteJobRequirementById(getCompanyId(), requirementId)).build());
	}

}
