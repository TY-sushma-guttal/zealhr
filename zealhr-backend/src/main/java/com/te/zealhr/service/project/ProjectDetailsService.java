package com.te.zealhr.service.project;

import com.te.zealhr.dto.project.UpdateProjectDTO;
import com.te.zealhr.dto.project.ClientDropDownDTO;
import com.te.zealhr.dto.project.CreateProjectDto;
import com.te.zealhr.dto.project.ProjectDetailsBasicDTO;

import java.util.List;

public interface ProjectDetailsService {

    List<ProjectDetailsBasicDTO> getAllProjectDetailsBasicByCompanyId(Long companyId);

    String createProject(CreateProjectDto createProjectDto, Long companyId);

    String updateProject(UpdateProjectDTO updateProjectDto);
    
    List<ClientDropDownDTO> getCompanyClients(Long companyId);
    
    List<ProjectDetailsBasicDTO> getAllProjectDetailsBasicByCompanyIdOptimse(Long companyId); 
}
