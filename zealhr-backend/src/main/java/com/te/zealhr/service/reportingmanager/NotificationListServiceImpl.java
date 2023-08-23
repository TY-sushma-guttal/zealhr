package com.te.zealhr.service.reportingmanager;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.zealhr.dto.reportingmanager.ApprisalMeetingDTO;
import com.te.zealhr.dto.reportingmanager.EditInterviewDTO;
import com.te.zealhr.dto.reportingmanager.EligibleEmployeeDetailsDTO;
import com.te.zealhr.dto.reportingmanager.EmployeeDetailsDTO;
import com.te.zealhr.dto.reportingmanager.ProjectDetailsDTO;
import com.te.zealhr.dto.reportingmanager.ScheduleInterviewDTO;
import com.te.zealhr.entity.employee.ApprisalMeetingInfo;
import com.te.zealhr.entity.employee.EmployeeOfficialInfo;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.employee.EmployeeReportingInfo;
import com.te.zealhr.entity.employee.EmployeeReviseSalary;
import com.te.zealhr.entity.project.ProjectDetails;
import com.te.zealhr.exception.admin.NoDataPresentException;
import com.te.zealhr.exception.employee.DataNotFoundException;
import com.te.zealhr.repository.employee.ApprisalMeetingInfoRepository;
import com.te.zealhr.repository.employee.EmployeePersonalInfoRepository;
import com.te.zealhr.repository.employee.EmployeeReportingInfoRepository;
import com.te.zealhr.service.notification.employee.InAppNotificationServiceImpl;
import com.te.zealhr.service.notification.employee.PushNotificationService;

@Service
public class NotificationListServiceImpl implements NotificationListService {

	@Autowired
	EmployeeReportingInfoRepository reportingInfoRepository;

	@Autowired
	EmployeePersonalInfoRepository infoRepository;

	@Autowired
	ApprisalMeetingInfoRepository meetingInfoRepository;

	@Autowired
	InAppNotificationServiceImpl notificationServiceImpl;

	@Autowired
	PushNotificationService pushNotificationService;

	@Override
	public List<EligibleEmployeeDetailsDTO> getEmployeeList(Long employeeInfoId, Long companyId) {

		List<EmployeeReportingInfo> employeeReportingInfoList = reportingInfoRepository
				.findByReportingManagerEmployeeInfoIdAndReportingManagerCompanyInfoCompanyId(employeeInfoId, companyId);

		if (employeeReportingInfoList.isEmpty()) {
			throw new NoDataPresentException("Info Not Found");

		}

		List<EligibleEmployeeDetailsDTO> detailsDTOList = new ArrayList<>();

		for (EmployeeReportingInfo employeeReportingInfo : employeeReportingInfoList) {

			EligibleEmployeeDetailsDTO detailsDTO = new EligibleEmployeeDetailsDTO();
			EmployeePersonalInfo employeePersonalInfo = employeeReportingInfo.getEmployeePersonalInfo();
			if (employeePersonalInfo != null) {
				List<EmployeeReviseSalary> employeeReviseSalary = employeePersonalInfo.getEmployeeReviseSalaryList();
				EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
				if (employeeOfficialInfo != null) {

					if (!employeePersonalInfo.getEmployeeReviseSalaryList().isEmpty()) {

						EmployeeReviseSalary reviseSalary = employeeReviseSalary.get(employeeReviseSalary.size() - 1);
						Long reviseSalaryId = reviseSalary.getReviseSalaryId();
						if ((meetingInfoRepository.findByEmployeeReviseSalaryReviseSalaryId(reviseSalaryId)).isEmpty()
								&& reviseSalary.getAmount() == null && reviseSalary.getReason() == null) {

							detailsDTO.setEmployeeId(employeeReportingInfo.getEmployeePersonalInfo()
									.getEmployeeOfficialInfo().getEmployeeId());
							detailsDTO.setEmployeeInfoId(
									employeeReportingInfo.getEmployeePersonalInfo().getEmployeeInfoId());
							detailsDTO.setFullName(employeeReportingInfo.getEmployeePersonalInfo().getFirstName());
							detailsDTO.setEmailId(employeeReportingInfo.getEmployeePersonalInfo()
									.getEmployeeOfficialInfo().getOfficialEmailId());
							detailsDTO.setDepartment(employeeReportingInfo.getEmployeePersonalInfo()
									.getEmployeeOfficialInfo().getDepartment());
							detailsDTO.setDesignation(employeeReportingInfo.getEmployeePersonalInfo()
									.getEmployeeOfficialInfo().getDesignation());
							detailsDTOList.add(detailsDTO);
						}
					}
				}
			}
		}
		return detailsDTOList;
	}

