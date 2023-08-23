package com.te.zealhr.service.helpandsupport.mongo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.zealhr.beancopy.BeanCopy;
import com.te.zealhr.constants.admin.AdminConstants;
import com.te.zealhr.dto.helpandsupport.mongo.CompanyAdminDeptTicketsResponseDto;
import com.te.zealhr.dto.helpandsupport.mongo.CompanyadminDeptTicketsDto;
import com.te.zealhr.dto.helpandsupport.mongo.TicketHistroy;
import com.te.zealhr.dto.helpandsupport.mongo.TicketHistroyDto;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.helpandsupport.mongo.CompanyAccountTickets;
import com.te.zealhr.entity.helpandsupport.mongo.CompanyAdminDeptTickets;
import com.te.zealhr.entity.helpandsupport.mongo.CompanyHrTickets;
import com.te.zealhr.entity.helpandsupport.mongo.CompanyItTickets;
import com.te.zealhr.exception.admin.EmployeeNotFoundException;
import com.te.zealhr.exception.admin.NoEmployeePresentException;
import com.te.zealhr.exception.admin.NoTicketFoundException;
import com.te.zealhr.repository.admindept.CompanyItTicketsRepository;
import com.te.zealhr.repository.employee.EmployeePersonalInfoRepository;
import com.te.zealhr.repository.helpandsupport.mongo.CompanyAccountTicketsRepository;
import com.te.zealhr.repository.helpandsupport.mongo.CompanyAdminDeptTicketsRepo;
import com.te.zealhr.repository.helpandsupport.mongo.CompanyHrTicketsRepository;
import com.te.zealhr.service.notification.employee.InAppNotificationServiceImpl;
import com.te.zealhr.service.notification.employee.PushNotificationService;

@Service
public class CompanyAdminDeptTicketsServiceImpl implements CompanyAdminDeptTicketsService {

	@Autowired
	CompanyAdminDeptTicketsRepo companyAdminDeptTicketsRepo;

	@Autowired
	EmployeePersonalInfoRepository employeePersonalInfoRepository;

	@Autowired
	private CompanyAccountTicketsRepository companyAccountTicketsRepository;

	@Autowired
	private CompanyItTicketsRepository companyItTicketsRepository;

	@Autowired
	private CompanyHrTicketsRepository companyHrTicketsRepository;

	@Autowired
	private InAppNotificationServiceImpl notificationServiceImpl;

	@Autowired
	private PushNotificationService pushNotificationService;

	@Override
	public boolean createTickets(CompanyadminDeptTicketsDto companyAdminDeptTicketsDto) {

		CompanyAdminDeptTickets companyAdminDeptTickets = new CompanyAdminDeptTickets();
		BeanUtils.copyProperties(companyAdminDeptTicketsDto, companyAdminDeptTickets);
		List<TicketHistroy> ticketHistoryList = new ArrayList<>();
		for (TicketHistroyDto ticketHistroyDto : companyAdminDeptTicketsDto.getTicketHistroys()) {
			TicketHistroy ticketHistroy = new TicketHistroy();
			BeanUtils.copyProperties(ticketHistroyDto, ticketHistroy);
			ticketHistoryList.add(ticketHistroy);
		}
		companyAdminDeptTickets.setTicketHistroys(ticketHistoryList);

		CompanyAdminDeptTickets save = companyAdminDeptTicketsRepo.save(companyAdminDeptTickets);
		return Optional.ofNullable(save).isPresent();

	}

	@Override
	public List<CompanyAdminDeptTicketsResponseDto> getAllTickets(Long companyId) {

		List<CompanyAdminDeptTickets> allTickets = companyAdminDeptTicketsRepo.findByCompanyId(companyId);
		if (allTickets.isEmpty()) {
			throw new NoTicketFoundException(AdminConstants.NO_TICKETS_FOUND);
		}

		return duplicateCode(allTickets, companyId);
	}

