package com.te.zealhr.service.employee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.zealhr.dto.employee.EmployeeTeamDTO;
import com.te.zealhr.dto.helpandsupport.mongo.TicketHistroy;
import com.te.zealhr.dto.hr.CanlenderRequestDTO;
import com.te.zealhr.dto.hr.mongo.HRTicketsBasicDTO;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.employee.EmployeeReportingInfo;
import com.te.zealhr.entity.helpandsupport.mongo.CompanyAccountTickets;
import com.te.zealhr.entity.helpandsupport.mongo.CompanyAdminDeptTickets;
import com.te.zealhr.entity.helpandsupport.mongo.CompanyHrTickets;
import com.te.zealhr.entity.helpandsupport.mongo.CompanyItTickets;
import com.te.zealhr.exception.employee.DataNotFoundException;
import com.te.zealhr.repository.admindept.CompanyItTicketsRepository;
import com.te.zealhr.repository.employee.EmployeePersonalInfoRepository;
import com.te.zealhr.repository.helpandsupport.mongo.CompanyAccountTicketsRepository;
import com.te.zealhr.repository.helpandsupport.mongo.CompanyAdminDeptTicketsRepo;
import com.te.zealhr.repository.helpandsupport.mongo.CompanyHrTicketsRepository;
import com.te.zealhr.service.hr.CompanyEventDetailsServiceImpl;

@Service
public class RaisedTicketServiceImpl implements RaisedTicketService {

	@Autowired
	private CompanyItTicketsRepository itTicketsRepository;

	@Autowired
	private CompanyAdminDeptTicketsRepo adminDeptTicketsRepository;

	@Autowired
	private CompanyHrTicketsRepository hrTicketsRepository;

	@Autowired
	private CompanyAccountTicketsRepository accountTicketsRepository;

	@Autowired
	private EmployeePersonalInfoRepository employeePersonalInfoRepository;

	@Autowired
	private CompanyEventDetailsServiceImpl companyEventDetailsServiceImpl;

	private static final String RESOLVED = "Resolved";

	@Override
	public List<HRTicketsBasicDTO> getAllRaisedTickets(CanlenderRequestDTO canlenderRequestDTO, Long employeeInfoId,
			Long companyId) {
		Optional<EmployeePersonalInfo> employeePersonalInfoOptional = employeePersonalInfoRepository
				.findByEmployeeInfoIdAndIsActiveTrue(employeeInfoId);
		if (employeePersonalInfoOptional.isEmpty()) {
			throw new DataNotFoundException("Employee Not Found");
		}

		EmployeePersonalInfo employeePersonalInfo = employeePersonalInfoOptional.get();

		List<EmployeePersonalInfo> companyEmployeeList = employeePersonalInfo.getCompanyInfo()
				.getEmployeePersonalInfoList().stream().filter(a -> a.getEmployeeOfficialInfo() != null)
				.collect(Collectors.toList());

		List<LocalDate> startEndDate = companyEventDetailsServiceImpl.getStartEndDate(canlenderRequestDTO.getYear(),
				canlenderRequestDTO.getFiscalMonth());

		List<CompanyItTickets> itTickets = itTicketsRepository.findByCompanyIdAndCreatedByAndCreatedDateBetween(
				companyId, employeeInfoId, startEndDate.get(0).atStartOfDay(),
				startEndDate.get(1).plusDays(1).atStartOfDay());

		List<CompanyAdminDeptTickets> adminTickets = adminDeptTicketsRepository
				.findByCompanyIdAndCreatedByAndCreatedDateBetween(companyId, employeeInfoId,
						startEndDate.get(0).atStartOfDay(), startEndDate.get(1).plusDays(1).atStartOfDay());

		List<CompanyHrTickets> hrTickets = hrTicketsRepository.findByCompanyIdAndCreatedByAndCreatedDateBetween(
				companyId, employeeInfoId, startEndDate.get(0).atStartOfDay(),
				startEndDate.get(1).plusDays(1).atStartOfDay());

		List<CompanyAccountTickets> accountTickets = accountTicketsRepository
				.findByCompanyIdAndCreatedByAndCreatedDateBetween(companyId, employeeInfoId,
						startEndDate.get(0).atStartOfDay(), startEndDate.get(1).plusDays(1).atStartOfDay());

		List<HRTicketsBasicDTO> hRTicketsBasicDTOList = new ArrayList<>();

		hRTicketsBasicDTOList.addAll(getITTicketDTOList(itTickets, companyEmployeeList));

		hRTicketsBasicDTOList.addAll(getAdminTicketDTOList(adminTickets, companyEmployeeList));

		hRTicketsBasicDTOList.addAll(getHRTicketDTOList(hrTickets, companyEmployeeList));

		hRTicketsBasicDTOList.addAll(getAccountTicketDTOList(accountTickets, companyEmployeeList));
		return hRTicketsBasicDTOList;

	}

