package com.te.zealhr.service.employee;

import static com.te.zealhr.common.admin.EmployeeReimbursementInfoConstants.EMPLOYEE_NOT_FOUND;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.zealhr.dto.employee.EmployeeAdvanceSalaryDTO;
import com.te.zealhr.dto.employee.EmployeePerformanceDetailsDTO;
import com.te.zealhr.dto.employee.EmployeePerformanceInfoDTO;
import com.te.zealhr.dto.hr.CanlenderRequestDTO;
import com.te.zealhr.entity.employee.EmployeeAdvanceSalary;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.report.mongo.EmployeePerformance;
import com.te.zealhr.entity.report.mongo.MonthlyPerformance;
import com.te.zealhr.exception.AdvanceSalaryApprovalDeleteException;
import com.te.zealhr.exception.FormInformationNotFilledException;
import com.te.zealhr.exception.employee.DataNotFoundException;
import com.te.zealhr.repository.admin.LevelsOfApprovalRepository;
import com.te.zealhr.repository.employee.EmployeeAdvanceSalaryRepository;
import com.te.zealhr.repository.employee.EmployeePersonalInfoRepository;
import com.te.zealhr.repository.report.EmployeePerformanceRepository;
import com.te.zealhr.service.hr.CompanyEventDetailsServiceImpl;

@Service
public class EmployeesAdvanceSalaryServiceImpl implements EmployeesAdvanceSalaryService {

	@Autowired
	private EmployeeAdvanceSalaryRepository employeeAdvanceSalaryRepository;

	@Autowired
	private EmployeePersonalInfoRepository personalInfoRepository;

	@Autowired
	private LevelsOfApprovalRepository approvalRepository;

	@Autowired
	private EmployeePerformanceRepository employeePerformanceRepo;

	@Autowired
	private CompanyEventDetailsServiceImpl eventDetailsServiceImpl;