	@Override
	public List<CompanyAdminDeptTicketsResponseDto> getAllTicketsAccordingStatus(Long companyId, String status) {

		List<CompanyAdminDeptTickets> allTickets = companyAdminDeptTicketsRepo
				.findByCompanyIdAndTicketHistroysStatusIgnoreCase(companyId, status);

		Map<String, Set<Long>> ownerIds = new HashMap<>();
		for (CompanyAdminDeptTickets companyAdminDeptTickets : allTickets) {
			Set<Long> idset = new HashSet<>();
			for (TicketHistroy ticketHistroy : companyAdminDeptTickets.getTicketHistroys()) {
				if (ticketHistroy.getStatus().equalsIgnoreCase(status)) {
					idset.add(ticketHistroy.getBy());
				}
			}
			ownerIds.put(companyAdminDeptTickets.getEmployeeId(), idset);
		}

		if (allTickets.isEmpty())
			return new ArrayList<>();

		List<Long> collect = ownerIds.entrySet().stream().map(Entry::getValue).flatMap(Collection::stream)
				.collect(Collectors.toList());

		List<EmployeePersonalInfo> employees = employeePersonalInfoRepository
				.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoEmployeeIdIn(companyId,
						ownerIds.entrySet().stream().map(Entry::getKey).collect(Collectors.toList()))
				.orElseGet(Collections::emptyList);

		List<EmployeePersonalInfo> ownersInfo = employeePersonalInfoRepository
				.findByCompanyInfoCompanyIdAndEmployeeInfoIdIn(companyId, collect).orElseGet(Collections::emptyList);

		return allTickets.stream().filter(xcc -> xcc.getTicketHistroys() != null
				&& xcc.getTicketHistroys().get(xcc.getTicketHistroys().size() - 1).getStatus().equalsIgnoreCase(status))
				.map(x -> {
					EmployeePersonalInfo employeePersonalInfo = employees.stream()
							.filter(id -> id.getEmployeeOfficialInfo().getEmployeeId().equals(x.getEmployeeId()))
							.findFirst().orElseGet(null);
					List<TicketHistroy> histories = ownersInfo.stream()
							.filter(ownerId -> collect.contains(ownerId.getEmployeeInfoId()))
							.map(y -> x.getTicketHistroys().stream()
									.filter(ticket -> ticket.getStatus().equalsIgnoreCase(status))
									.map(history -> TicketHistroy.builder().ownerName(y.getFirstName())
											.date(history.getDate()).status(history.getStatus()).build())
									.collect(Collectors.toList()))
							.filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toList());
					return CompanyAdminDeptTicketsResponseDto.builder().objectTicketId(x.getObjectTicketId())
							.category(x.getCategory()).employeeName(employeePersonalInfo.getFirstName())
							.employeeId(x.getEmployeeId()).adminTicketId(x.getTicketId()).ticketHistroies(histories)
							.build();
				}).collect(Collectors.toList());
	}

	@Override
	public CompanyAdminDeptTicketsResponseDto getTicketById(String objectTicketId, String status, Long employeeInfoId) {
		CompanyAdminDeptTickets adminDeptTickets = companyAdminDeptTicketsRepo
				.findByObjectTicketIdAndTicketHistroysStatusIgnoreCase(objectTicketId, status)
				.orElseThrow(() -> new NoTicketFoundException(AdminConstants.NO_TICKETS_FOUND));
		if (adminDeptTickets.getTicketHistroys().isEmpty())
			return CompanyAdminDeptTicketsResponseDto.builder().build();
		List<TicketHistroy> list = adminDeptTickets.getTicketHistroys();
		List<EmployeePersonalInfo> employees = employeePersonalInfoRepository
				.findAllById(list.stream().map(TicketHistroy::getBy).collect(Collectors.toSet()));
		List<TicketHistroy> ticketHistroys = list.stream()
				.map(x -> employees.stream().filter(y -> y.getEmployeeInfoId().equals(x.getBy())).map(xy -> {
					x.setOwnerName(xy.getFirstName());
					x.setEmployeeId(xy.getEmployeeOfficialInfo().getEmployeeId());
					x.setPictureURL(xy.getPictureURL());
					return x;
				}).collect(Collectors.toList())).flatMap(Collection::stream).collect(Collectors.toList());

		EmployeePersonalInfo createdInfo = employeePersonalInfoRepository
				.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoEmployeeId(adminDeptTickets.getCompanyId(),
						adminDeptTickets.getEmployeeId())
				.filter(x -> !x.isEmpty()).map(y -> y.get(0)).orElse(null);
		EmployeePersonalInfo ownerInfo = employeePersonalInfoRepository.findById(ticketHistroys.get(0).getBy())
				.orElse(null);
		EmployeePersonalInfo resolvedTicket = employeePersonalInfoRepository
				.findById(ticketHistroys.get(ticketHistroys.size() - 1).getBy()).orElse(null);
		if (createdInfo == null || ownerInfo == null || resolvedTicket == null)
			return CompanyAdminDeptTicketsResponseDto.builder().build();

		TicketHistroy latestTicketHistory = ticketHistroys.get(ticketHistroys.size() - 1);

		return CompanyAdminDeptTicketsResponseDto.builder().category(adminDeptTickets.getCategory())
				.objectTicketId(objectTicketId).ticketOwner(ownerInfo.getFirstName())
				.employeeId(adminDeptTickets.getEmployeeId()).adminTicketId(adminDeptTickets.getTicketId())
				.date(adminDeptTickets.getTicketHistroys().get(0).getDate()).status(latestTicketHistory.getStatus())
				.isAuthorizedPerson(latestTicketHistory.getStatus().equalsIgnoreCase("Created")
						|| latestTicketHistory.getStatus().equalsIgnoreCase("Delegated") ? Boolean.TRUE
								: Objects.equals(employeeInfoId, latestTicketHistory.getBy()))
				.description(adminDeptTickets.getDescription()).employeeBy(resolvedTicket.getFirstName())
				.lastDate(ticketHistroys.get(ticketHistroys.size() - 1).getDate()).ticketHistroies(list)
				.rating(adminDeptTickets.getRating()).feedback(adminDeptTickets.getFeedback())
				.attachmentsUrl(adminDeptTickets.getAttachmentsUrl())
				.questionAnswer(adminDeptTickets.getQuestionAnswer()).employeeName(createdInfo.getFirstName()).build();

	}

	@Override
	public Boolean updateTickets(Long employeeInfoId, CompanyadminDeptTicketsDto companyadminDeptTicketsDto) {

		CompanyAdminDeptTickets companyAdminDeptTickets = companyAdminDeptTicketsRepo
				.findById(companyadminDeptTicketsDto.getObjectTicketId())
				.orElseThrow(() -> new NoTicketFoundException(AdminConstants.NO_TICKETS_FOUND));

		companyAdminDeptTickets
				.getTicketHistroys().add(
						TicketHistroy
								.builder().date(LocalDate.now()).by(
										employeeInfoId)
								.department(companyadminDeptTicketsDto.getDepartment() == null
										? employeePersonalInfoRepository.findById(employeeInfoId)
												.orElseThrow(() -> new EmployeeNotFoundException("Employee Not Found"))
												.getEmployeeOfficialInfo().getDepartment()
										: companyadminDeptTicketsDto.getDepartment())
								.status(companyadminDeptTicketsDto.getStatus()).build());

		return Optional
				.of(companyadminDeptTicketsDto.getStatus().equalsIgnoreCase("Delegated")).filter(da -> da).map(
						x -> Optional
								.of(Optional.of(companyadminDeptTicketsDto.getDepartment().equalsIgnoreCase("ACCOUNTS"))
										.filter(a -> a)
										.map(xy -> Optional
												.ofNullable(
														companyAccountTicketsRepository.save(BeanCopy.objectProperties(
																companyAdminDeptTickets, CompanyAccountTickets.class)))
												.isPresent())
										.orElseGet(() -> Optional
												.of(companyadminDeptTicketsDto.getDepartment().equalsIgnoreCase("IT"))
												.filter(b -> b)
												.map(xyz -> Optional.ofNullable(
														companyItTicketsRepository
																.save(BeanCopy.objectProperties(companyAdminDeptTickets,
																		CompanyItTickets.class)))
														.isPresent())
												.orElseGet(() -> Optional
														.of(companyadminDeptTicketsDto.getDepartment()
																.equalsIgnoreCase("HR"))
														.filter(c -> c)
														.map(xyza -> Optional.ofNullable(companyHrTicketsRepository
																.save(BeanCopy.objectProperties(companyAdminDeptTickets,
																		CompanyHrTickets.class)))
																.isPresent())
														.orElseGet(() -> false))))
								.filter(d -> d).map(e -> {
									companyAdminDeptTicketsRepo
											.deleteById(companyadminDeptTicketsDto.getObjectTicketId());
									if (companyadminDeptTicketsDto.getStatus().equalsIgnoreCase("Resolved")) {
										notificationServiceImpl.saveNotification(
												"Ticket: " + companyAdminDeptTickets.getTicketId() + " is Resolved",
												employeeInfoId);

										Optional<EmployeePersonalInfo> findById = employeePersonalInfoRepository
												.findById(employeeInfoId);
										if (findById.get().getExpoToken() != null) {
											pushNotificationService.pushMessage("zealhr",
													"Ticket: " + companyAdminDeptTickets.getTicketId() + " is Resolved",
													findById.get().getExpoToken());
										}

									}

									return true;
								}).orElseGet(() -> false))

				.orElseGet(() -> Optional.ofNullable(companyAdminDeptTicketsRepo.save(companyAdminDeptTickets))
						.isPresent());
	}

	@Override
	public List<CompanyAdminDeptTicketsResponseDto> getAllTicketsAccordingCategory(Long companyId, String category) {

		List<CompanyAdminDeptTickets> allTickets = companyAdminDeptTicketsRepo.findByCompanyIdAndCategory(companyId,
				category);
		if (allTickets.isEmpty()) {
			throw new NoTicketFoundException(AdminConstants.NO_TICKETS_FOUND);
		}

		return duplicateCode(allTickets, companyId);
	}

	private List<CompanyAdminDeptTicketsResponseDto> duplicateCode(List<CompanyAdminDeptTickets> allTickets,
			Long companyId) {

		List<String> employeeIdList = new ArrayList<>();
		Set<Long> idset = new HashSet<>();
		for (CompanyAdminDeptTickets companyAdminDeptTickets : allTickets) {

			employeeIdList.add(companyAdminDeptTickets.getEmployeeId());
			List<TicketHistroy> ticketHistroys = companyAdminDeptTickets.getTicketHistroys();

			if (ticketHistroys != null && !ticketHistroys.isEmpty()) {
				int size = ticketHistroys.size();
				TicketHistroy ticketHistroy = ticketHistroys.get(size - 1);
				idset.add(ticketHistroy.getBy());
			}

		}

		List<EmployeePersonalInfo> employeePersonalInfo = employeePersonalInfoRepository
				.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoEmployeeIdIn(companyId, employeeIdList)
				.orElseThrow(() -> new NoEmployeePresentException(AdminConstants.NO_EMPLOYEE_PRESENT));
		if (employeePersonalInfo.isEmpty()) {
			throw new NoEmployeePresentException(AdminConstants.NO_EMPLOYEE_PRESENT);
		}

		List<Long> employeeInfoIdList = new ArrayList<>(idset);

		List<EmployeePersonalInfo> findAllById = employeePersonalInfoRepository
				.findByCompanyInfoCompanyIdAndEmployeeInfoIdIn(companyId, employeeInfoIdList)
				.orElseThrow(() -> new NoEmployeePresentException(AdminConstants.NO_EMPLOYEE_PRESENT));
		if (findAllById.isEmpty()) {
			throw new NoEmployeePresentException(AdminConstants.NO_EMPLOYEE_PRESENT);
		}

		Map<String, String> map = new HashMap<>();

		for (EmployeePersonalInfo employeePersonalInfo2 : employeePersonalInfo) {
			String employeeId = employeePersonalInfo2.getEmployeeOfficialInfo().getEmployeeId();
			String firstName = employeePersonalInfo2.getFirstName();
			map.put(employeeId, firstName);
		}

		Map<Long, String> map2 = new HashMap<>();
		Map<Long, String> map3 = new HashMap<>();
		for (EmployeePersonalInfo employeePersonalInfo2 : findAllById) {

			String firstName = employeePersonalInfo2.getFirstName();
			Long employeeInfoId = employeePersonalInfo2.getEmployeeInfoId();
			map2.put(employeeInfoId, firstName);
			map3.put(employeeInfoId, employeePersonalInfo2.getEmployeeOfficialInfo().getEmployeeId());
		}

		List<CompanyAdminDeptTicketsResponseDto> companyAdminDeptTicketsResponseDtoList = new ArrayList<>();
		for (CompanyAdminDeptTickets companyAdminDeptTickets : allTickets) {
			CompanyAdminDeptTicketsResponseDto companyAdminDeptTicketsResponseDto = new CompanyAdminDeptTicketsResponseDto();
			BeanUtils.copyProperties(companyAdminDeptTickets, companyAdminDeptTicketsResponseDto);
			companyAdminDeptTicketsResponseDto.setEmployeeName(map.get(companyAdminDeptTickets.getEmployeeId()));
			List<TicketHistroy> ticketHistroys = companyAdminDeptTickets.getTicketHistroys();
			if (ticketHistroys != null && !ticketHistroys.isEmpty()) {

				int size = ticketHistroys.size();
				TicketHistroy ticketHistroy = ticketHistroys.get(size - 1);
				BeanUtils.copyProperties(ticketHistroy, companyAdminDeptTicketsResponseDto);
				companyAdminDeptTicketsResponseDto.setTicketOwnerEmployeeId(map3.get(ticketHistroy.getBy()));
				companyAdminDeptTicketsResponseDto.setTicketOwner(map2.get(ticketHistroy.getBy()));

			}
			companyAdminDeptTicketsResponseDto.setAdminTicketId(companyAdminDeptTickets.getTicketId());
			companyAdminDeptTicketsResponseDto.setEmployeeId(companyAdminDeptTickets.getEmployeeId());

			if (companyAdminDeptTickets.getQuestionAnswer() != null
					&& companyAdminDeptTickets.getQuestionAnswer().containsValue("")) {
				companyAdminDeptTicketsResponseDto.setFlag(true);
			}
			companyAdminDeptTicketsResponseDtoList.add(companyAdminDeptTicketsResponseDto);

		}

		List<CompanyAdminDeptTicketsResponseDto> collect2 = companyAdminDeptTicketsResponseDtoList.stream()
				.filter(x -> x.getFlag() != null && x.getFlag()).collect(Collectors.toList());
		List<CompanyAdminDeptTicketsResponseDto> collect3 = companyAdminDeptTicketsResponseDtoList.stream()
				.filter(x -> x.getFlag() == null || !x.getFlag()).collect(Collectors.toList());

		List<CompanyAdminDeptTicketsResponseDto> companyAdminDeptTicketsResponseDtoList1 = new ArrayList<>();

		companyAdminDeptTicketsResponseDtoList1.addAll(collect2);
		companyAdminDeptTicketsResponseDtoList1.addAll(collect3);

		return companyAdminDeptTicketsResponseDtoList1;
	}

}
