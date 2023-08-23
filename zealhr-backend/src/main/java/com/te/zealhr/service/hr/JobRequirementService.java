package com.te.zealhr.service.hr;

import java.util.List;

import com.te.zealhr.dto.hr.JobRequirementDetailsDTO;

public interface JobRequirementService {

	JobRequirementDetailsDTO addJobRequirement(Long companyId, JobRequirementDetailsDTO jobRequirementDetailsDTO);

	List<JobRequirementDetailsDTO> getAllJobRequirements(Long companyId);

	String deleteJobRequirementById(Long companyId, Long requirementId);

}
