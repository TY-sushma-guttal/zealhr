package com.te.zealhr.service.hr;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.te.zealhr.dto.hr.AnnouncementDetailsDTO;
import com.te.zealhr.dto.hr.EmployeeInformationDTO;
import com.te.zealhr.entity.admin.CompanyInfo;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.hr.AnnouncementDetails;
import com.te.zealhr.exception.DataNotFoundException;
import com.te.zealhr.repository.admin.CompanyInfoRepository;
import com.te.zealhr.repository.hr.AnnouncementDetailsRepository;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

	@Autowired
	private AnnouncementDetailsRepository announcementDetailsRepository;

	@Autowired
	private CompanyInfoRepository companyInfoRepository;

	@Override
	@Transactional
	public AnnouncementDetailsDTO saveAnnouncement(AnnouncementDetailsDTO announcementDetailsDTO, Long companyId) {
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new DataNotFoundException("Company not Found"));
		AnnouncementDetails announcementDetails = new AnnouncementDetails();
		BeanUtils.copyProperties(announcementDetailsDTO, announcementDetails);
		announcementDetails.setCompanyInfo(companyInfo);
		if (announcementDetailsDTO.getEmployeeIdList() != null) {
			List<EmployeePersonalInfo> employees = companyInfo.getEmployeePersonalInfoList().stream()
					.filter(employee -> employee.getEmployeeOfficialInfo() != null).collect(Collectors.toList());
			for (String employeeId : announcementDetailsDTO.getEmployeeIdList()) {
				if (employees.stream()
						.noneMatch(employee -> employee.getEmployeeOfficialInfo().getEmployeeId().equals(employeeId))) {
					throw new DataNotFoundException("Selected Employee Not Found");
				}
			}
			announcementDetails.setEmployees(announcementDetailsDTO.getEmployeeIdList());
		}
		if (announcementDetailsDTO.getEmployeePersonalInfoId() != null) {
			Optional<EmployeePersonalInfo> employeePersonalInfo = companyInfo.getEmployeePersonalInfoList().stream()
					.filter(employee -> employee.getEmployeeInfoId()
							.equals(announcementDetailsDTO.getEmployeePersonalInfoId()))
					.findAny();
			if (!employeePersonalInfo.isPresent()) {
				throw new DataNotFoundException("Selected Employee Not Found");
			}
			announcementDetails.setEmployeePersonalInfo(employeePersonalInfo.get());
		}
		announcementDetailsRepository.save(announcementDetails);
		return announcementDetailsDTO;
	}

	@Override
	@Transactional
	public AnnouncementDetailsDTO updateAnnouncement(AnnouncementDetailsDTO announcementDetailsDTO) {
		AnnouncementDetails announcementDetails = announcementDetailsRepository
				.findById(announcementDetailsDTO.getAnnouncementId())
				.orElseThrow(() -> new DataNotFoundException("Announcement Not Found"));
		if (announcementDetails.getCreatedDate().isAfter(LocalDateTime.now().minusHours(24))
				&& announcementDetails.getCreatedDate().isBefore(LocalDateTime.now())) {
			announcementDetails.setDiscription(announcementDetailsDTO.getDiscription());
		} else {
			throw new DataNotFoundException("Announcement Cannot Be Edited after 24 Hours");
		}
		announcementDetailsRepository.save(announcementDetails);
		return announcementDetailsDTO;
	}

	@Override
	public List<AnnouncementDetailsDTO> getAnnouncements(Long companyId) {
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new DataNotFoundException("Company not Found"));
		return announcementDetailsRepository.findByCompanyInfoCompanyIdAndCreatedDateIsBetween(companyId,
				LocalDateTime.now().minusHours(24), LocalDateTime.now()).stream().map(announcement -> {
					AnnouncementDetailsDTO announcementDetailsDTO = new AnnouncementDetailsDTO();
					BeanUtils.copyProperties(announcement, announcementDetailsDTO);
					if (announcement.getEmployeePersonalInfo() != null) {
						EmployeePersonalInfo employeePersonalInfo = announcement.getEmployeePersonalInfo();
						announcementDetailsDTO
								.setRelatedEmployee(
										new EmployeeInformationDTO(
												employeePersonalInfo.getEmployeeOfficialInfo() == null ? null
														: employeePersonalInfo.getEmployeeOfficialInfo()
																.getEmployeeId(),
												employeePersonalInfo.getEmployeeInfoId(),
												employeePersonalInfo.getFirstName(), employeePersonalInfo.getPictureURL()));
					}
					if (announcement.getEmployees() != null) {
						announcementDetailsDTO.setSelectedEmployees(getSelectedEmployees(companyInfo, announcement));
					}
					return announcementDetailsDTO;
				}).collect(Collectors.toList());

	}

	private List<EmployeeInformationDTO> getSelectedEmployees(CompanyInfo companyInfo,
			AnnouncementDetails announcement) {
		List<EmployeePersonalInfo> employees = companyInfo.getEmployeePersonalInfoList().stream()
				.filter(employee -> employee.getEmployeeOfficialInfo() != null).collect(Collectors.toList());
		List<EmployeeInformationDTO> employeeInformationDTOList = new ArrayList<>();
		announcement.getEmployees().stream().forEach(employeeId -> {
			Optional<EmployeePersonalInfo> employeeInfoOptional = employees.stream()
					.filter(employee -> employee.getEmployeeOfficialInfo().getEmployeeId().equals(employeeId))
					.findAny();
			if (employeeInfoOptional.isPresent()) {
				EmployeePersonalInfo employeeInfo = employeeInfoOptional.get();
				employeeInformationDTOList.add(new EmployeeInformationDTO(
						employeeInfo.getEmployeeOfficialInfo() == null ? null
								: employeeInfo.getEmployeeOfficialInfo().getEmployeeId(),
						employeeInfo.getEmployeeInfoId(), employeeInfo.getFirstName(), employeeInfo.getPictureURL()));
			}
		});
		return employeeInformationDTOList;
	}

	@Override
	public String deleteAnnouncement(Long announcementId) {
		Optional<AnnouncementDetails> optionalAnnouncementDetails = announcementDetailsRepository
				.findById(announcementId);
		if (optionalAnnouncementDetails.isPresent()) {
			announcementDetailsRepository.deleteById(announcementId);
			return "Announcement Details Deleted Successfully";
		} else
			throw new DataNotFoundException("Announcement Does Not Exist");

	}

}
