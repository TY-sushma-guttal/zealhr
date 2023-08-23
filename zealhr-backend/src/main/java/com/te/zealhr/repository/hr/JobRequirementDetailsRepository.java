package com.te.zealhr.repository.hr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.zealhr.entity.hr.JobRequirementDetails;

public interface JobRequirementDetailsRepository extends JpaRepository<JobRequirementDetails, Long> {

	JobRequirementDetails findByCompanyInfoCompanyIdAndRequirementId(Long companyId, Long requirementId);

	List<JobRequirementDetails> findByCompanyInfoCompanyId(Long companyId);

	List<JobRequirementDetails> findByCompanyInfoCompanyIdAndJobRole(Long companyId, String jobRole);

}