	@Override
	public ApprisalMeetingDTO getEmployee(Long employeeInfoId, Long companyId) {

		EmployeePersonalInfo personalInfos = infoRepository.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId,
				companyId);
		if (personalInfos == null) {
			throw new DataNotFoundException("Info Not Found");
		}
		EmployeeOfficialInfo employeeOfficialInfo = personalInfos.getEmployeeOfficialInfo();

		if (employeeOfficialInfo == null) {
			throw new DataNotFoundException("OfficialInfo Not Found");
		}
		ApprisalMeetingDTO detailsDTO = new ApprisalMeetingDTO();

		BeanUtils.copyProperties(employeeOfficialInfo, detailsDTO);
		detailsDTO.setFullName(personalInfos.getFirstName());
		detailsDTO.setEmployeeInfoId(personalInfos.getEmployeeInfoId());

		List<EmployeeReviseSalary> employeeReviseSalaryList = personalInfos.getEmployeeReviseSalaryList();
		EmployeeReviseSalary employeeReviseSalary = employeeReviseSalaryList.get(employeeReviseSalaryList.size() - 1);
		detailsDTO.setDueDate(employeeReviseSalary.getApprisalDate());

		detailsDTO.setMobileNumber(personalInfos.getMobileNumber());
		List<ProjectDetails> allocatedProjectList = personalInfos.getAllocatedProjectList();
		List<ProjectDetailsDTO> projectDetailsDTOs = new ArrayList<>();
		for (ProjectDetails projectDetails : allocatedProjectList) {
			ProjectDetailsDTO projectDetailsDTO = new ProjectDetailsDTO();
			projectDetailsDTO.setProjectId(projectDetails.getProjectId());
			projectDetailsDTO.setProjectName(projectDetails.getProjectName());
			projectDetailsDTOs.add(projectDetailsDTO);
		}

		detailsDTO.setProject(projectDetailsDTOs);
		detailsDTO.setDueDate(personalInfos.getEmployeeReviseSalaryList().get(0).getApprisalDate());

