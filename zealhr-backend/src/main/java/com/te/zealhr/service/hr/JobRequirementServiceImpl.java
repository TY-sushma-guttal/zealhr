package com.te.zealhr.service.hr;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.zealhr.dto.hr.JobRequirementDetailsDTO;
import com.te.zealhr.entity.admin.CompanyInfo;
import com.te.zealhr.entity.hr.JobRequirementDetails;
import com.te.zealhr.exception.DataNotFoundException;
import com.te.zealhr.exception.InvalidInputException;
import com.te.zealhr.repository.admin.CompanyInfoRepository;
import com.te.zealhr.repository.hr.JobRequirementDetailsRepository;

@Service
public class JobRequirementServiceImpl implements JobRequirementService {

	@Autowired
	private JobRequirementDetailsRepository jobRequirementDetailsRepository;

	@Autowired
	private CompanyInfoRepository companyInfoRepository;

	/**
	 * This method is used to add/update job requirement.
	 */
	@Override
	@Transactional
	public JobRequirementDetailsDTO addJobRequirement(Long companyId,
			JobRequirementDetailsDTO jobRequirementDetailsDTO) {
		CompanyInfo companyInfo = companyInfoRepository.findByCompanyId(companyId)
				.orElseThrow(() -> new DataNotFoundException("Job Requirement Does Not Exist"));

		if (jobRequirementDetailsRepository
				.findByCompanyInfoCompanyIdAndJobRole(companyId, jobRequirementDetailsDTO.getJobRole()).stream()
				.anyMatch(requirement -> (jobRequirementDetailsDTO.getRequirementId() == null
						|| (!requirement.getRequirementId().equals(jobRequirementDetailsDTO.getRequirementId())))
						&& requirement.getNoOfOpenings() > requirement.getClosedSlots())) {
			throw new InvalidInputException("Requirement for this Job Role Is Already Present and Not Closed");
		}

		JobRequirementDetails jobRequirementDetails = Optional.ofNullable(jobRequirementDetailsRepository
				.findByCompanyInfoCompanyIdAndRequirementId(companyId, jobRequirementDetailsDTO.getRequirementId()))
				.orElseGet(JobRequirementDetails::new);

		BeanUtils.copyProperties(jobRequirementDetailsDTO, jobRequirementDetails);
		jobRequirementDetails.setCompanyInfo(companyInfo);
		jobRequirementDetails = jobRequirementDetailsRepository.save(jobRequirementDetails);
		BeanUtils.copyProperties(jobRequirementDetails, jobRequirementDetailsDTO);
		return jobRequirementDetailsDTO;
	}

	/**
	 * This method is used to fetch all the requirements.
	 */
	@Override
	public List<JobRequirementDetailsDTO> getAllJobRequirements(Long companyId) {
		return jobRequirementDetailsRepository.findByCompanyInfoCompanyId(companyId).stream().map(requirement -> {
			JobRequirementDetailsDTO jobRequirementDetailsDTO = new JobRequirementDetailsDTO();
			BeanUtils.copyProperties(requirement, jobRequirementDetailsDTO);
			return jobRequirementDetailsDTO;
		}).collect(Collectors.toList());
	}

	/**
	 * This method is used to delete the requirement.
	 */
	@Override
	@Transactional
	public String deleteJobRequirementById(Long companyId, Long requirementId) {
		JobRequirementDetails jobRequirementDetails = Optional.ofNullable(
				jobRequirementDetailsRepository.findByCompanyInfoCompanyIdAndRequirementId(companyId, requirementId))
				.orElseThrow(() -> new DataNotFoundException("Job Requirement Does Not Exist"));
		if (jobRequirementDetails.getCandidateInfoList().isEmpty()) {
			jobRequirementDetailsRepository.delete(jobRequirementDetails);
			return "Job Requirement Deleted Successfully!!";
		} else
			throw new InvalidInputException("Cannot Delete Job Requirement!!!");
	}

}
