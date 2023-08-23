package com.te.zealhr.repository.project;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.zealhr.entity.project.ProjectEstimationDetails;

public interface ProjectEstimationDetailsRepository extends JpaRepository<ProjectEstimationDetails, Long> {

	List<ProjectEstimationDetails> findByProjectDetailsProjectIdIn(List<Long> listOfProjectId);

	List<ProjectEstimationDetails> findByProjectDetailsCompanyInfoCompanyId(Long companyId);

	Optional<ProjectEstimationDetails> findByProjectDetailsProjectId(Long projectId);

}