	@Override
	public List<HRTicketsBasicDTO> getTeamPendingTickets(Long employeeInfoId) {
		Optional<EmployeePersonalInfo> employeePersonalInfoOptional = employeePersonalInfoRepository
				.findById(employeeInfoId);
		if (employeePersonalInfoOptional.isEmpty()) {
			throw new DataNotFoundException("Employee Not Found");
		}
		List<HRTicketsBasicDTO> hrTicketsBasicDTOList = new ArrayList<>();
		List<Long> employeeIds = new ArrayList<>();
		getMyTeam(employeePersonalInfoOptional.get(), employeeIds);
		List<EmployeePersonalInfo> companyEmployeeList = employeePersonalInfoOptional.get().getCompanyInfo()
				.getEmployeePersonalInfoList().stream().filter(a -> a.getEmployeeOfficialInfo() != null)
				.collect(Collectors.toList());
		hrTicketsBasicDTOList.addAll(getHRTicketDTOList(
				hrTicketsRepository.findByCreatedByInAndTicketHistroysStatusNotIn(employeeIds, RESOLVED),
				companyEmployeeList));
		hrTicketsBasicDTOList.addAll(getITTicketDTOList(
				itTicketsRepository.findByCreatedByInAndTicketHistroysStatusNotIn(employeeIds, RESOLVED),
				companyEmployeeList));
		hrTicketsBasicDTOList.addAll(getAccountTicketDTOList(
				accountTicketsRepository.findByCreatedByInAndTicketHistroysStatusNotIn(employeeIds, RESOLVED),
				companyEmployeeList));
		hrTicketsBasicDTOList.addAll(getAdminTicketDTOList(
				adminDeptTicketsRepository.findByCreatedByInAndTicketHistroysStatusNotIn(employeeIds, RESOLVED),
				companyEmployeeList));
		return hrTicketsBasicDTOList;
	}

	private List<EmployeeTeamDTO> getMyTeam(EmployeePersonalInfo employeePersonalInfo, List<Long> employeeIds) {
		List<EmployeeReportingInfo> employeeReportingInfoList = employeePersonalInfo.getEmployeeReportingInfoList();
		List<EmployeeTeamDTO> employeeTeamDTOList = new ArrayList<>();
		for (EmployeeReportingInfo employeeReportingInfo : employeeReportingInfoList) {
			getTeamDTO(employeeReportingInfo, employeeIds);
		}
		return employeeTeamDTOList;
	}

	private void getTeamDTO(EmployeeReportingInfo employeeReportingInfo, List<Long> employeeIds) {
		EmployeePersonalInfo employeePersonalInfo = employeeReportingInfo.getEmployeePersonalInfo();
		employeeIds.add(employeePersonalInfo.getEmployeeInfoId());
		List<EmployeeReportingInfo> employeeReportingInfoList = employeePersonalInfo.getEmployeeReportingInfoList();
		for (EmployeeReportingInfo employeeReporting : employeeReportingInfoList) {
			getTeamDTO(employeeReporting, employeeIds);
		}
	}

	private List<HRTicketsBasicDTO> getHRTicketDTOList(List<CompanyHrTickets> hrTickets,
			List<EmployeePersonalInfo> companyEmployeeList) {
		return hrTickets.stream().map(ticket -> {
			List<TicketHistroy> ticketHistroy = ticket.getTicketHistroys();
			Optional<EmployeePersonalInfo> employeePersonalInfoOptional = companyEmployeeList.stream()
					.filter(employee -> employee.getEmployeeInfoId().equals(ticket.getCreatedBy())).findAny();
			return HRTicketsBasicDTO.builder().category(ticket.getCategory())
					.raisedDate(ticket.getCreatedDate().toLocalDate()).ticketObjectId(ticket.getTicketObjectId())
					.hrTicketId(ticket.getTicketId())
					.status((ticketHistroy != null && !ticketHistroy.isEmpty())
							? ticketHistroy.get(ticketHistroy.size() - 1).getStatus()
							: null)
					.category(ticket.getCategory())
					.ticketOwner(
							employeePersonalInfoOptional.isPresent() ? employeePersonalInfoOptional.get().getFirstName()
									: null)
					.description(ticket.getDescription()).department("HR").build();
		}).collect(Collectors.toList());
	}

