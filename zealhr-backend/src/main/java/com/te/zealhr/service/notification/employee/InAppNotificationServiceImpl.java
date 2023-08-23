package com.te.zealhr.service.notification.employee;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.te.zealhr.dto.employee.EmployeeNotificationDTO;
import com.te.zealhr.dto.sales.EventBirthdayOtherDetailsDTO;
import com.te.zealhr.dto.sales.EventDTO;
import com.te.zealhr.dto.superadmin.CompanyNotificationDTO;
import com.te.zealhr.entity.admin.CompanyInfo;
import com.te.zealhr.entity.employee.EmployeeNotification;
import com.te.zealhr.entity.employee.EmployeeOfficialInfo;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.employee.EmployeeReportingInfo;
import com.te.zealhr.entity.hr.AnnouncementDetails;
import com.te.zealhr.entity.superadmin.CompanyNotification;
import com.te.zealhr.exception.DataNotFoundException;
import com.te.zealhr.repository.admin.CompanyInfoRepository;
import com.te.zealhr.repository.employee.EmployeeNotificationRepository;
import com.te.zealhr.repository.employee.EmployeePersonalInfoRepository;
import com.te.zealhr.repository.employee.EmployeeReportingInfoRepository;
import com.te.zealhr.repository.hr.AnnouncementDetailsRepository;
import com.te.zealhr.repository.hr.CompanyEventDetailsRepository;
import com.te.zealhr.repository.superadmin.CompanyNotificationRepository;
import com.te.zealhr.service.hr.CompanyEventDetailsService;

@Service
public class InAppNotificationServiceImpl implements InAppNotificationService {

	@Autowired
	private CompanyInfoRepository companyInfoRepository;

	@Autowired
	private CompanyNotificationRepository companyNotificationRepository;

	@Autowired
	private CompanyEventDetailsService companyEventDetailsService;

	@Autowired
	private AnnouncementDetailsRepository announcementDetailsRepository;

	@Autowired
	private EmployeeReportingInfoRepository employeeReportingInfoRepository;

	@Autowired
	private EmployeeNotificationRepository employeeNotificationRepository;

	@Autowired
	private CompanyEventDetailsRepository companyEventDetailsRepository;

	@Autowired
	private EmployeePersonalInfoRepository employeePersonalInfoRepository;

	@Override
	public List<EmployeeNotificationDTO> getNotification(Long employeeInfoId) {
		List<EmployeeNotification> employeeNotificationList = employeeNotificationRepository
				.findByReceiverEmployeePersonalInfoEmployeeInfoId(employeeInfoId);
		Collections.reverse(employeeNotificationList);
		return employeeNotificationList.stream()
				.map(notification -> EmployeeNotificationDTO.builder().description(notification.getDescription())
						.employeeNotificationId(notification.getEmployeeNotificationId())
						.isSeen(notification.getIsSeen()).build())
				.collect(Collectors.toList());
	}

	@Transactional
	public EmployeeNotificationDTO saveNotification(String message, Long employeeInfoId) {
		EmployeePersonalInfo employee = employeePersonalInfoRepository.findById(employeeInfoId)
				.orElseThrow(() -> new DataNotFoundException("Employee Not Found"));
		EmployeeNotification employeeNotification = new EmployeeNotification();
		employeeNotification.setDescription(message);
		employeeNotification.setReceiverEmployeePersonalInfo(employee);
		employeeNotification.setIsSeen(Boolean.FALSE);
		employeeNotification = employeeNotificationRepository.save(employeeNotification);
		return EmployeeNotificationDTO.builder().description(employeeNotification.getDescription())
				.employeeNotificationId(employeeNotification.getEmployeeNotificationId()).build();
	}

	@Transactional
	public CompanyNotificationDTO saveCompanyNotification(String message, Long companyId) {
		CompanyInfo companyInfo = companyInfoRepository.findById(companyId)
				.orElseThrow(() -> new DataNotFoundException("Employee Not Found"));
		CompanyNotification notification = new CompanyNotification();
		notification.setDescription(message);
		notification.setIsSeen(Boolean.FALSE);
		notification.setCompanyInfo(companyInfo);
		notification = companyNotificationRepository.save(notification);
		return CompanyNotificationDTO.builder().description(notification.getDescription())
				.companyNotificationId(notification.getCompanyNotificationId()).build();
	}

	@Override
	@Transactional
	public Boolean updateNotification(Long employeeInfoId) {
		employeeNotificationRepository.findByReceiverEmployeePersonalInfoEmployeeInfoIdAndIsSeenFalse(employeeInfoId)
				.stream().forEach(notification -> notification.setIsSeen(Boolean.TRUE));
		return Boolean.TRUE;
	}

