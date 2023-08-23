package com.te.zealhr.service.admin;

import static com.te.zealhr.common.hr.HrConstants.OVERALL_FEEDBACK;
import static com.te.zealhr.common.hr.HrConstants.REJECTED;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.zealhr.dto.DashboardDTO;
import com.te.zealhr.dto.DashboardRequestDTO;
import com.te.zealhr.dto.DashboardResponseDTO;
import com.te.zealhr.dto.TicketDetailsDTO;
import com.te.zealhr.dto.account.mongo.CompanyAccountTicketsDTO;
import com.te.zealhr.dto.helpandsupport.mongo.CompanyTicketDto;
import com.te.zealhr.dto.helpandsupport.mongo.TicketHistroy;
import com.te.zealhr.dto.hr.EmployeeDisplayDetailsDTO;
import com.te.zealhr.dto.hr.mongo.HRTicketsBasicDTO;
import com.te.zealhr.entity.admin.CompanyInfo;
import com.te.zealhr.entity.admin.CompanyPayrollInfo;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.employee.EmployeeSalaryDetails;
import com.te.zealhr.entity.employee.mongo.AttendanceDetails;
import com.te.zealhr.entity.employee.mongo.EmployeeAttendanceDetails;
import com.te.zealhr.entity.helpandsupport.mongo.CompanyAccountTickets;
import com.te.zealhr.entity.helpandsupport.mongo.CompanyAdminDeptTickets;
import com.te.zealhr.entity.helpandsupport.mongo.CompanyHrTickets;
import com.te.zealhr.entity.helpandsupport.mongo.CompanyItTickets;
import com.te.zealhr.entity.hr.CandidateInfo;
import com.te.zealhr.entity.hr.CandidateInterviewInfo;
import com.te.zealhr.exception.CompanyIdNotFoundException;
import com.te.zealhr.exception.DataNotFoundException;
import com.te.zealhr.exception.employee.EmployeeNotFoundException;
import com.te.zealhr.repository.admin.CompanyInfoRepository;
import com.te.zealhr.repository.admindept.CompanyItTicketsRepository;
import com.te.zealhr.repository.employee.EmployeePersonalInfoRepository;
import com.te.zealhr.repository.employee.EmployeeSalaryDetailsRepository;
import com.te.zealhr.repository.employee.mongo.EmployeeAttendanceDetailsRepository;
import com.te.zealhr.repository.helpandsupport.mongo.CompanyAccountTicketsRepository;
import com.te.zealhr.repository.helpandsupport.mongo.CompanyAdminDeptTicketsRepo;
import com.te.zealhr.repository.helpandsupport.mongo.CompanyHrTicketsRepository;
import com.te.zealhr.service.account.AccountDashboardService;
import com.te.zealhr.service.admindept.AdminDeptDashboardService;
import com.te.zealhr.service.hr.HRDashboardService;
import com.te.zealhr.service.it.ITDashboardService;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

	@Autowired
	private ITDashboardService itDashboardService;

	@Autowired
	private AccountDashboardService accountDashboardService;

	@Autowired
	private AdminDeptDashboardService adminDeptDashboardService;

	@Autowired
	private HRDashboardService hrDashboardService;

	@Autowired
	private EmployeeSalaryDetailsRepository employeeSalaryDetailsRepository;

	@Autowired
	private CompanyAccountTicketsRepository accountTicketsRepository;

	@Autowired
	private CompanyItTicketsRepository companyItTicketsRepository;

	@Autowired
	private CompanyHrTicketsRepository companyHrTicketsRepository;

	@Autowired
	private CompanyAdminDeptTicketsRepo companyAdminDeptTicketsRepo;

	@Autowired
	private EmployeePersonalInfoRepository employeeInfoRepository;

	@Autowired
	private EmployeeAttendanceDetailsRepository employeeAttendanceDetailsRepository;

	@Autowired
	CompanyInfoRepository infoRepository;

	private static final String EMPLOYEE_NOT_FOUND = "Employee Not Found";

	private static final String DEPARTMENT_NOT_FOUND = "Department Not Found";

	private static final String TICKET_NOT_FOUND = "Ticket Not Found";

	@Override
	public DashboardResponseDTO getTicketDetails(DashboardRequestDTO dashboardRequestDTO, Long companyId) {
		switch (dashboardRequestDTO.getType()) {
		case "IT":
			return itDashboardService.getTicketDetails(dashboardRequestDTO, companyId);
		case "ACCOUNT":
			return accountDashboardService.getTicketDetails(dashboardRequestDTO, companyId);
		case "HR":
			return hrDashboardService.getTicketDetails(dashboardRequestDTO, companyId);
		case "ADMIN DEPARTMENT":
			return adminDeptDashboardService.getTicketDetails(dashboardRequestDTO, companyId);
		default:
			throw new DataNotFoundException(DEPARTMENT_NOT_FOUND);
		}
	}

	@Override
	public List<HRTicketsBasicDTO> getTicketDetailsByStatus(DashboardRequestDTO dashboardRequestDTO, Long companyId) {
		switch (dashboardRequestDTO.getDepartment()) {
		case "IT":
			return itDashboardService.getTicketDetailsByStatus(companyId, dashboardRequestDTO.getType(),
					dashboardRequestDTO.getFilterValue());
		case "ACCOUNT":
			return accountDashboardService.getTicketDetailsByStatus(companyId, dashboardRequestDTO.getType(),
					dashboardRequestDTO.getFilterValue());
		case "HR":
			return hrDashboardService.getTicketDetailsByStatus(companyId, dashboardRequestDTO.getType(),
					dashboardRequestDTO.getFilterValue());
		case "ADMIN DEPARTMENT":
			return adminDeptDashboardService.getTicketDetailsByStatus(companyId, dashboardRequestDTO.getType(),
					dashboardRequestDTO.getFilterValue());
		default:
			throw new DataNotFoundException(DEPARTMENT_NOT_FOUND);
		}
	}

	@Override
	public DashboardResponseDTO getSalaryDetails(DashboardRequestDTO dashboardRequestDTO, Long companyId) {

		List<EmployeeSalaryDetails> salaryDetailsForGraph = employeeSalaryDetailsRepository
				.findByCompanyInfoCompanyIdAndMonthAndYear(companyId, dashboardRequestDTO.getMonth(),
						dashboardRequestDTO.getYear());
		List<DashboardDTO> valuesForCards = new ArrayList<>();
		valuesForCards.add(DashboardDTO.builder().type("Finalized")
				.count(salaryDetailsForGraph.stream().filter(EmployeeSalaryDetails::getIsFinalized).count()).build());
		valuesForCards.add(DashboardDTO.builder().type("Paid")
				.count(salaryDetailsForGraph.stream().filter(EmployeeSalaryDetails::getIsPaid).count()).build());
		valuesForCards.add(DashboardDTO.builder().type("Payslip Generated")
				.count(salaryDetailsForGraph.stream().filter(EmployeeSalaryDetails::getIsPayslipGenerated).count())
				.build());

		return DashboardResponseDTO.builder().cardValues(valuesForCards).build();
	}

	@Override
	public TicketDetailsDTO getTicketDetailsById(DashboardRequestDTO dashboardRequestDTO, String objectTicketId,
			Long companyId) {
		switch (dashboardRequestDTO.getDepartment()) {
		case "IT":
			return getITTicketDTO(companyId, objectTicketId);
		case "ACCOUNT":
			return getAccountTicketDTO(companyId, objectTicketId);
		case "HR":
			return getHRTicketDTO(companyId, objectTicketId);
		case "ADMIN DEPARTMENT":
			return getAdminDeptTicketDTO(companyId, objectTicketId);
		default:
			throw new DataNotFoundException(DEPARTMENT_NOT_FOUND);
		}
	}

	TicketDetailsDTO getAccountTicketDTO(Long companyId, String objectTicketId) {
		TicketDetailsDTO ticketDetailsDTO = new TicketDetailsDTO();
		CompanyAccountTickets accountTickets = accountTicketsRepository
				.findByCompanyIdAndObjectTicketId(companyId, objectTicketId)
				.orElseThrow(() -> new DataNotFoundException("Account tickets details are not found"));

		BeanUtils.copyProperties(accountTickets, ticketDetailsDTO);

		ticketDetailsDTO.setObjectTicketId(objectTicketId);

		List<EmployeePersonalInfo> employee = employeeInfoRepository
				.findByEmployeeOfficialInfoEmployeeIdAndCompanyInfoCompanyId(accountTickets.getEmployeeId(), companyId);

		ticketDetailsDTO.setEmployeeName(employee.isEmpty() ? null : employee.get(0).getFirstName());
		ticketDetailsDTO.setType(accountTickets.getSubCategory());

		List<TicketHistroy> ticketHistroys = accountTickets.getTicketHistroys();
		List<Long> employeeInfoId = ticketHistroys.stream().map(TicketHistroy::getBy).collect(Collectors.toList());
		employeeInfoId.add(accountTickets.getCreatedBy());

		List<EmployeePersonalInfo> employeeInfoList = employeeInfoRepository.findAllById(employeeInfoId);
		List<EmployeePersonalInfo> ticketOwner = employeeInfoList.stream()
				.filter(e -> e.getEmployeeInfoId().equals(accountTickets.getCreatedBy())).collect(Collectors.toList());
		ticketDetailsDTO.setTicketOwner(ticketOwner.isEmpty() ? null : ticketOwner.get(0).getFirstName());
		for (TicketHistroy ticketHistroy : ticketHistroys) {
			for (EmployeePersonalInfo employeeInfo : employeeInfoList) {
				if (employeeInfo.getEmployeeInfoId().equals(ticketHistroy.getBy())) {
					ticketHistroy.setEmployeeId(employeeInfo.getEmployeeOfficialInfo().getEmployeeId());
					ticketHistroy.setEmployeeName(employeeInfo.getFirstName());
				}

			}
		}
		ticketDetailsDTO.setQuestionAnswer(accountTickets.getQuestionAnswer());
		ticketDetailsDTO.setDepartment("ACCOUNT");
		return ticketDetailsDTO;
	}

	TicketDetailsDTO getHRTicketDTO(Long companyId, String objectTicketId) {
		TicketDetailsDTO ticketDetailsDTO = new TicketDetailsDTO();
		CompanyHrTickets hrTickets = companyHrTicketsRepository.findById(objectTicketId)
				.orElseThrow(() -> new DataNotFoundException("HR tickets details are not found"));

		BeanUtils.copyProperties(hrTickets, ticketDetailsDTO);

		ticketDetailsDTO.setObjectTicketId(objectTicketId);

		List<EmployeePersonalInfo> employee = employeeInfoRepository
				.findByEmployeeOfficialInfoEmployeeIdAndCompanyInfoCompanyId(hrTickets.getEmployeeId(), companyId);

		ticketDetailsDTO.setEmployeeName(employee.isEmpty() ? null : employee.get(0).getFirstName());
		ticketDetailsDTO.setType(hrTickets.getSubCategory());

		List<TicketHistroy> ticketHistroys = hrTickets.getTicketHistroys();
		List<Long> employeeInfoId = ticketHistroys.stream().map(TicketHistroy::getBy).collect(Collectors.toList());
		employeeInfoId.add(hrTickets.getCreatedBy());

		List<EmployeePersonalInfo> employeeInfoList = employeeInfoRepository.findAllById(employeeInfoId);
		List<EmployeePersonalInfo> ticketOwner = employeeInfoList.stream()
				.filter(e -> e.getEmployeeInfoId().equals(hrTickets.getCreatedBy())).collect(Collectors.toList());
		ticketDetailsDTO.setTicketOwner(ticketOwner.isEmpty() ? null : ticketOwner.get(0).getFirstName());
		for (TicketHistroy ticketHistroy : ticketHistroys) {
			for (EmployeePersonalInfo employeeInfo : employeeInfoList) {
				if (employeeInfo.getEmployeeInfoId().equals(ticketHistroy.getBy())) {
					ticketHistroy.setEmployeeId(employeeInfo.getEmployeeOfficialInfo().getEmployeeId());
					ticketHistroy.setEmployeeName(employeeInfo.getFirstName());
				}

			}
		}
		ticketDetailsDTO.setQuestionAnswer(hrTickets.getQuestionAnswer());
		ticketDetailsDTO.setDepartment("HR");
		return ticketDetailsDTO;
	}

	TicketDetailsDTO getAdminDeptTicketDTO(Long companyId, String objectTicketId) {
		TicketDetailsDTO ticketDetailsDTO = new TicketDetailsDTO();
		CompanyAdminDeptTickets adminDeptTickets = companyAdminDeptTicketsRepo.findById(objectTicketId)
				.orElseThrow(() -> new DataNotFoundException("Admin Department tickets details are not found"));

		BeanUtils.copyProperties(adminDeptTickets, ticketDetailsDTO);

		ticketDetailsDTO.setObjectTicketId(objectTicketId);

		List<EmployeePersonalInfo> employee = employeeInfoRepository
				.findByEmployeeOfficialInfoEmployeeIdAndCompanyInfoCompanyId(adminDeptTickets.getEmployeeId(),
						companyId);

		ticketDetailsDTO.setEmployeeName(employee.isEmpty() ? null : employee.get(0).getFirstName());
		ticketDetailsDTO.setType(adminDeptTickets.getSubCategory());

		List<TicketHistroy> ticketHistroys = adminDeptTickets.getTicketHistroys();
		List<Long> employeeInfoId = ticketHistroys.stream().map(TicketHistroy::getBy).collect(Collectors.toList());
		employeeInfoId.add(adminDeptTickets.getCreatedBy());

		List<EmployeePersonalInfo> employeeInfoList = employeeInfoRepository.findAllById(employeeInfoId);
		List<EmployeePersonalInfo> ticketOwner = employeeInfoList.stream()
				.filter(e -> e.getEmployeeInfoId().equals(adminDeptTickets.getCreatedBy()))
				.collect(Collectors.toList());
		ticketDetailsDTO.setTicketOwner(ticketOwner.isEmpty() ? null : ticketOwner.get(0).getFirstName());
		for (TicketHistroy ticketHistroy : ticketHistroys) {
			for (EmployeePersonalInfo employeeInfo : employeeInfoList) {
				if (employeeInfo.getEmployeeInfoId().equals(ticketHistroy.getBy())) {
					ticketHistroy.setEmployeeId(employeeInfo.getEmployeeOfficialInfo().getEmployeeId());
					ticketHistroy.setEmployeeName(employeeInfo.getFirstName());
				}

			}
		}
		ticketDetailsDTO.setQuestionAnswer(adminDeptTickets.getQuestionAnswer());
		ticketDetailsDTO.setDepartment("ADMIN DEPARTMENT");
		return ticketDetailsDTO;
	}

	TicketDetailsDTO getITTicketDTO(Long companyId, String objectTicketId) {
		TicketDetailsDTO ticketDetailsDTO = new TicketDetailsDTO();
		CompanyItTickets itDeptTickets = companyItTicketsRepository.findById(objectTicketId)
				.orElseThrow(() -> new DataNotFoundException("It tickets details are not found"));

		BeanUtils.copyProperties(itDeptTickets, ticketDetailsDTO);
		ticketDetailsDTO.setObjectTicketId(objectTicketId);
		ticketDetailsDTO.setUniqueNumber(itDeptTickets.getIdentificationNumber());

		List<EmployeePersonalInfo> employee = employeeInfoRepository
				.findByEmployeeOfficialInfoEmployeeIdAndCompanyInfoCompanyId(itDeptTickets.getEmployeeId(), companyId);

		ticketDetailsDTO.setEmployeeName(employee.isEmpty() ? null : employee.get(0).getFirstName());
		ticketDetailsDTO.setType(itDeptTickets.getSubCategory());

		List<TicketHistroy> ticketHistroys = itDeptTickets.getTicketHistroys();
		List<Long> employeeInfoId = ticketHistroys.stream().map(TicketHistroy::getBy).collect(Collectors.toList());
		employeeInfoId.add(itDeptTickets.getCreatedBy());

		List<EmployeePersonalInfo> employeeInfoList = employeeInfoRepository.findAllById(employeeInfoId);
		List<EmployeePersonalInfo> ticketOwner = employeeInfoList.stream()
				.filter(e -> e.getEmployeeInfoId().equals(itDeptTickets.getCreatedBy())).collect(Collectors.toList());
		ticketDetailsDTO.setTicketOwner(ticketOwner.isEmpty() ? null : ticketOwner.get(0).getFirstName());
		for (TicketHistroy ticketHistroy : ticketHistroys) {
			for (EmployeePersonalInfo employeeInfo : employeeInfoList) {
				if (employeeInfo.getEmployeeInfoId().equals(ticketHistroy.getBy())) {
					ticketHistroy.setEmployeeId(employeeInfo.getEmployeeOfficialInfo().getEmployeeId());
					ticketHistroy.setEmployeeName(employeeInfo.getFirstName());
				}

			}
		}
		ticketDetailsDTO.setQuestionAnswer(itDeptTickets.getQuestionAnswer());
		ticketDetailsDTO.setDepartment("IT");
		return ticketDetailsDTO;
	}

	@Override
	public CompanyTicketDto getQuestionAndAnswer(CompanyTicketDto updateTicketDTO, Long companyId) {
		switch (updateTicketDTO.getDepartment()) {
		case "HR":
			CompanyHrTickets companyHrTickets = companyHrTicketsRepository.findById(updateTicketDTO.getId())
					.orElseThrow(() -> new DataNotFoundException(TICKET_NOT_FOUND));
			BeanUtils.copyProperties(companyHrTickets, updateTicketDTO);
			break;

		case "ADMIN DEPARTMENT":
			CompanyAdminDeptTickets adminDeptTickets = companyAdminDeptTicketsRepo.findById(updateTicketDTO.getId())
					.orElseThrow(() -> new DataNotFoundException(TICKET_NOT_FOUND));
			BeanUtils.copyProperties(adminDeptTickets, updateTicketDTO);
			break;
		case "ACCOUNT":
			CompanyAccountTickets accountTickets = accountTicketsRepository.findById(updateTicketDTO.getId())
					.orElseThrow(() -> new DataNotFoundException(TICKET_NOT_FOUND));
			BeanUtils.copyProperties(accountTickets, updateTicketDTO);
			break;
		case "IT":
			CompanyItTickets companyItTickets = companyItTicketsRepository.findById(updateTicketDTO.getId())
					.orElseThrow(() -> new DataNotFoundException(TICKET_NOT_FOUND));
			BeanUtils.copyProperties(companyItTickets, updateTicketDTO);
			break;
		default:
			throw new DataNotFoundException(TICKET_NOT_FOUND);

		}
		return updateTicketDTO;
	}

	@Override
	public CompanyTicketDto updateTicketRemarks(CompanyTicketDto updateTicketDTO, Long companyId) {

		switch (updateTicketDTO.getDepartment().toUpperCase()) {
		case "HR":
			CompanyHrTickets hrTicket = companyHrTicketsRepository.findById(updateTicketDTO.getId())
					.orElseThrow(() -> new DataNotFoundException(TICKET_NOT_FOUND));
			hrTicket.setQuestionAnswer(updateTicketDTO.getQuestionAnswer());
			companyHrTicketsRepository.save(hrTicket);
			break;
		case "IT":
			CompanyItTickets itTickets = companyItTicketsRepository.findById(updateTicketDTO.getId())
					.orElseThrow(() -> new DataNotFoundException(TICKET_NOT_FOUND));
			itTickets.setQuestionAnswer(updateTicketDTO.getQuestionAnswer());
			companyItTicketsRepository.save(itTickets);
			break;
		case "ADMIN":
		case "ADMIN DEPARTMENT":
			CompanyAdminDeptTickets adminTickets = companyAdminDeptTicketsRepo.findById(updateTicketDTO.getId())
					.orElseThrow(() -> new DataNotFoundException(TICKET_NOT_FOUND));
			adminTickets.setQuestionAnswer(updateTicketDTO.getQuestionAnswer());
			companyAdminDeptTicketsRepo.save(adminTickets);
			break;
		case "ACCOUNT":
			CompanyAccountTickets accountTickets = accountTicketsRepository.findById(updateTicketDTO.getId())
					.orElseThrow(() -> new DataNotFoundException(TICKET_NOT_FOUND));
			accountTickets.setQuestionAnswer(updateTicketDTO.getQuestionAnswer());
			accountTicketsRepository.save(accountTickets);
			break;

		default:
			throw new DataNotFoundException(DEPARTMENT_NOT_FOUND);
		}
		return updateTicketDTO;

	}

	@Override
	public DashboardResponseDTO getAttendanceDetails(DashboardRequestDTO dashboardRequestDTO, Long companyId) {
		List<EmployeePersonalInfo> employeeList = employeeInfoRepository
				.findByCompanyInfoCompanyIdAndIsActiveTrue(companyId);
		return DashboardResponseDTO.builder()
				.cardValues(getEmployeeCounts(employeeList, companyId, "Cards", LocalDate.now()))
				.graphValues(getEmployeeCounts(employeeList, companyId, "Graph", dashboardRequestDTO.getStartDate()))
				.build();
	}

	private List<DashboardDTO> getEmployeeCounts(List<EmployeePersonalInfo> employees, Long companyId, String type,
			LocalDate date) {
		List<EmployeeAttendanceDetails> employeeAttendanceDetailsList = employeeAttendanceDetailsRepository
				.findByCompanyIdAndMonthNoAndYear(companyId, date.getMonthValue(), date.getYear());
		List<DashboardDTO> values = new ArrayList<>();
		Long presentCount = 0l;
		presentCount += employeeAttendanceDetailsList.stream()
				.filter(attendanceDetails -> attendanceDetails.getAttendanceDetails() != null
						&& !(attendanceDetails.getAttendanceDetails().stream()
								.filter(attendance -> attendance.getPunchIn() != null
										&& attendance.getPunchIn().toLocalDate().equals(date)
										&& (attendance.getIsInsideLocation() || (attendance.getStatus() != null
												&& attendance.getStatus().containsKey("Present"))))
								.collect(Collectors.toList()).isEmpty()))
				.count();
		Long totalCount = Long.valueOf(employees.size());
		if (type.equalsIgnoreCase("Cards")) {
			values.add(DashboardDTO.builder().type("Active").count(totalCount).build());
		}
		values.add(DashboardDTO.builder().type("Present").count(presentCount).build());
		values.add(DashboardDTO.builder().type("Absent").count(Math.subtractExact(totalCount, presentCount)).build());
		return values;
	}

	public List<EmployeeDisplayDetailsDTO> getEmployeeDetailsByStatus(Long companyId, String type) {
		LocalDate date = LocalDate.now();
		List<EmployeeDisplayDetailsDTO> employeeDisplayList = new ArrayList<>();
		List<EmployeeDisplayDetailsDTO> employeeList = employeeInfoRepository.getActiveEmployeeDetails(companyId);
		switch (type.toUpperCase()) {
		case "ACTIVE": {
			return employeeList;
		}
		case "PRESENT": {
			List<EmployeeAttendanceDetails> employeeAttendanceDetailsList = employeeAttendanceDetailsRepository
					.findByCompanyIdAndMonthNoAndYear(companyId, date.getMonthValue(), date.getYear());
			employeeAttendanceDetailsList.stream()
					.filter(attendanceDetails -> attendanceDetails.getAttendanceDetails() != null
							&& !(attendanceDetails.getAttendanceDetails().stream()
									.filter(attendance -> attendance.getPunchIn() != null
											&& attendance.getPunchIn().toLocalDate().equals(date)
											&& (attendance.getIsInsideLocation() || (attendance.getStatus() != null
													&& attendance.getStatus().containsKey("Present"))))
									.collect(Collectors.toList()).isEmpty()))
					.forEach(attendance -> {
						Optional<EmployeeDisplayDetailsDTO> employeeDetails = employeeList.stream()
								.filter(employee -> attendance.getEmployeeInfoId().equals(employee.getEmployeeId()))
								.findAny();
						if (employeeDetails.isPresent()) {
							employeeDisplayList.add(employeeDetails.get());
						}
					});
			return employeeDisplayList;
		}
		case "ABSENT": {
			List<EmployeeAttendanceDetails> employeeAttendanceDetailsList = employeeAttendanceDetailsRepository
					.findByCompanyIdAndMonthNoAndYear(companyId, date.getMonthValue(), date.getYear());
			employeeAttendanceDetailsList.stream()
					.filter(attendanceDetails -> attendanceDetails.getAttendanceDetails() != null
							&& !(attendanceDetails.getAttendanceDetails().stream()
									.filter(attendance -> attendance.getPunchIn() != null
											&& attendance.getPunchIn().toLocalDate().equals(date)
											&& (attendance.getIsInsideLocation() || (attendance.getStatus() != null
													&& attendance.getStatus().containsKey("Present"))))
									.collect(Collectors.toList()).isEmpty()))
					.forEach(attendance -> {
						Optional<EmployeeDisplayDetailsDTO> employeeDetails = employeeList.stream()
								.filter(employee -> attendance.getEmployeeInfoId().equals(employee.getEmployeeId()))
								.findAny();
						if (employeeDetails.isPresent()) {
							employeeDisplayList.add(employeeDetails.get());
						}
					});
			employeeList.removeAll(employeeDisplayList);
			return employeeList;
		}
		default:
			throw new DataNotFoundException("Type Not Found");
		}

	}

}
