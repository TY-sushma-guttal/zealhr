package com.te.zealhr.service.project;

import java.util.ArrayList;

import java.util.List;

import com.te.zealhr.dto.employee.EmployeeDropdownDTO;
import com.te.zealhr.dto.project.EmployeeDetailsForProjectDTO;
import com.te.zealhr.dto.project.MapEmployeeProjectDTO;
import com.te.zealhr.dto.project.MilestoneDTO;
import com.te.zealhr.dto.project.MilestoneIDNameDTO;
import com.te.zealhr.dto.project.ProjectDetailsDTO;
import com.te.zealhr.dto.project.mongo.ProjectListDTO;
import com.te.zealhr.dto.project.ProjectEstimationDTO;
import com.te.zealhr.dto.project.ProjectListInCompanyDTO;
import com.te.zealhr.dto.project.SubMilestoneDetailsDTO;
import com.te.zealhr.dto.project.SubMilestoneIDNameDTO;
import com.te.zealhr.dto.project.mongo.ProjectListDTO;

/**
 * 
 * @author Ravindra
 *
 */
public interface ProjectManagementService {

	ArrayList<ProjectListDTO> onGoingProjects(Long companyId);

	public MapEmployeeProjectDTO mapEmployeeWithProject(MapEmployeeProjectDTO mapEmployeeProjectDTO);

	public List<EmployeeDropdownDTO> getListOfEmployeesInProject(Long projectId);

	public MilestoneDTO addMilestone(MilestoneDTO milestone);

	public MilestoneDTO getMilestone(String projectObjectId);

	public ProjectDetailsDTO getProjectDetailsById(Long projectId);

	List<MilestoneIDNameDTO> getProjectMilestonesIDName(Long projectId);

	public MilestoneDTO addSubMilestone(MilestoneDTO milestone);

	ProjectEstimationDTO getProjectEstimationDetailsById(Long projectId);

	List<SubMilestoneIDNameDTO> getSubMilestonesIDName(String mileStoneObjectId);

	SubMilestoneDetailsDTO getSubMilestoneById(String projectObjectId, Long milestoneId);

	List<MilestoneDTO> getAllMilestone(Long projectId);
	
	List<EmployeeDetailsForProjectDTO> getEmployeesInProject(Long projectId);

	List<ProjectListInCompanyDTO> getProjectList(Long companyId);
	
	List<EmployeeDropdownDTO> getEmployeesForAllocation(Long projectId, Long companyId);

	String emmployeeUnmapToProject(String employeeId, Long projectId, Long companyId);
}
