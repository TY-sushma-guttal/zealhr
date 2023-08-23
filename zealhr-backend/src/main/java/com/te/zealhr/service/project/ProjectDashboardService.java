package com.te.zealhr.service.project;

import java.util.List;

import com.te.zealhr.dto.DashboardRequestDTO;
import com.te.zealhr.dto.DashboardResponseDTO;
import com.te.zealhr.dto.project.ProjectCompleteDetailsDTO;
import com.te.zealhr.dto.project.ProjectDetailsBasicDTO;
import com.te.zealhr.dto.project.mongo.ProjectListDTO;

public interface ProjectDashboardService {

	DashboardResponseDTO getProjectDetails(DashboardRequestDTO dashboardRequestDTO, Long companyId);
	
	List<ProjectListDTO> getAllProjectNames(Long companyId);
	
	ProjectCompleteDetailsDTO getProjectDetailsById(Long projectId, Long companyId);
	
	List<ProjectDetailsBasicDTO> getProjectDetailsByStatus(Long companyId, String type);

}
