package com.te.zealhr.service.project;

import static com.te.zealhr.common.project.ProjectManagementConstants.APPROVED;
import static com.te.zealhr.common.project.ProjectManagementConstants.REJECTED;
import static com.te.zealhr.common.project.ProjectManagementConstants.STATUS_NOT_ESTIMATED;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.te.zealhr.dto.project.ProjectDetailsBasicDTO;
import com.te.zealhr.dto.project.ProjectEstimationDTO;
import com.te.zealhr.dto.project.UpdateProjectStatusDTO;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.project.ProjectDetails;
import com.te.zealhr.entity.project.ProjectEstimationDetails;
import com.te.zealhr.entity.sales.CompanyClientInfo;
import com.te.zealhr.exception.employee.DataNotFoundException;
import com.te.zealhr.repository.employee.EmployeePersonelInfoRepository;
import com.te.zealhr.repository.project.ProjectDetailsRepository;
import com.te.zealhr.repository.project.ProjectEstimationDetailsRepository;
import com.te.zealhr.service.notification.employee.InAppNotificationServiceImpl;
import com.te.zealhr.service.notification.employee.PushNotificationService;
import com.te.zealhr.util.S3UploadFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProjectEstimationServiceImpl implements ProjectEstimationService {

	@Autowired
	private S3UploadFile s3UploadFile;

	@Autowired
	private ProjectDetailsRepository projectDetailsRepository;

	@Autowired
	private ProjectEstimationDetailsRepository projectEstimationDetailsRepository;

	@Autowired
	private InAppNotificationServiceImpl notificationServiceImpl;

	@Autowired
	private PushNotificationService pushNotificationService;

	@Autowired
	private EmployeePersonelInfoRepository employeePersonelInfoRepository;

	@Override
	@Transactional
	public ProjectEstimationDTO estimate(ProjectEstimationDTO projectEstimationDTO, MultipartFile file) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(projectEstimationDTO.getProjectId())
				.orElseThrow(() -> new DataNotFoundException("Project Not Found"));
		Optional<ProjectEstimationDetails> findByProjectDetailsProjectId = projectEstimationDetailsRepository
				.findByProjectDetailsProjectId(projectEstimationDTO.getProjectId());
		if (findByProjectDetailsProjectId.isPresent()) {
			throw new DataNotFoundException("Project is Already Estimated");
		}
		if (!file.isEmpty()) {
			projectEstimationDTO.setFileURL(s3UploadFile.uploadFile(file));
		}
		ProjectEstimationDetails projectEstimationDetails = new ProjectEstimationDetails();
		BeanUtils.copyProperties(projectEstimationDTO, projectEstimationDetails);
		projectEstimationDetails.setStartDate(LocalDate.parse(projectEstimationDTO.getStartDate()));
		projectEstimationDetails.setEndDate(LocalDate.parse(projectEstimationDTO.getEndDate()));
		projectEstimationDetails.setStatus("In-Progress");
		projectEstimationDetails.setProjectDetails(projectDetails);
		projectDetails.setProjectEstimationDetails(projectEstimationDetails);
		ProjectEstimationDetails save = projectEstimationDetailsRepository.save(projectEstimationDetails);
		projectEstimationDTO.setEstimationId(save.getEstimationId());
		notificationServiceImpl.saveNotification(
				"Estimation of " + projectDetails.getProjectName() + "project Approved Successfully",
				projectDetails.getCreatedBy());

		return projectEstimationDTO;
	}

	@Override
	public ProjectEstimationDTO getEstimationByProject(Long projectId) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(projectId)
				.orElseThrow(() -> new DataNotFoundException("Project Not Found"));
		ProjectEstimationDetails projectEstimationDetails = projectDetails.getProjectEstimationDetails();
		if (projectEstimationDetails == null) {
			return null;
		}
		ProjectEstimationDTO projectEstimationDTO = new ProjectEstimationDTO();
		BeanUtils.copyProperties(projectEstimationDetails, projectEstimationDTO);
		projectEstimationDTO.setProjectId(projectId);
		projectEstimationDTO.setStartDate(projectEstimationDetails.getStartDate().toString());
		projectEstimationDTO.setEndDate(projectEstimationDetails.getEndDate().toString());
		if (projectEstimationDetails.getStatus().equalsIgnoreCase(REJECTED)) {
			projectEstimationDTO.setRejectionReason(projectEstimationDetails.getRejectionReason());
		}
		return projectEstimationDTO;
	}

	@Override
	public List<ProjectDetailsBasicDTO> getAllProjectDetailsEstimation(Long companyId) {

		log.info("getAllProjectDetailsEstimation method, execution start");
		List<ProjectDetailsBasicDTO> projectDetailsBasicDto = new ArrayList<>();
		List<ProjectDetails> projectDeatilsList = projectDetailsRepository.findByCompanyInfoCompanyId(companyId);
		projectDeatilsList.stream().forEach(projectDetails -> {
			ProjectDetailsBasicDTO projectDetailsDTO = new ProjectDetailsBasicDTO();
			BeanUtils.copyProperties(projectDetails, projectDetailsDTO);
			EmployeePersonalInfo ownerPersonalInfo = projectDetails.getCompanyInfo().getEmployeePersonalInfoList()
					.stream().filter(e -> Objects.equals(e.getEmployeeInfoId(), projectDetails.getCreatedBy()))
					.findFirst().orElse(null);

			if (ownerPersonalInfo != null) {
				projectDetailsDTO
						.setProjectOwner(ownerPersonalInfo.getFirstName());
			}
			CompanyClientInfo companyClientInfo = projectDetails.getCompanyClientInfo();
			if (companyClientInfo != null) {
				projectDetailsDTO.setClientName(companyClientInfo.getClientName());
			}
			EmployeePersonalInfo projectManager = projectDetails.getProjectManager();
			if (projectManager != null) {
				projectDetailsDTO.setProjectManager(projectManager.getFirstName());
			}
			projectDetailsBasicDto.add(projectDetailsDTO);
			ProjectEstimationDetails projectEstimationDetails = projectDetails.getProjectEstimationDetails();
			if (projectEstimationDetails == null) {
				projectDetailsDTO.setStatus(STATUS_NOT_ESTIMATED);
			} else {
				projectDetailsDTO.setStatus(projectEstimationDetails.getStatus());
			}

		});
		log.info("getAllProjectDetailsEstimation method, execution finished");
		Collections.reverse(projectDetailsBasicDto);
		return projectDetailsBasicDto;
	}

	@Override
	public ProjectDetailsBasicDTO getProjectDetailsEstimation(Long projectId) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(projectId).orElse(null);

		if (projectDetails == null) {
			return null;
		}

		ProjectDetailsBasicDTO projectDetailsDTO = new ProjectDetailsBasicDTO();
		BeanUtils.copyProperties(projectDetails, projectDetailsDTO);
		EmployeePersonalInfo ownerPersonalInfo = projectDetails.getCompanyInfo().getEmployeePersonalInfoList().stream()
				.filter(e -> Objects.equals(e.getEmployeeInfoId(), projectDetails.getCreatedBy())).findFirst()
				.orElse(null);
		if (ownerPersonalInfo != null) {
			projectDetailsDTO.setProjectOwner(ownerPersonalInfo.getFirstName());
		}
		CompanyClientInfo companyClientInfo = projectDetails.getCompanyClientInfo();
		if (companyClientInfo != null) {
			projectDetailsDTO.setClientName(companyClientInfo.getClientName());
		}
		EmployeePersonalInfo projectManager = projectDetails.getProjectManager();
		if (projectManager != null) {
			projectDetailsDTO.setProjectManager(projectManager.getFirstName());
		}
		EmployeePersonalInfo reportingManager = projectDetails.getReportingManager();
		if (reportingManager != null) {
			projectDetailsDTO
					.setReportingManager(reportingManager.getFirstName());
		}
		if (projectDetails.getProjectEstimationDetails() == null) {
			projectDetailsDTO.setStatus(STATUS_NOT_ESTIMATED);
		} else {
			projectDetailsDTO.setStatus(projectDetails.getProjectEstimationDetails().getStatus());
			if (projectDetails.getProjectEstimationDetails().getStatus().equalsIgnoreCase(REJECTED)) {
				projectDetailsDTO.setRejectionReason(projectDetails.getProjectEstimationDetails().getRejectionReason());
			}
		}
		return projectDetailsDTO;
	}

	@Override
	@Transactional
	public String updateStatus(UpdateProjectStatusDTO updateProjectStatusDTO) {
		ProjectDetails projectDetails = projectDetailsRepository.findById(updateProjectStatusDTO.getProjectId())
				.orElseThrow(() -> new DataNotFoundException("Project Not Found"));
		ProjectEstimationDetails projectEstimationDetails = projectDetails.getProjectEstimationDetails();
		if (projectEstimationDetails == null) {
			throw new DataNotFoundException("Estimation Not Found");
		}
		if (!updateProjectStatusDTO.getStatus().equalsIgnoreCase(APPROVED)
				&& !updateProjectStatusDTO.getStatus().equalsIgnoreCase(REJECTED)) {
			throw new DataNotFoundException("Estimation can only be Approved or Rejected");
		}
		if (APPROVED.equalsIgnoreCase(projectEstimationDetails.getStatus())
				|| REJECTED.equalsIgnoreCase(projectEstimationDetails.getStatus())) {
			throw new DataNotFoundException("Estimation Already " + projectEstimationDetails.getStatus());
		}
		projectEstimationDetails.setStatus(updateProjectStatusDTO.getStatus());
		if (updateProjectStatusDTO.getStatus().equalsIgnoreCase(REJECTED)) {
			projectEstimationDetails.setRejectionReason(updateProjectStatusDTO.getRejectionReason());
		}
		projectEstimationDetailsRepository.save(projectEstimationDetails);
		if (updateProjectStatusDTO.getStatus().equalsIgnoreCase(APPROVED)) {
			notificationServiceImpl.saveNotification(
					"Estimation of " + projectDetails.getProjectName() + " project Approved Successfully",
					projectDetails.getCreatedBy());

			Optional<EmployeePersonalInfo> findById = employeePersonelInfoRepository
					.findById(projectDetails.getCreatedBy());

			if (findById.isPresent() && findById.get().getExpoToken() != null) {
				pushNotificationService.pushMessage("zealhr",
						"Estimation of " + projectDetails.getProjectName() + " project Approved Successfully",
						findById.get().getExpoToken());
			}
		}
		return "Estimation " + updateProjectStatusDTO.getStatus() + " Successfully";
	}

}