	private List<HRTicketsBasicDTO> getITTicketDTOList(List<CompanyItTickets> itTickets,
			List<EmployeePersonalInfo> companyEmployeeList) {
		return itTickets.stream().map(ticket -> {
			List<TicketHistroy> ticketHistroy = ticket.getTicketHistroys();
			Optional<EmployeePersonalInfo> employeePersonalInfoOptional = companyEmployeeList.stream()
					.filter(employee -> employee.getEmployeeInfoId().equals(ticket.getCreatedBy())).findAny();
			return HRTicketsBasicDTO.builder().category(ticket.getCategory())
					.raisedDate(ticket.getCreatedDate().toLocalDate()).ticketObjectId(ticket.getId())
					.hrTicketId(ticket.getTicketId())
					.status((ticketHistroy != null && !ticketHistroy.isEmpty())
							? ticketHistroy.get(ticketHistroy.size() - 1).getStatus()
							: null)
					.category(ticket.getCategory())
					.ticketOwner(
							employeePersonalInfoOptional.isPresent() ? employeePersonalInfoOptional.get().getFirstName()
									: null)
					.description(ticket.getDescription()).department("IT").build();
		}).collect(Collectors.toList());
	}

	private List<HRTicketsBasicDTO> getAccountTicketDTOList(List<CompanyAccountTickets> accountTickets,
			List<EmployeePersonalInfo> companyEmployeeList) {
		return accountTickets.stream().map(ticket -> {
			List<TicketHistroy> ticketHistroy = ticket.getTicketHistroys();
			Optional<EmployeePersonalInfo> employeePersonalInfoOptional = companyEmployeeList.stream()
					.filter(employee -> employee.getEmployeeInfoId().equals(ticket.getCreatedBy())).findAny();
			return HRTicketsBasicDTO.builder().category(ticket.getCategory())
					.raisedDate(ticket.getCreatedDate().toLocalDate()).ticketObjectId(ticket.getObjectTicketId())
					.hrTicketId(ticket.getTicketId())
					.status((ticketHistroy != null && !ticketHistroy.isEmpty())
							? ticketHistroy.get(ticketHistroy.size() - 1).getStatus()
							: null)
					.category(ticket.getCategory())
					.ticketOwner(
							employeePersonalInfoOptional.isPresent() ? employeePersonalInfoOptional.get().getFirstName()
									: null)
					.description(ticket.getDescription()).department("ACCOUNT").build();
		}).collect(Collectors.toList());
	}

	private List<HRTicketsBasicDTO> getAdminTicketDTOList(List<CompanyAdminDeptTickets> adminTickets,
			List<EmployeePersonalInfo> companyEmployeeList) {
		return adminTickets.stream().map(ticket -> {
			List<TicketHistroy> ticketHistroy = ticket.getTicketHistroys();
			Optional<EmployeePersonalInfo> employeePersonalInfoOptional = companyEmployeeList.stream()
					.filter(employee -> employee.getEmployeeInfoId().equals(ticket.getCreatedBy())).findAny();
			return HRTicketsBasicDTO.builder().category(ticket.getCategory())
					.raisedDate(ticket.getCreatedDate().toLocalDate()).ticketObjectId(ticket.getObjectTicketId())
					.hrTicketId(ticket.getTicketId())
					.status((ticketHistroy != null && !ticketHistroy.isEmpty())
							? ticketHistroy.get(ticketHistroy.size() - 1).getStatus()
							: null)
					.category(ticket.getCategory())
					.ticketOwner(
							employeePersonalInfoOptional.isPresent() ? employeePersonalInfoOptional.get().getFirstName()
									: null)
					.description(ticket.getDescription()).department("ADMIN DEPARTMENT").build();
		}).collect(Collectors.toList());
	}

}
