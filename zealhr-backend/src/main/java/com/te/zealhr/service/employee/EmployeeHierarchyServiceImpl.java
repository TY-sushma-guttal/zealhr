package com.te.zealhr.service.employee;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.zealhr.dto.employee.EmployeeTeamDTO;
import com.te.zealhr.dto.employee.GetReportingManagerDTO;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.employee.EmployeeReportingInfo;
import com.te.zealhr.repository.employee.EmployeePersonalInfoRepository;

@Service
public class EmployeeHierarchyServiceImpl implements EmployeeHierarchyService {

	@Autowired
	private EmployeePersonalInfoRepository personalInfoRepository;

	@Override
	public EmployeeTeamDTO getEmployeeHierarchy(Long employeeInfoId, Long companyId) {

		EmployeePersonalInfo employeePersonalInfo = personalInfoRepository.findById(employeeInfoId).orElse(null);
		if (employeePersonalInfo == null) {
			return new EmployeeTeamDTO();
		}
		List<EmployeeTeamDTO> myTeam = getMyTeam(employeePersonalInfo);
		EmployeeTeamDTO employeeTeamDTO = new EmployeeTeamDTO(employeePersonalInfo.getEmployeeInfoId(),
				employeePersonalInfo.getEmployeeOfficialInfo() == null ? null
						: employeePersonalInfo.getEmployeeOfficialInfo().getDesignation(),
						employeePersonalInfo.getFirstName(), employeePersonalInfo.getPictureURL());
		employeeTeamDTO.setManagerOf(myTeam);
		List<EmployeeTeamDTO> reportingManager = getReportingManager(List.of(employeeTeamDTO), employeePersonalInfo);
		return reportingManager.isEmpty() ? new EmployeeTeamDTO() : reportingManager.get(0);
	}

	List<EmployeeTeamDTO> getReportingManager(List<EmployeeTeamDTO> reportingManagerList,
			EmployeePersonalInfo employeePersonalInfo) {
		if (employeePersonalInfo.getEmployeeInfoList().isEmpty()) {
			return reportingManagerList;
		} else {
			EmployeePersonalInfo reportingManager = employeePersonalInfo.getEmployeeInfoList().get(0)
					.getReportingManager();
			EmployeeTeamDTO employeeTeamDTO = new EmployeeTeamDTO(reportingManager.getEmployeeInfoId(),
					reportingManager.getEmployeeOfficialInfo() == null ? null
							: reportingManager.getEmployeeOfficialInfo().getDesignation(),
					reportingManager.getFirstName(), reportingManager.getPictureURL());
			employeeTeamDTO.setManagerOf(reportingManagerList);
			return getReportingManager(List.of(employeeTeamDTO), reportingManager);
		}
	}

	private EmployeeTeamDTO getTeamDTO(EmployeeReportingInfo employeeReportingInfo) {
		EmployeePersonalInfo employeePersonalInfo = employeeReportingInfo.getEmployeePersonalInfo();
		EmployeeTeamDTO employeeTeamDTO = new EmployeeTeamDTO(employeePersonalInfo.getEmployeeInfoId(),
				employeePersonalInfo.getEmployeeOfficialInfo() == null ? null
						: employeePersonalInfo.getEmployeeOfficialInfo().getDesignation(),
				employeePersonalInfo.getFirstName(), employeePersonalInfo.getPictureURL());
		List<EmployeeTeamDTO> child = new ArrayList<>();
		List<EmployeeReportingInfo> employeeReportingInfoList = employeePersonalInfo.getEmployeeReportingInfoList();
		for (EmployeeReportingInfo employeeReporting : employeeReportingInfoList) {
			child.add(getTeamDTO(employeeReporting));
		}
		employeeTeamDTO.setManagerOf(child);
		return employeeTeamDTO;
	}

	public List<EmployeeTeamDTO> getMyTeam(EmployeePersonalInfo employeePersonalInfo) {
		List<EmployeeReportingInfo> employeeReportingInfoList = employeePersonalInfo.getEmployeeReportingInfoList();
		List<EmployeeTeamDTO> employeeTeamDTOList = new ArrayList<>();
		for (EmployeeReportingInfo employeeReportingInfo : employeeReportingInfoList) {
			employeeTeamDTOList.add(getTeamDTO(employeeReportingInfo));
		}
		return employeeTeamDTOList;
	}
}