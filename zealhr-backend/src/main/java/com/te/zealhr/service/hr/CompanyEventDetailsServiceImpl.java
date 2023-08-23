package com.te.zealhr.service.hr;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.zealhr.dto.employee.MultipleMailDTO;
import com.te.zealhr.dto.hr.BranchNameFetchDTO;
import com.te.zealhr.dto.hr.CanlenderRequestDTO;
import com.te.zealhr.dto.hr.EmployeeInformationDTO;
import com.te.zealhr.dto.hr.EventManagementAddEventDTO;
import com.te.zealhr.dto.hr.EventManagementAddWinnerDTO;
import com.te.zealhr.dto.hr.EventManagementDepartmentNameDTO;
import com.te.zealhr.dto.hr.EventManagementDisplayEventDTO;
import com.te.zealhr.dto.hr.EventManagementEditEventDTO;
import com.te.zealhr.dto.hr.EventManagementNameFetchDTO;
import com.te.zealhr.dto.hr.EventManagementParticipantslistDTO;
import com.te.zealhr.entity.admin.CompanyBranchInfo;
import com.te.zealhr.entity.admin.CompanyDepartmentDetails;
import com.te.zealhr.entity.admin.CompanyInfo;
import com.te.zealhr.entity.employee.EmployeeOfficialInfo;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.hr.CompanyEventDetails;
import com.te.zealhr.exception.DataNotFoundException;
import com.te.zealhr.exception.PermissionDeniedException;
import com.te.zealhr.exception.admin.NoDataPresentException;
import com.te.zealhr.repository.admin.CompanyBranchInfoRepository;
import com.te.zealhr.repository.admin.CompanyDepartmentDetailsRepository;
import com.te.zealhr.repository.admin.CompanyInfoRepository;
import com.te.zealhr.repository.employee.EmployeeOfficialInfoRepository;
import com.te.zealhr.repository.employee.EmployeePersonalInfoRepository;
import com.te.zealhr.repository.hr.CompanyEventDetailsRepository;
import com.te.zealhr.service.mail.employee.EventMailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CompanyEventDetailsServiceImpl implements CompanyEventDetailsService {

	private CompanyEventDetails companyEventDetails = new CompanyEventDetails();

	@Autowired
	private CompanyEventDetailsRepository companyEventDetailsRepository;

	@Autowired
	private CompanyDepartmentDetailsRepository departmentInfoRepository;

	@Autowired
	private EmployeePersonalInfoRepository employeePersonelInfoRepository;

	@Autowired
	private CompanyBranchInfoRepository branchInfoRepository;

	@Autowired
	private CompanyInfoRepository companyInfoRepository;

	@Autowired
	EmployeeOfficialInfoRepository employeeOfficialInfoRepository;

	@Autowired
	private EventMailService eventMailService;

	// AddEvent Api...

	@Override
	public EventManagementAddEventDTO adddetails(EventManagementAddEventDTO eventdto, Long companyId) {
		Optional<CompanyInfo> companyInfoDetails = companyInfoRepository.findById(companyId);
		if (!companyInfoDetails.isPresent()) {
			throw new DataNotFoundException("Company is not Present");
		} else {
			CompanyEventDetails companyEventDetails = new CompanyEventDetails();

			if (eventdto.getEventDate().isBefore(LocalDate.now())) {
				throw new NoDataPresentException("Previous Events Cannot Be Added");
			}
			BeanUtils.copyProperties(eventdto, companyEventDetails);

			companyEventDetails.setCompanyInfo(companyInfoDetails.get());

			BeanUtils.copyProperties(companyEventDetailsRepository.save(companyEventDetails), eventdto);

			Boolean isMailRequired = eventdto.getIsMailRequired();
			if (isMailRequired) {
				log.info("sending mail");
				sendMail(eventdto.getEmployees(), companyId, eventdto);
			}

			return eventdto;

		}

	}

	// Department names getting Api..

	@Override
	public List<EventManagementDepartmentNameDTO> fetchDepartmentNames(Long companyId) {

		List<CompanyDepartmentDetails> departmentsList = departmentInfoRepository.findByCompanyInfoCompanyId(companyId);

		List<EventManagementDepartmentNameDTO> eventManagementDepartmentNamedtoList = new ArrayList<>();

		for (CompanyDepartmentDetails department : departmentsList) {

			eventManagementDepartmentNamedtoList.add(new EventManagementDepartmentNameDTO(
					department.getCompanyDepartmentId(), department.getCompanyDepartmentName()));
		}
		return eventManagementDepartmentNamedtoList;
	}

	// Eventlist displaying api based on EventId..
	@Override
	public EventManagementDisplayEventDTO displayEventDetails(Long eventId) {

		CompanyEventDetails eventDetails = companyEventDetailsRepository.findByEventId(eventId);
		if (eventDetails != null) {
			CompanyInfo companyInfo = eventDetails.getCompanyInfo();
			if (companyInfo == null) {
				throw new DataNotFoundException("Company Not Found");
			}

			Long companyId = companyInfo.getCompanyId();

			EventManagementDisplayEventDTO eventManagementDisplayEventdto = new EventManagementDisplayEventDTO();

			BeanUtils.copyProperties(eventDetails, eventManagementDisplayEventdto);
			eventManagementDisplayEventdto.setPhotoUrl(eventDetails.getPhotoUrl());
			eventManagementDisplayEventdto.setLocation(eventDetails.getLocation());

			List<EventManagementAddWinnerDTO> winnerlist = new ArrayList<>();
			List<EmployeeInformationDTO> employeelist = new ArrayList<>();

			List<String> winnerIdlist = eventDetails.getWinners();
			List<String> employeeIdlist = eventDetails.getEmployees();

			List<String> departmentsList = eventDetails.getDepartments();

			List<EventManagementDepartmentNameDTO> departmentListDTO = new ArrayList<>();

			List<CompanyDepartmentDetails> alldepartmentList = departmentInfoRepository
					.findByCompanyInfoCompanyId(companyId);

			for (CompanyDepartmentDetails department : alldepartmentList) {
				for (String dName : departmentsList) {
					if (dName.equals(department.getCompanyDepartmentName()))
						departmentListDTO.add(new EventManagementDepartmentNameDTO(department.getCompanyDepartmentId(),
								department.getCompanyDepartmentName()));
				}
			}
			eventManagementDisplayEventdto.setDepartments(departmentListDTO);

			for (String winnerId : winnerIdlist) {

				EventManagementAddWinnerDTO addWinnerDTO = new EventManagementAddWinnerDTO();
				List<EmployeePersonalInfo> winnerInfo = employeePersonelInfoRepository
						.findByEmployeeOfficialInfoEmployeeIdAndCompanyInfoCompanyId(winnerId, companyId);
				if (!winnerInfo.isEmpty()) {

					addWinnerDTO.setEmployeeId(winnerInfo.get(0).getEmployeeOfficialInfo().getEmployeeId());
					addWinnerDTO.setEmployeeInfoId(winnerInfo.get(0).getEmployeeInfoId());
					addWinnerDTO.setWinnerName((winnerInfo.get(0).getFirstName()));
					addWinnerDTO.setDepartment(winnerInfo.get(0).getEmployeeOfficialInfo().getDepartment());

					winnerlist.add(addWinnerDTO);
				} else {
					throw new DataNotFoundException("Employee Personal id not Found");
				}

			}
			for (String employeeId : employeeIdlist) {
				EmployeeInformationDTO employeeInformationDTO = new EmployeeInformationDTO();
				List<EmployeePersonalInfo> employeeInfo = employeePersonelInfoRepository
						.findByEmployeeOfficialInfoEmployeeIdAndCompanyInfoCompanyId(employeeId, companyId);
				if (!employeeInfo.isEmpty()) {

					employeeInformationDTO.setEmployeeId(employeeInfo.get(0).getEmployeeOfficialInfo().getEmployeeId());
					employeeInformationDTO.setEmployeeInfoId(employeeInfo.get(0).getEmployeeInfoId());
					employeeInformationDTO.setFullname(employeeInfo.get(0).getFirstName());
					employeeInformationDTO.setPictureURL(employeeInfo.get(0).getPictureURL());
					employeelist.add(employeeInformationDTO);
				} else {
					throw new DataNotFoundException("Employee Personal id not Found");
				}

			}
			eventManagementDisplayEventdto.setWinners(winnerlist);
			eventManagementDisplayEventdto.setEmployees(employeelist);

			return eventManagementDisplayEventdto;
		} else {
			throw new DataNotFoundException("Event Not Found");
		}

	}

//for fetching the EventLIst based on companyId and Month and Year

	@Override
	public List<EventManagementDisplayEventDTO> displayEventList(CanlenderRequestDTO calCanlenderRequestDTO, Long companyId) {

		Optional<CompanyInfo> optionaldetails = companyInfoRepository.findById(companyId);
		if (optionaldetails.isPresent()) {

			List<LocalDate> startEndDate = getStartEndDate(calCanlenderRequestDTO.getYear(), calCanlenderRequestDTO.getFiscalMonth());
			List<EventManagementDisplayEventDTO> eventList = new ArrayList<>();

			List<CompanyEventDetails> eventDetails = companyEventDetailsRepository
					.findByCompanyInfoCompanyIdAndEventDateBetween(companyId, startEndDate.get(0), startEndDate.get(1));

			eventDetails.stream().forEach(event -> {
				EventManagementDisplayEventDTO eventdto = new EventManagementDisplayEventDTO();
				BeanUtils.copyProperties(event, eventdto);
				List<EventManagementAddWinnerDTO> winnerlist = new ArrayList<>();
				List<String> winnerIdlist = event.getWinners();

				for (String winnerId : winnerIdlist) {

					EventManagementAddWinnerDTO addWinnerDTO = new EventManagementAddWinnerDTO();
					List<EmployeePersonalInfo> winnerInfo = employeePersonelInfoRepository
							.findByEmployeeOfficialInfoEmployeeIdAndCompanyInfoCompanyId(winnerId, companyId);
					if (!winnerInfo.isEmpty()) {

						addWinnerDTO.setEmployeeId(winnerInfo.get(0).getEmployeeOfficialInfo().getEmployeeId());
						addWinnerDTO.setEmployeeInfoId(winnerInfo.get(0).getEmployeeInfoId());
						addWinnerDTO.setWinnerName((winnerInfo.get(0).getFirstName()));
						addWinnerDTO.setDepartment(winnerInfo.get(0).getEmployeeOfficialInfo().getDepartment());
						winnerlist.add(addWinnerDTO);
					} else {
						log.error("Winner's personal employee id not found for event id: " + event.getEventId());
					}
				}
				eventdto.setWinners(winnerlist);
				eventList.add(eventdto);
			});

			List<EventManagementDisplayEventDTO> unsortedeventdtoList = eventList;
			Comparator<EventManagementDisplayEventDTO> basedOnDate = (date1, date2) -> date1.getEventDate()
					.compareTo(date2.getEventDate());
			Comparator<EventManagementDisplayEventDTO> basedOnStartTime = (time1, time2) -> time1.getStartTime()
					.compareTo(time2.getStartTime());

			return unsortedeventdtoList.stream().sorted(basedOnDate.thenComparing(basedOnStartTime))
					.collect(Collectors.toList());
		} else {

			throw new DataNotFoundException("Company Id not present");
		}

	}

	public List<LocalDate> getStartEndDate(String year, String fiscalMonth) {
		Month month = Month.valueOf(fiscalMonth.toUpperCase(Locale.ENGLISH));
		int startMonth = month.getValue();
		int endMonth;
		int startYear;
		int endYear;
		if (year.contains("-")) {
			String[] split = year.split("-");
			startYear = Integer.valueOf(split[0]);
			endYear = Integer.valueOf(split[1]);
			endMonth = startMonth - 1;
		} else {
			startYear = Integer.valueOf(year);
			endYear = Integer.valueOf(year);
			endMonth = 12;
		}
		List<LocalDate> dates = new ArrayList<>();
		dates.add(LocalDate.of(startYear, startMonth, 1));
		LocalDate of = LocalDate.of(endYear, endMonth, 1);
		dates.add(of.withDayOfMonth(of.getMonth().length(of.isLeapYear())));
		return dates;
	}

// for uploading the winner Api
	@Override
	public EventManagementAddWinnerDTO addWinners(EventManagementAddWinnerDTO addWinnerdto, Long eventId) {

		Optional<CompanyEventDetails> eventDetails = companyEventDetailsRepository.findById(eventId);

		if (eventDetails.isPresent()) {

			CompanyEventDetails companyEventDetails2 = eventDetails.get();
			if ((companyEventDetails2.getEventDate().isAfter(LocalDate.now())
					|| (companyEventDetails2.getEventDate().isEqual(LocalDate.now())
							&& companyEventDetails2.getStartTime().isAfter(LocalTime.now())))) {
				throw new PermissionDeniedException("Event Not Yet Started");
			}
			BeanUtils.copyProperties(addWinnerdto, companyEventDetails2);
			/*
			 * if (!(addWinnerdto.getPhotoUrl().endsWith(".jpeg") ||
			 * addWinnerdto.getPhotoUrl().endsWith(".jpg"))) { throw new
			 * PermissionDeniedException("Jpeg Format Only Accepted"); }
			 */
			BeanUtils.copyProperties(companyEventDetailsRepository.save(companyEventDetails2), addWinnerdto);

			return addWinnerdto;
		}

		else {
			throw new DataNotFoundException("Event Not Found");

		}

	}
// for fetching the employee from EmployeePersonelInfo Api..

	@Override
	public List<EventManagementNameFetchDTO> fetchEmployeeNames(Long companyId) {
		Optional<CompanyInfo> optionalCompanyDetails = companyInfoRepository.findById(companyId);
		if (optionalCompanyDetails.isPresent()) {

			List<EmployeePersonalInfo> employeePersonalInfoList = employeePersonelInfoRepository
					.findByCompanyInfoCompanyId(companyId);
			if (employeePersonalInfoList.isEmpty()) {
				throw new DataNotFoundException("Employee Not Present");

			} else {
				List<EventManagementNameFetchDTO> eventManagementNameFetchdtoList = new ArrayList<>();
				for (EmployeePersonalInfo employeePersonalInfo : employeePersonalInfoList) {
					if (employeePersonalInfo.getEmployeeOfficialInfo() != null) {
						eventManagementNameFetchdtoList.add(new EventManagementNameFetchDTO(
								employeePersonalInfo.getEmployeeInfoId(), employeePersonalInfo.getFirstName(),
								employeePersonalInfo.getEmployeeOfficialInfo().getEmployeeId()));
					}
				}

				return eventManagementNameFetchdtoList;
			}
		} else {
			throw new DataNotFoundException("Company Id Not Present");
		}
	}

	// for fetching employees from list of Department

	public List<EventManagementNameFetchDTO> fetchMultipleDepartmentEmployees(Long companyId, List<String> department) {
		Optional<CompanyInfo> optionalDetails = companyInfoRepository.findById(companyId);
		if (optionalDetails.isPresent()) {
			List<EmployeePersonalInfo> employeePersonalInfoList;
			if (department.isEmpty()) {
				employeePersonalInfoList = employeePersonelInfoRepository.findByCompanyInfoCompanyId(companyId);
			} else {
				employeePersonalInfoList = employeePersonelInfoRepository
						.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoDepartmentIn(companyId, department);

			}

			if (employeePersonalInfoList.isEmpty()) {
				throw new DataNotFoundException("Employees Not Present");
			} else {
				List<EventManagementNameFetchDTO> eventManagementNameFetchdtoList = new ArrayList<>();
				for (EmployeePersonalInfo employeePersonalInfo : employeePersonalInfoList) {
					eventManagementNameFetchdtoList.add(new EventManagementNameFetchDTO(
							employeePersonalInfo.getEmployeeInfoId(), employeePersonalInfo.getFirstName(),
							employeePersonalInfo.getEmployeeOfficialInfo().getEmployeeId()));

				}

				return eventManagementNameFetchdtoList;
			}
		} else {
			throw new DataNotFoundException("Company Id Not Present");
		}
	}

	// for fetching branchId from company
	@Override
	public List<BranchNameFetchDTO> fetchBranchNames(Long companyId) {

		Optional<CompanyInfo> optionalDetails = companyInfoRepository.findById(companyId);
		if (optionalDetails.isPresent()) {

			List<BranchNameFetchDTO> fetchDtoList = new ArrayList<>();

			List<CompanyBranchInfo> companyBranchInfoList = branchInfoRepository.findByCompanyInfoCompanyId(companyId);

			if (!companyBranchInfoList.isEmpty()) {

				for (CompanyBranchInfo companyBranchInfo : companyBranchInfoList) {
					BranchNameFetchDTO fetchDto = new BranchNameFetchDTO();

					fetchDto.setBranchId(companyBranchInfo.getBranchId());
					fetchDto.setBranchName(companyBranchInfo.getBranchName());
					fetchDtoList.add(fetchDto);

				}

				return fetchDtoList;
			} else {
				throw new DataNotFoundException("Branches not Present");
			}
		} else {
			throw new DataNotFoundException("CompanyId Not Present");
		}
	}

// For Deleting the Event Api..

	@Override
	public void deleteEvent(Long eventId) {

		Optional<CompanyEventDetails> eventDetails = companyEventDetailsRepository.findById(eventId);

		if (eventDetails.isPresent()) {
			companyEventDetailsRepository.deleteById(eventId);

		} else {
			throw new DataNotFoundException("Event Not FOund");
		}

	}

// For Editing the Event
	@Override
	public EventManagementEditEventDTO editEvent(EventManagementEditEventDTO editEventdto, Long eventId) {

		Optional<CompanyEventDetails> companyEventDetails = companyEventDetailsRepository.findById(eventId);
		if (companyEventDetails.isPresent()) {
			CompanyEventDetails companyEventDetailsInfo = companyEventDetails.get();
			if (companyEventDetailsInfo.getEventDate().isBefore(LocalDate.now())
					|| (companyEventDetailsInfo.getEventDate().isEqual(LocalDate.now())
							&& companyEventDetailsInfo.getStartTime().isBefore(LocalTime.now()))) {
				throw new PermissionDeniedException(
						"Once the Event is Started or Completed,the Event cannot be Edited");
			} else {
				BeanUtils.copyProperties(editEventdto, companyEventDetailsInfo);
				BeanUtils.copyProperties(companyEventDetailsRepository.save(companyEventDetailsInfo), editEventdto);
				return editEventdto;
			}
		} else {
			throw new DataNotFoundException("Event Not Found");
		}
	}

	// Fetching participants list for selecting Winners Api..

	@Override
	public List<EventManagementParticipantslistDTO> getParticipantsList(Long eventId) {

		Optional<CompanyEventDetails> eventInfoforId = companyEventDetailsRepository.findById(eventId);

		if (eventInfoforId.isPresent()) {
			CompanyEventDetails eventDetails = eventInfoforId.get();
			CompanyInfo companyInfo = eventDetails.getCompanyInfo();
			Long companyId = companyInfo != null ? companyInfo.getCompanyId() : null;
			List<EventManagementParticipantslistDTO> employeelist = new ArrayList<>();
			List<String> employeeIdlist = eventDetails.getEmployees();

			for (String employeeId : employeeIdlist) {
				List<EmployeePersonalInfo> employeeInfo = employeePersonelInfoRepository
						.findByEmployeeOfficialInfoEmployeeIdAndCompanyInfoCompanyId(employeeId, companyId);
				if (!employeeInfo.isEmpty()) {

					EventManagementParticipantslistDTO eventManagementParticipantslistDTO = new EventManagementParticipantslistDTO();
					eventManagementParticipantslistDTO
							.setEmployeeId(employeeInfo.get(0).getEmployeeOfficialInfo().getEmployeeId());
					eventManagementParticipantslistDTO.setEmployeeInfoId(employeeInfo.get(0).getEmployeeInfoId());
					eventManagementParticipantslistDTO.setEmployees(employeeInfo.get(0).getFirstName());
					employeelist.add(eventManagementParticipantslistDTO);
				} else {
					throw new DataNotFoundException("Employee Personal id not Found");
				}

			}

			return employeelist;
		} else {
			throw new DataNotFoundException("Event Id nor Present");
		}

	}

	public void sendMail(List<String> list, Long companyId, EventManagementAddEventDTO eventdto) {
		List<EmployeeOfficialInfo> employees = employeeOfficialInfoRepository
				.findByCompanyBranchInfoCompanyInfoCompanyIdAndEmployeeIdIn(companyId, list);
		if (!employees.isEmpty()) {
			MultipleMailDTO multipleMailDTO = new MultipleMailDTO();
			multipleMailDTO.setBody("Dear Employee,\n\n\tHR Team invites you to participate in "
					+ eventdto.getEventTitle() + " that’s going to be held on " + eventdto.getEventDate()
					+ ".\n\tPlease refer below for the additional information on the same." + "\n\n" + "EVENT: "
					+ eventdto.getEventTitle() + "\n" + "DESCRIPTION: " + eventdto.getEventDescription() + "\n"
					+ "DATE: " + eventdto.getEventDate() + "\n" + "FROM: " + eventdto.getStartTime() + "\n" + "TO: "
					+ eventdto.getEndTime() + "\n\n\n" + "Thanks & Regards," + "\n" + "Team zealhr");
			multipleMailDTO.setSubject("Event Details");
			List<String> mails = employees.stream()
					.filter(EmployeeOfficialInfo -> EmployeeOfficialInfo.getOfficialEmailId() != null)
					.map(EmployeeOfficialInfo::getOfficialEmailId).collect(Collectors.toList());
			multipleMailDTO.setTo(mails);
			eventMailService.sendMultipleMail(multipleMailDTO);
			log.info("successfully mail send");
		}
	}

}