	@Override
	public EmployeeAdvanceSalaryDTO saveAdvanceSalaryRequest(Long companyId, EmployeeAdvanceSalaryDTO advanceSalaryDTO,
			Long employeeInfoId) {
		EmployeeAdvanceSalary advanceSalary = new EmployeeAdvanceSalary();
		EmployeePersonalInfo employeePersonalInfo = personalInfoRepository
				.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId, companyId);
		if (employeePersonalInfo == null) {
			throw new DataNotFoundException(EMPLOYEE_NOT_FOUND);
		} else {

			if (advanceSalaryDTO != null) {

				BeanUtils.copyProperties(advanceSalaryDTO, advanceSalary);
				advanceSalary.setEmployeePersonalInfo(employeePersonalInfo);
				advanceSalary.setStatus("Pending");
				BeanUtils.copyProperties(employeeAdvanceSalaryRepository.save(advanceSalary), advanceSalaryDTO);

				return advanceSalaryDTO;
			} else {
				throw new FormInformationNotFilledException("Form informations are not filled properly");
			}
		}
	}

	@Override
	public List<EmployeeAdvanceSalaryDTO> getAdvanceSalaryDTOList(CanlenderRequestDTO canlenderRequestDTO,
			Long employeeInfoId, Long companyId) {

		EmployeePersonalInfo personalInfoList = personalInfoRepository
				.findByEmployeeInfoIdAndCompanyInfoCompanyId(employeeInfoId, companyId);
		if (personalInfoList != null) {
			List<LocalDate> startEndDate = eventDetailsServiceImpl.getStartEndDate(canlenderRequestDTO.getYear(),
					canlenderRequestDTO.getFiscalMonth());

			List<EmployeeAdvanceSalary> employeeAdvanceSalaryList = employeeAdvanceSalaryRepository
					.findByEmployeePersonalInfoEmployeeInfoIdAndCreatedDateBetween(employeeInfoId, startEndDate.get(0).atStartOfDay(),
							startEndDate.get(1).plusDays(1).atStartOfDay());

			List<EmployeeAdvanceSalaryDTO> advanceSalaryList = new ArrayList<>();

			for (EmployeeAdvanceSalary employeeAdvanceSalary : employeeAdvanceSalaryList) {
				EmployeeAdvanceSalaryDTO tempDTO = new EmployeeAdvanceSalaryDTO();
				tempDTO.setRequestedOn(employeeAdvanceSalary.getCreatedDate());

				LinkedHashMap<String, String> approvedBy = employeeAdvanceSalary.getApprovedBy();
				Set<String> keySet = approvedBy.keySet();
				HashMap<String, String> resultMap = new HashMap<>();
				for (String keyValue : keySet) {
					resultMap.put(keyValue, "Approved");
				}
				if (employeeAdvanceSalary.getRejectedBy() != null) {
					List<String> advanceSalaryApproval = approvalRepository.findByCompanyInfoCompanyId(companyId).get()
							.getAdvanceSalary();
					if (keySet.size() == 0)
						resultMap.put(advanceSalaryApproval.get(0), "Rejected");
					else if (keySet.size() == 1)
						resultMap.put(advanceSalaryApproval.get(1), "Rejected");
					else
						resultMap.put(advanceSalaryApproval.get(2), "Rejected");
				}
				BeanUtils.copyProperties(employeeAdvanceSalary, tempDTO);
				tempDTO.setApprovedBy(resultMap);
				advanceSalaryList.add(tempDTO);
			}

			return advanceSalaryList;
		} else {
			throw new DataNotFoundException(EMPLOYEE_NOT_FOUND);
		}
	}

	@Override
	public EmployeeAdvanceSalaryDTO getAdvanceSalary(Long advanceSalaryId, Long companyId) {

		Optional<EmployeeAdvanceSalary> advanceSalary = employeeAdvanceSalaryRepository
				.findByAdvanceSalaryIdAndEmployeePersonalInfoCompanyInfoCompanyId(advanceSalaryId, companyId);
		EmployeeAdvanceSalary employeeAdvanceSalary = advanceSalary.get();

		if (advanceSalary.isPresent()) {
			EmployeeAdvanceSalaryDTO advanceSalaryDTO = new EmployeeAdvanceSalaryDTO();

			LinkedHashMap<String, String> approvedBy = employeeAdvanceSalary.getApprovedBy();
			Set<String> keySet = approvedBy.keySet();
			HashMap<String, String> resultMap = new HashMap<>();
			for (String keyValue : keySet) {
				resultMap.put(keyValue, "Approved");
			}
			if (employeeAdvanceSalary.getRejectedBy() != null) {
				List<String> advanceSalaryApproval = approvalRepository.findByCompanyInfoCompanyId(companyId).get()
						.getAdvanceSalary();
				if (keySet.size() == 0)
					resultMap.put(advanceSalaryApproval.get(0), "Rejected");
				else if (keySet.size() == 1)
					resultMap.put(advanceSalaryApproval.get(1), "Rejected");
				else
					resultMap.put(advanceSalaryApproval.get(2), "Rejected");
			}

			BeanUtils.copyProperties(advanceSalary.get(), advanceSalaryDTO);
			advanceSalaryDTO.setApprovedBy(resultMap);
			return advanceSalaryDTO;
		} else {
			throw new DataNotFoundException("Data is Not Present");
		}
	}

	@Override
	public void deleteAdvanceSalaryRequest(Long advanceSalaryId, Long companyId) {

		Optional<EmployeeAdvanceSalary> employeeAdvanceSalary = employeeAdvanceSalaryRepository
				.findByAdvanceSalaryIdAndEmployeePersonalInfoCompanyInfoCompanyId(advanceSalaryId, companyId);

		if (employeeAdvanceSalary.isPresent()) {

			EmployeeAdvanceSalary advanceSalary = employeeAdvanceSalary.get();

			if (advanceSalary.getApprovedBy().isEmpty() && advanceSalary.getRejectedBy() == null) {

				employeeAdvanceSalaryRepository.deleteById(advanceSalaryId);
			} else {
				throw new AdvanceSalaryApprovalDeleteException("Request Can't be Deleted");
			}
		} else {
			throw new DataNotFoundException("Request Not Found");
		}
	}

	@Override
	public EmployeeAdvanceSalaryDTO editAdvanceSalaryRequest(EmployeeAdvanceSalaryDTO advanceSalaryDTO,
			Long advanceSalaryId, Long companyId) {

		Optional<EmployeeAdvanceSalary> employeeAdvanceSalary = employeeAdvanceSalaryRepository
				.findByAdvanceSalaryIdAndEmployeePersonalInfoCompanyInfoCompanyId(advanceSalaryId, companyId);
		if (employeeAdvanceSalary.isPresent()) {

			EmployeeAdvanceSalary emplAdvanceSalary = employeeAdvanceSalary.get();

			if (emplAdvanceSalary.getApprovedBy().isEmpty() && emplAdvanceSalary.getRejectedBy() == null) {

				emplAdvanceSalary.setAmount(advanceSalaryDTO.getAmount());
				emplAdvanceSalary.setEmi(advanceSalaryDTO.getEmi());
				emplAdvanceSalary.setReason(advanceSalaryDTO.getReason());

				BeanUtils.copyProperties(employeeAdvanceSalaryRepository.save(emplAdvanceSalary), advanceSalaryDTO);

				return advanceSalaryDTO;
			} else {
				throw new AdvanceSalaryApprovalDeleteException("Request Can't Be Edited");
			}
		} else {
			throw new DataNotFoundException("Request Not Found");
		}
	}

	@Override
	public Map<String, EmployeePerformanceInfoDTO> getPerformanceMonthly(EmployeePerformanceDetailsDTO performanceDto,
			Long companyId) {
		Month fiscalMonth = Month.valueOf(performanceDto.getFiscalMonth().toUpperCase(Locale.ENGLISH));
		List<String> firstYearMonths;
		List<String> lastYearMonths = new ArrayList<>();
		int startMonth = fiscalMonth.getValue();
		List<Long> years = new ArrayList<>();
		Map<String, EmployeePerformanceInfoDTO> employeePerformanceInfoDTOMap = new LinkedHashMap<>();
		if (performanceDto.getYear().contains("-")) {
			String[] split = performanceDto.getYear().split("-");
			years.add(Long.valueOf(split[0]));
			years.add(Long.valueOf(split[1]));
			firstYearMonths = getMonthNamesBetween(startMonth, 12, employeePerformanceInfoDTOMap);
			lastYearMonths = getMonthNamesBetween(1, startMonth - 1, employeePerformanceInfoDTOMap);
		} else {
			years.add(Long.valueOf(performanceDto.getYear()));
			firstYearMonths = getMonthNamesBetween(1, 12, employeePerformanceInfoDTOMap);
		}
		List<EmployeePerformance> employeePerformanceList = employeePerformanceRepo
				.findByCompanyIdAndEmployeeInfoIdAndYearIn(companyId, performanceDto.getEmployeeInfoId(), years);
		for (EmployeePerformance employeePerformance : employeePerformanceList) {
			Map<String, MonthlyPerformance> monthlyPerformance = employeePerformance.getMonthlyPerformance();
			if (monthlyPerformance != null && employeePerformance.getYear().equals(years.get(0))) {
				getMonthData(firstYearMonths, employeePerformanceInfoDTOMap, monthlyPerformance);
			}

			if (monthlyPerformance != null && years.size() == 2 && employeePerformance.getYear().equals(years.get(1))) {
				getMonthData(lastYearMonths, employeePerformanceInfoDTOMap, monthlyPerformance);
			}
		}

		return employeePerformanceInfoDTOMap;

	}

	private void getMonthData(List<String> months,
			Map<String, EmployeePerformanceInfoDTO> employeePerformanceInfoDTOMap,
			Map<String, MonthlyPerformance> monthlyPerformance) {
		for (String month : months) {
			MonthlyPerformance monthlyPerformanceDetails = monthlyPerformance.get(month);
			if (monthlyPerformanceDetails != null) {
				BeanUtils.copyProperties(monthlyPerformanceDetails, employeePerformanceInfoDTOMap.get(month));
				// employeePerformanceInfoDTOMap.put(month, employeePerformanceInfoDTO);
			}
		}
	}

	private static List<String> getMonthNamesBetween(int startMonthNumber, int endMonthNumber,
			Map<String, EmployeePerformanceInfoDTO> employeePerformanceInfoDTOMap) {
		List<String> monthNamesBetween = new ArrayList<>();

		for (int monthNumber = startMonthNumber; monthNumber <= endMonthNumber; monthNumber++) {
			Month month = Month.of(monthNumber);
			String monthName = month.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH);
			monthNamesBetween.add(monthName.toUpperCase());
			employeePerformanceInfoDTOMap.put(monthName.toUpperCase(), new EmployeePerformanceInfoDTO());
		}

		return monthNamesBetween;
	}
}
