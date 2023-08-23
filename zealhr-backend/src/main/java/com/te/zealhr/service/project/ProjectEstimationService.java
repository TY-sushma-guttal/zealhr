package com.te.zealhr.service.project;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.te.zealhr.dto.project.ProjectDetailsBasicDTO;
import com.te.zealhr.dto.project.ProjectEstimationDTO;
import com.te.zealhr.dto.project.UpdateProjectStatusDTO;

public interface ProjectEstimationService {

	ProjectEstimationDTO estimate(ProjectEstimationDTO projectEstimationDTO, MultipartFile file);

	List<ProjectDetailsBasicDTO> getAllProjectDetailsEstimation(Long companyId);

	ProjectDetailsBasicDTO getProjectDetailsEstimation(Long projectId);
	
	ProjectEstimationDTO getEstimationByProject(Long projectId);
	
	String updateStatus(UpdateProjectStatusDTO updateProjectStatusDTO);

}
