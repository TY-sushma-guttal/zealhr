package com.te.zealhr.service.employee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.zealhr.dto.employee.AppraisalMeetingDTO;
import com.te.zealhr.dto.employee.EmployeeDropdownDTO;
import com.te.zealhr.entity.admin.CompanyInfo;
import com.te.zealhr.entity.employee.ApprisalMeetingInfo;
import com.te.zealhr.entity.employee.EmployeeOfficialInfo;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.employee.EmployeeReviseSalary;
import com.te.zealhr.exception.employee.DataNotFoundException;
import com.te.zealhr.repository.employee.ApprisalMeetingInfoRepository;
import com.te.zealhr.repository.employee.EmployeePersonalInfoRepository;

@Service
public class EmployeeAppraisalDetailsServiceImpli implements EmployeeAppraisalDetailsService {

	@Autowired
	private ApprisalMeetingInfoRepository apprisalMeetingInfoRepository;

	@Autowired
	private EmployeePersonalInfoRepository employeePersonalInfoRepository;

	@Override
	public AppraisalMeetingDTO getEmployeeApprisalDetails(Long employeeInfoId) {
		EmployeePersonalInfo employeeInfo = employeePersonalInfoRepository.findById(employeeInfoId)
				.orElseThrow(() -> new DataNotFoundException("Employee Not found"));
		EmployeeOfficialInfo employeeOfficialInfo = employeeInfo.getEmployeeOfficialInfo();
		if (employeeOfficialInfo == null || employeeOfficialInfo.getDoj() == null) {
			throw new DataNotFoundException("Employee Official Details Not Found");
		}
		CompanyInfo companyInfo = employeeInfo.getCompanyInfo();
		if (companyInfo == null) {
			throw new DataNotFoundException("Company Not Found");
		}
		if (companyInfo.getCompanyRuleInfo() == null || companyInfo.getCompanyRuleInfo().getApprisalCycle() == null) {
			throw new DataNotFoundException("Apprisal Configuration Not Found");
		}
		AppraisalMeetingDTO apprisalMeetingDTO = new AppraisalMeetingDTO();
		List<ApprisalMeetingInfo> apprisalMeetingInfoList = apprisalMeetingInfoRepository
				.findByEmployeeReviseSalaryEmployeePersonalInfoEmployeeInfoIdAndEmployeeReviseSalaryRevisedDateIsNull(
						employeeInfoId);
		if (!apprisalMeetingInfoList.isEmpty()) {
			BeanUtils.copyProperties(apprisalMeetingInfoList.get(apprisalMeetingInfoList.size() - 1),
					apprisalMeetingDTO);
			List<EmployeeDropdownDTO> organizers = new ArrayList<>();
			for (EmployeePersonalInfo employeePersonalInfo : apprisalMeetingInfoList
					.get(apprisalMeetingInfoList.size() - 1).getEmployeePersonalInfoList()) {
				EmployeeOfficialInfo organizerEmployeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
				if (organizerEmployeeOfficialInfo != null) {
					organizers.add(new EmployeeDropdownDTO(employeePersonalInfo.getEmployeeInfoId(),
							organizerEmployeeOfficialInfo.getEmployeeId(), employeePersonalInfo.getFirstName()));
				}
			}
			apprisalMeetingDTO.setOrganizers(organizers);
		} else {
			getApprisalMonth(apprisalMeetingDTO, companyInfo, employeeInfo, employeeOfficialInfo);
		}
		return apprisalMeetingDTO;
	}

	private AppraisalMeetingDTO getApprisalMonth(AppraisalMeetingDTO apprisalMeetingDTO, CompanyInfo companyInfo,
			EmployeePersonalInfo employeeInfo, EmployeeOfficialInfo employeeOfficialInfo) {
		Integer apprisalCycle = Integer.parseInt(companyInfo.getCompanyRuleInfo().getApprisalCycle());
		List<EmployeeReviseSalary> periousApprisalDetails = employeeInfo.getEmployeeReviseSalaryList();
		periousApprisalDetails = periousApprisalDetails.stream().filter(apprisal -> apprisal.getRevisedDate() != null)
				.collect(Collectors.toList());
		LocalDate lastApprisalDate;
		if (!periousApprisalDetails.isEmpty()) {
			lastApprisalDate = periousApprisalDetails.get(periousApprisalDetails.size() - 1).getRevisedDate();
		} else {
			lastApprisalDate = employeeOfficialInfo.getDoj();
		}
		LocalDate newRevisalDate = lastApprisalDate.plusMonths(apprisalCycle);
		apprisalMeetingDTO.setApprisalMonth(newRevisalDate.getMonth().toString());
		return apprisalMeetingDTO;
	}

}