	/**
	 * This method is used to fetch all the events, notifications related to
	 * employee, birthday and anniversary wishes.
	 */
	@Override
	public EventBirthdayOtherDetailsDTO getCompanyEventsAndAnnouncementsAndWishes(Long employeeInfoId, Long companyId) {
		Optional<EmployeePersonalInfo> optionalEmployeePersonalInfo = employeePersonalInfoRepository
				.findById(employeeInfoId);
		if (optionalEmployeePersonalInfo.isEmpty())
			throw new DataNotFoundException("Employee Not Found");

		Optional<CompanyInfo> optionalCompanyInfo = companyInfoRepository
				.findById(optionalEmployeePersonalInfo.get().getCompanyInfo().getCompanyId());
		if (optionalCompanyInfo.isEmpty())
			throw new DataNotFoundException("Company Not Found");

		List<LocalDate> birthdayDates = new ArrayList<>();
		List<LocalDate> anniversaryDates = new ArrayList<>();
		for (int i = LocalDate.now().getYear() - 110; i < LocalDate.now().getYear() - 10; i++) {
			birthdayDates.add(LocalDate.of(i, LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth()));
		}
		for (int i = LocalDate.now().getYear() - 110; i < LocalDate.now().getYear(); i++) {
			anniversaryDates.add(LocalDate.of(i, LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth()));
		}

		List<String> birthdayWishes = employeePersonalInfoRepository
				.findByIsActiveTrueAndEmployeeOfficialInfoNotNullAndDobIn(birthdayDates).stream().map(a -> {
					return messageDescription("Birthday", a);
				}).collect(Collectors.toList());

		List<String> anniversaryWishes = employeePersonalInfoRepository
				.findByIsActiveTrueAndEmployeeOfficialInfoNotNullAndEmployeeOfficialInfoDojIn(anniversaryDates).stream()
				.map(a -> {
					return messageDescription("Anniversary", a);
				}).collect(Collectors.toList());
		birthdayWishes.addAll(anniversaryWishes);

		// Fetching event list
		List<EventDTO> events = new ArrayList<>();
		EmployeeOfficialInfo employeeOfficialInfo = optionalEmployeePersonalInfo.get().getEmployeeOfficialInfo();
		if (employeeOfficialInfo != null) {
			events = companyEventDetailsRepository.findByCompanyInfoCompanyId(companyId).stream().filter(
					e -> (e.getEventDate().isAfter(LocalDate.now()) || e.getEventDate().isEqual(LocalDate.now()))
							&& (e.getEmployees().stream()
									.anyMatch(a -> a.equalsIgnoreCase(employeeOfficialInfo.getEmployeeId()))))
					.map(event -> EventDTO.builder().eventDate(event.getEventDate()).eventName(event.getEventTitle())
							.photoUrl(event.getPhotoUrl()).eventEndTime(event.getEndTime())
							.eventStartTime(event.getStartTime()).build())
					.collect(Collectors.toList());
		}

		List<Long> employeeIds = new ArrayList<>();

		List<EmployeeReportingInfo> employeeReportersList = new ArrayList<>();
		// Fetching all the employees reporting to him
//		List<EmployeeReportingInfo> employeeReportersList = optionalEmployeePersonalInfo.get()
//				.getEmployeeReportingInfoList();

		// Fetching his reporting manager
		EmployeeReportingInfo employeeReportingInfo = employeeReportingInfoRepository
				.findByEmployeePersonalInfoEmployeeInfoId(employeeInfoId);

		// Fetching all the employees reporting to his manager
		List<EmployeeReportingInfo> employeesReportingToManagerList = new ArrayList<>();
		employeeIds.addAll(optionalEmployeePersonalInfo.get().getEmployeeReportingInfoList().stream()
				.map(a -> a.getEmployeePersonalInfo().getEmployeeInfoId()).collect(Collectors.toList()));
		if (employeeReportingInfo != null) {
			employeesReportingToManagerList = employeeReportingInfo.getReportingManager()
					.getEmployeeReportingInfoList();
			employeeReportersList.addAll(employeesReportingToManagerList);
			employeeReportersList.add(employeeReportingInfo);
			employeeReportersList.stream().forEach(a -> {
				employeeIds.add(a.getEmployeePersonalInfo().getEmployeeInfoId());
			});
		} else {
			employeeIds.add(employeeInfoId);
		}

		List<AnnouncementDetails> finalAnnouncementList = new ArrayList<>();
		List<AnnouncementDetails> announcementList = announcementDetailsRepository
				.findByCompanyInfoCompanyIdAndCreatedDateIsBetween(companyId, LocalDateTime.now().minusHours(24),
						LocalDateTime.now());
		finalAnnouncementList.addAll(announcementList.stream()
				.filter(announcement -> announcement.getEmployeePersonalInfo() != null
						&& employeeIds.contains(announcement.getEmployeePersonalInfo().getEmployeeInfoId()))
				.collect(Collectors.toList()));
		finalAnnouncementList.addAll(announcementList.stream()
				.filter(announcement -> announcement.getEmployees() != null
						&& announcement.getEmployees().contains(employeeOfficialInfo.getEmployeeId()))
				.collect(Collectors.toList()));
		finalAnnouncementList.addAll(announcementList.stream()
				.filter(announcement -> (announcement.getEmployees() == null || announcement.getEmployees().isEmpty())
						&& announcement.getEmployeePersonalInfo() == null)
				.collect(Collectors.toList()));
		return EventBirthdayOtherDetailsDTO
				.builder().wishes(birthdayWishes).eventDTOs(events).announcements(finalAnnouncementList.stream()
						.map(announcement -> announcement.getDiscription()).collect(Collectors.toList()))
				.notifications(getNotification(employeeInfoId)).build();
	}

	/**
	 * This is the helper method to get the message description for birthday and
	 * anniversary wishes
	 * 
	 * @param type
	 * @param employee
	 * @return
	 */
	private String messageDescription(String type, EmployeePersonalInfo employee) {
		String description = "Today is " + employee.getFirstName() + "'s" + " (Employee Id : "
				+ employee.getEmployeeOfficialInfo().getEmployeeId();
		if (employee.getEmployeeOfficialInfo().getDepartment() != null) {
			description = description + ", " + employee.getEmployeeOfficialInfo().getDepartment() + " team) ";
		} else {
			description = description + ") ";
		}

		return description + type + "!!";
	}

}
