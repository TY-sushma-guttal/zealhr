package com.te.zealhr.service.employee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.zealhr.dto.employee.CompanyEmployeeResignationDetailsDTO;
import com.te.zealhr.dto.employee.EmployeeResignationDiscussionDTO;
import com.te.zealhr.dto.hr.NotificationExitInterviewDropdownDTO;
import com.te.zealhr.entity.employee.CompanyEmployeeResignationDetails;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.employee.EmployeeResignationDiscussion;
import com.te.zealhr.exception.DataNotFoundException;
import com.te.zealhr.repository.employee.EmployeePersonalInfoRepository;
import com.te.zealhr.repository.employee.EmployeeResignationRequestRepository;

@Service
public class EmployeeResignationRequestServiceImpl implements EmployeeResignationRequestService {

	@Autowired
	EmployeeResignationRequestRepository resignationRepository;

	@Autowired
	private EmployeePersonalInfoRepository personalInfoRepository;

	@Override
	public CompanyEmployeeResignationDetailsDTO saveEmployeeResignation(
			CompanyEmployeeResignationDetailsDTO resignationDetailsDTO, Long employeeInfoId, Long companyId) {

		 EmployeePersonalInfo personalInfo = personalInfoRepository
				.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId, companyId);
		 if(personalInfo==null) {
			 throw new DataNotFoundException("PersonalInfo Not Found");
		 }
		CompanyEmployeeResignationDetailsDTO newDTO = new CompanyEmployeeResignationDetailsDTO();
		CompanyEmployeeResignationDetails resignationDetails = new CompanyEmployeeResignationDetails();
		BeanUtils.copyProperties(resignationDetailsDTO, resignationDetails);

		resignationDetails.setAppliedDate(LocalDate.now());
		resignationDetails.setStatus("Pending");
		resignationDetails.setCompanyInfo(personalInfo.getCompanyInfo());
		resignationDetails.setEmployeePersonalInfo(personalInfo);
		BeanUtils.copyProperties(resignationRepository.save(resignationDetails), newDTO);

		return newDTO;
	}


	@Override
	public CompanyEmployeeResignationDetailsDTO getEmployeeResignation(Long employeeInfoId,Long companyId) {

		CompanyEmployeeResignationDetailsDTO resignationDto = new CompanyEmployeeResignationDetailsDTO();

		 List<CompanyEmployeeResignationDetails> resignationRequestList = resignationRepository
				.findByEmployeePersonalInfoEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId,companyId);
		 if(resignationRequestList.isEmpty()) {
			 throw new DataNotFoundException("Data Not Found");
		 }
		 int size = resignationRequestList.size();
		 CompanyEmployeeResignationDetails resignationRequest = resignationRequestList.get(size-1);
		 
		List<EmployeeResignationDiscussion> employeeResignationDiscussionList = resignationRequest.getEmployeeResignationDiscussionList();

		EmployeeResignationDiscussionDTO discussionDTO = new EmployeeResignationDiscussionDTO();

		List<EmployeeResignationDiscussionDTO> DTOList = new ArrayList();

		List<LocalDate> discussionDate = new ArrayList();
		
		for (EmployeeResignationDiscussion employeeResignationDiscussion : employeeResignationDiscussionList) {

			BeanUtils.copyProperties(employeeResignationDiscussion, discussionDTO);

			List<NotificationExitInterviewDropdownDTO> organizerDTOList = new ArrayList<>();

			List<EmployeePersonalInfo> organizers = employeeResignationDiscussion.getEmployeePersonalInfoList();

			for (EmployeePersonalInfo personalInfo : organizers) {
				NotificationExitInterviewDropdownDTO organizerDTO = new NotificationExitInterviewDropdownDTO();

				organizerDTO.setEmployeeId(personalInfo.getEmployeeOfficialInfo().getEmployeeId());
				organizerDTO.setEmployeeInfoId(personalInfo.getEmployeeInfoId());
				organizerDTO.setEmployeeName(personalInfo.getFirstName());
				organizerDTOList.add(organizerDTO);
			}
			discussionDTO.setOrganizerDetails(organizerDTOList);

			DTOList.add(discussionDTO);
			discussionDate.add(employeeResignationDiscussion.getDiscussionDate());
		}
		resignationDto.setDiscussion(DTOList);
		resignationDto.setDiscussionDate(discussionDate);
//		resignationDto.setFeedback(null);
		BeanUtils.copyProperties(resignationRequest, resignationDto);

		return resignationDto;
	}

}