		return detailsDTO;
	}

	@Override
	public ScheduleInterviewDTO addInterview(ScheduleInterviewDTO interviewDTO, Long employeeInfoId, Long companyId,
			Long userId) {

		EmployeePersonalInfo info = infoRepository.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId,
				companyId);

		if (info == null) {
			throw new DataNotFoundException("Info Not Found");
		}
		if (info.getEmployeeReviseSalaryList() == null || info.getEmployeeReviseSalaryList().isEmpty()) {
			throw new DataNotFoundException("RevisedSalary Info Not Found");
		}

		EmployeeOfficialInfo employeeOfficialInfo = info.getEmployeeOfficialInfo();

		if (employeeOfficialInfo == null) {
			throw new DataNotFoundException("OfficialInfo Not Found");
		}

		ApprisalMeetingInfo apprisalMeetingInfo = new ApprisalMeetingInfo();

		BeanUtils.copyProperties(interviewDTO, apprisalMeetingInfo);

		List<Long> employeeInfoIdList = interviewDTO.getEmployeeInfoIdList();
		if (employeeInfoIdList.isEmpty()) {
			throw new DataNotFoundException("Attendees information cannot be empty");
		}
		employeeInfoIdList.add(userId);

		List<EmployeePersonalInfo> employeePersonalInfo = infoRepository.findAllById(employeeInfoIdList);
		employeePersonalInfo.forEach(pi -> {
			List<ApprisalMeetingInfo> listtemp = pi.getApprisalMeetingInfoList();
//			if(listtemp == null) {
//				listtemp = new ArrayList<>();
//			}
			listtemp.add(apprisalMeetingInfo);
			pi.setApprisalMeetingInfoList(listtemp);
			notificationServiceImpl.saveNotification("Apprisal Meeting is scheduled for " + info.getFirstName() + " on "
					+ apprisalMeetingInfo.getMeetingDate(), pi.getEmployeeInfoId());

			if (pi.getExpoToken() != null) {
				pushNotificationService.pushMessage("zealhr", "Apprisal Meeting is scheduled for " + info.getFirstName()
						+ " on " + apprisalMeetingInfo.getMeetingDate(), pi.getExpoToken());
			}
		});
		apprisalMeetingInfo.setStatus("Pending");
		apprisalMeetingInfo.setEmployeePersonalInfoList(employeePersonalInfo);
		apprisalMeetingInfo.setEmployeeReviseSalary(info.getEmployeeReviseSalaryList().get(0));
		BeanUtils.copyProperties(meetingInfoRepository.save(apprisalMeetingInfo), interviewDTO);
		notificationServiceImpl.saveNotification(
				"Apprisal Meeting is scheduled on " + apprisalMeetingInfo.getMeetingDate(), info.getEmployeeInfoId());

		if (info.getExpoToken() != null) {
			pushNotificationService.pushMessage("zealhr",
					"Apprisal Meeting is scheduled on " + apprisalMeetingInfo.getMeetingDate(), info.getExpoToken());
		}

		return interviewDTO;
	}

	@Override
	public EditInterviewDTO editInterview(EditInterviewDTO editInterviewDTO, Long employeeInfoId, Long meetingId,
			Long companyId, Long userId) {

		ApprisalMeetingInfo meetingInfo = meetingInfoRepository
				.findByMeetingIdAndEmployeeReviseSalaryCompanyInfoCompanyId(meetingId, companyId);

		if (meetingInfo == null) {
			throw new DataNotFoundException("Meeting Details Not Found");
		} else {

			EmployeePersonalInfo info = infoRepository.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId,
					companyId);

			if (info == null) {
				throw new DataNotFoundException("Info Not Found");
			}
			if (info.getEmployeeReviseSalaryList().isEmpty()) {
				throw new DataNotFoundException("Revised Salary Info Not Found");
			}
			EmployeeOfficialInfo employeeOfficialInfo = info.getEmployeeOfficialInfo();

			if (employeeOfficialInfo == null) {
				throw new DataNotFoundException("OfficialInfo Not Found");
			}

			List<Long> employeeInfoIdList = editInterviewDTO.getEmployeeInfoIdList();
			if (employeeInfoIdList.isEmpty()) {
				throw new DataNotFoundException("Attendees information cannot be empty");
			}
			employeeInfoIdList.add(userId);

			List<EmployeePersonalInfo> newAttendiesInfo = infoRepository.findAllById(employeeInfoIdList);
			List<EmployeePersonalInfo> meetingAttendeesList = meetingInfo.getEmployeePersonalInfoList();

			meetingAttendeesList.stream().forEach(employee -> {
				if (!newAttendiesInfo.contains(employee)) {
					employee.getApprisalMeetingInfoList().remove(meetingInfo);
				} else if (newAttendiesInfo.contains(employee)) {
					newAttendiesInfo.remove(employee);
				}
			});

			newAttendiesInfo.stream().forEach(attendies -> {
				attendies.getApprisalMeetingInfoList().add(meetingInfo);
			});

			BeanUtils.copyProperties(editInterviewDTO, meetingInfo);

			meetingInfo.setStatus("Pending");
			meetingInfo.setEmployeePersonalInfoList(newAttendiesInfo);
			meetingInfo.setEmployeeReviseSalary(info.getEmployeeReviseSalaryList().get(0));
			BeanUtils.copyProperties(meetingInfoRepository.save(meetingInfo), editInterviewDTO);

			return editInterviewDTO;
		}
	}

}
