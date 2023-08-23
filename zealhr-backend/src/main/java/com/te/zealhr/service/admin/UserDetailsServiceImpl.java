package com.te.zealhr.service.admin;

import static com.te.zealhr.common.employee.EmployeeLoginConstants.EMPLYOEE_DOES_NOT_EXIST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.zealhr.constants.admin.AdminConstants;
import com.te.zealhr.dto.admin.AddExistingEmployeeDataRequestDto;
import com.te.zealhr.dto.admin.BranchInfoDto;
import com.te.zealhr.dto.admin.DepartmentInfoDto;
import com.te.zealhr.dto.admin.DesignationInfoDto;
import com.te.zealhr.dto.admin.EmployeeDataDto;
import com.te.zealhr.dto.admin.EmployeeOfficialInfoDTO;
import com.te.zealhr.dto.admin.EmployeeUploadDTO;
import com.te.zealhr.dto.admin.WorkWeekInfoDto;
import com.te.zealhr.dto.admindept.ProductNameDTO;
import com.te.zealhr.dto.employee.EmployeeIdDto;
import com.te.zealhr.dto.employee.MailDto;
import com.te.zealhr.entity.admin.CompanyBranchInfo;
import com.te.zealhr.entity.admin.CompanyDepartmentDetails;
import com.te.zealhr.entity.admin.CompanyDesignationInfo;
import com.te.zealhr.entity.admin.CompanyInfo;
import com.te.zealhr.entity.admin.CompanyWorkWeekRule;
import com.te.zealhr.entity.employee.EmployeeLoginInfo;
import com.te.zealhr.entity.employee.EmployeeOfficialInfo;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.exception.DataNotFoundException;
import com.te.zealhr.exception.admin.BranchNotFoundException;
import com.te.zealhr.exception.admin.CompanyNotFound;
import com.te.zealhr.exception.admin.DuplicateEmployeeIdException;
import com.te.zealhr.exception.admin.WorkWeekRuleNotFoundException;
import com.te.zealhr.exception.employee.EmployeeNotFoundException;
import com.te.zealhr.exception.employee.EmployeeOfficialInfoNotFoundException;
import com.te.zealhr.repository.admin.CompanyDepartmentDetailsRepository;
import com.te.zealhr.repository.admin.CompanyDesignationInfoRepository;
import com.te.zealhr.repository.admin.CompanyInfoRepository;
import com.te.zealhr.repository.employee.EmployeeLoginInfoRepository;
import com.te.zealhr.repository.employee.EmployeePersonalInfoRepository;
import com.te.zealhr.service.hr.EmployeeManagementServiceImpl;
import com.te.zealhr.service.mail.employee.EmailService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Tapas
 *
 */
@Slf4j
@Service

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private CompanyDepartmentDetailsRepository departmentInfoRepository;

	@Autowired
	private CompanyDesignationInfoRepository companyDesignationInfoRepository;

	@Autowired
	private CompanyInfoRepository companyInfoRepo;

	@Autowired
	private EmployeeLoginInfoRepository employeeLoginInfoRepo;

	@Autowired
	private EmployeePersonalInfoRepository employeePersonalInfoRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private EmployeeManagementServiceImpl employeeManagementServiceImpl;

	@Override
	// API for Showing All User based on companyId

	public List<EmployeeDataDto> userDetails(long companyId) {
		log.info("service method userDetails of UserDetailsServiceImpl class company id is : {}", companyId);
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND));

		List<EmployeePersonalInfo> listOfEmployeePersonalInfo = companyInfo.getEmployeePersonalInfoList();

		Collections.reverse(listOfEmployeePersonalInfo);

		List<EmployeeDataDto> employeeDataDtos = new ArrayList<>();

		for (EmployeePersonalInfo employeePersonalInfo : listOfEmployeePersonalInfo) {
			EmployeeDataDto employeeDataDto = new EmployeeDataDto();
			BeanUtils.copyProperties(employeePersonalInfo, employeeDataDto);

			if (employeePersonalInfo.getEmployeeOfficialInfo() == null) {
				continue;
			}

			if (employeePersonalInfo.getEmployeeOfficialInfo() != null) {
				BeanUtils.copyProperties(employeePersonalInfo.getEmployeeOfficialInfo(), employeeDataDto);
			}

			if (employeeDataDto.getDesignation() != null && !companyInfo.getCompanyDesignationInfoList().isEmpty()) {

				Optional<Long> findFirst = companyInfo.getCompanyDesignationInfoList().stream()
						.filter(x -> x.getDesignationName().equals(employeeDataDto.getDesignation()))
						.map(CompanyDesignationInfo::getDesignationId).findFirst();

				if (!findFirst.isEmpty()) {
					employeeDataDto.setDesignationId(findFirst.get());
				}

			}

			employeeDataDto.setFirstName(employeePersonalInfo.getFirstName());
			employeeDataDtos.add(employeeDataDto);

		}

		log.info("service method returned values--- list of employees : {}", employeeDataDtos);
		return employeeDataDtos;
	}

	/*
	 * API for User Management Details (Find by CompanyId and officialId)
	 */
	@Override
	public EmployeeOfficialInfoDTO userManagementDetails(Long companyId, Long officialId) {
		log.info(
				"service method userManagementDetails of UserDetailsServiceImpl class company id is : {},official id is : {}",
				companyId, officialId);

		Optional<CompanyInfo> optionalCompanyInfo = companyInfoRepo.findById(companyId);
		if (!optionalCompanyInfo.isPresent()) {
			throw new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND);
		}

		EmployeePersonalInfo personalInfo = employeePersonalInfoRepository
				.findByCompanyInfoCompanyIdAndEmployeeOfficialInfoOfficialId(companyId, officialId)
				.orElseThrow(() -> new EmployeeNotFoundException(AdminConstants.EMPLOYEE_NOT_FOUND));

		EmployeeOfficialInfoDTO employeeOfficialInfoDTO1 = new EmployeeOfficialInfoDTO();

		employeeOfficialInfoDTO1.setFirstName(personalInfo.getFirstName());

		BeanUtils.copyProperties(personalInfo, employeeOfficialInfoDTO1);

		if (personalInfo.getEmployeeOfficialInfo() == null) {
			throw new EmployeeOfficialInfoNotFoundException("employee official info not found");
		}

		BeanUtils.copyProperties(personalInfo.getEmployeeOfficialInfo(), employeeOfficialInfoDTO1);

		if (personalInfo.getEmployeeOfficialInfo().getCompanyWorkWeekRule() != null) {

			BeanUtils.copyProperties(personalInfo.getEmployeeOfficialInfo().getCompanyWorkWeekRule(),
					employeeOfficialInfoDTO1);

		}

		if (personalInfo.getEmployeeOfficialInfo().getCompanyBranchInfo() != null) {

			BeanUtils.copyProperties(personalInfo.getEmployeeOfficialInfo().getCompanyBranchInfo(),
					employeeOfficialInfoDTO1);
		}

		if (employeeOfficialInfoDTO1.getDesignation() != null
				&& !optionalCompanyInfo.get().getCompanyDesignationInfoList().isEmpty()) {

			Optional<Long> findFirst = optionalCompanyInfo.get().getCompanyDesignationInfoList().stream()
					.filter(x -> x.getDesignationName().equals(employeeOfficialInfoDTO1.getDesignation()))
					.map(CompanyDesignationInfo::getDesignationId).findFirst();

			if (!findFirst.isEmpty()) {
				employeeOfficialInfoDTO1.setDesignationId(findFirst.get());
			}

		}

		if (employeeOfficialInfoDTO1.getDepartment() != null) {
			Optional<CompanyDepartmentDetails> department = departmentInfoRepository
					.findByCompanyDepartmentNameAndCompanyInfoCompanyId(employeeOfficialInfoDTO1.getDepartment(),
							companyId);
			if (!department.isEmpty()) {
				employeeOfficialInfoDTO1.setDepartmentId(department.get().getCompanyDepartmentId());
			}

		}

		EmployeeLoginInfo findByEmployeeId = employeeLoginInfoRepo
				.findByEmployeeId(employeeOfficialInfoDTO1.getEmployeeId()).filter(y -> !y.isEmpty()).map(x -> x.get(0))
				.orElseThrow(() -> new EmployeeNotFoundException(EMPLYOEE_DOES_NOT_EXIST));

		BeanUtils.copyProperties(findByEmployeeId, employeeOfficialInfoDTO1);

		log.info("service method returned values--- employee details : {}", employeeOfficialInfoDTO1);
		return employeeOfficialInfoDTO1;
	}

	/*
	 * API for status active/inactive
	 */
	@Override
	@Transactional
	public String updateStatus(Long companyId, Long officialId, String employeeId,
			ProductNameDTO employeeStatusUpdateDTO) {
		log.info(
				"service method updateStatus of UserDetailsServiceImpl class company id is : {},official id is : {},eployee details update request : {}",
				companyId, officialId, employeeStatusUpdateDTO);

		if (!(boolean) employeeStatusUpdateDTO.getIsActive() && employeeStatusUpdateDTO.getReason() == null) {
			throw new DataNotFoundException("please provide reason for Inactive");
		}

		Optional<CompanyInfo> companyInfo = companyInfoRepo.findById(companyId);
		if (!companyInfo.isPresent()) {
			throw new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND);
		}

		EmployeeLoginInfo employeeLoginInfo = employeeLoginInfoRepo
				.findByEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeOfficialInfoOfficialId(
						companyId, officialId)
				.orElseThrow(() -> new EmployeeNotFoundException(AdminConstants.EMPLOYEE_NOT_FOUND));

		EmployeePersonalInfo employeePersonalInfo = employeeLoginInfo.getEmployeePersonalInfo();

		if (employeeStatusUpdateDTO.getReason() != null) {
			employeePersonalInfo.setStatus(Map.of(employeeId, employeeStatusUpdateDTO.getReason()));

		} else {
			employeePersonalInfo.setStatus(Map.of(AdminConstants.APPROVED_BY, employeeId));
		}

		employeePersonalInfo.setIsActive(employeeStatusUpdateDTO.getIsActive());

		return null;
	}

	// push all data on add existing employee

	@Override
	@Transactional
	public String addExistingEmployee(Long companyId, String employeeId,
			AddExistingEmployeeDataRequestDto addExistingEmployeeDataRequestDto) {
		log.info(
				"service method of UserDetailsServiceImpl class company id is : {},eployee details update request : {}",
				companyId, addExistingEmployeeDataRequestDto);
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND));

		addExistingEmployeeDataRequestDto.setEmployeeId(addExistingEmployeeDataRequestDto.getEmployeeId() == null
				? employeeManagementServiceImpl
						.generateEmployeeId(companyId, addExistingEmployeeDataRequestDto.getDoj()).toString()
				: addExistingEmployeeDataRequestDto.getEmployeeId());
		Optional<EmployeeLoginInfo> findByEmployeeId = employeeLoginInfoRepo
				.findByEmployeeIdAndEmployeePersonalInfoCompanyInfoCompanyId(
						addExistingEmployeeDataRequestDto.getEmployeeId(), companyId);

		if (!findByEmployeeId.isEmpty()) {
			throw new DuplicateEmployeeIdException("Employee id already exists");
		}

		Boolean companyWorkWeekRulePresent = true;

		if (addExistingEmployeeDataRequestDto.getWorkWeekRuleId() == null) {
			if (!companyInfo.getCompanyWorkWeekRuleList().isEmpty()) {
				Optional<CompanyWorkWeekRule> defaultCompanyWorkWeekRule = companyInfo.getCompanyWorkWeekRuleList()
						.stream().filter(CompanyWorkWeekRule::getIsDefault).findFirst();
				if (defaultCompanyWorkWeekRule.isPresent()) {
					addExistingEmployeeDataRequestDto
							.setWorkWeekRuleId(defaultCompanyWorkWeekRule.get().getWorkWeekRuleId());
				}
			} else {
				companyWorkWeekRulePresent = false;
			}
		}

		EmployeeOfficialInfo employeeOfficialInfo = new EmployeeOfficialInfo();
		BeanUtils.copyProperties(addExistingEmployeeDataRequestDto, employeeOfficialInfo);

		employeeOfficialInfo.setCompanyBranchInfo(
				UserDetailsServiceImpl.findBranch(companyInfo, addExistingEmployeeDataRequestDto.getBranchId()));

		if (Boolean.TRUE.equals(companyWorkWeekRulePresent)) {
			employeeOfficialInfo.setCompanyWorkWeekRule(UserDetailsServiceImpl.findWorkWeekRule(companyInfo,
					addExistingEmployeeDataRequestDto.getWorkWeekRuleId()));
		} else {
			employeeOfficialInfo.setCompanyWorkWeekRule(null);
		}

		EmployeePersonalInfo employeePersonalInfo = new EmployeePersonalInfo();
		BeanUtils.copyProperties(addExistingEmployeeDataRequestDto, employeePersonalInfo);
		employeePersonalInfo.setCompanyInfo(companyInfo);
		employeePersonalInfo.setEmployeeOfficialInfo(employeeOfficialInfo);

		employeePersonalInfo.setStatus(Map.of(AdminConstants.APPROVED_BY, employeeId));
		employeePersonalInfo.setIsActive(Boolean.TRUE);
		EmployeePersonalInfo savedEmployeePersonalInfo = employeePersonalInfoRepository.save(employeePersonalInfo);

		EmployeeLoginInfo employeeLoginInfo = new EmployeeLoginInfo();
		BeanUtils.copyProperties(addExistingEmployeeDataRequestDto, employeeLoginInfo);
		employeeLoginInfo.setEmployeePersonalInfo(savedEmployeePersonalInfo);
		employeeLoginInfo.setRoles(addExistingEmployeeDataRequestDto.getRoles());
		employeeLoginInfo.setEmailId(addExistingEmployeeDataRequestDto.getOfficialEmailId());

		EmployeeLoginInfo saveEmployeeLoginInfo = employeeLoginInfoRepo.save(employeeLoginInfo);

		String toMail = saveEmployeeLoginInfo.getEmployeePersonalInfo().getEmployeeOfficialInfo().getOfficialEmailId();

		if (toMail != null) {
			MailDto mailDto = new MailDto();
			mailDto.setTo(toMail);
			mailDto.setSubject("Your password For Verification");
			mailDto.setBody("Dear " + saveEmployeeLoginInfo.getEmployeePersonalInfo().getFirstName() + ",\r\n" + "\r\n"
					+ "your official email ID login credentials. Please login and access zealhr. If you are unable to login, please share a screenshot of the error page to projectmailer2021@gmail.com"
					+ "\r\n" + "\r\n" + "Your Company Code :" + employeePersonalInfo.getCompanyInfo().getCompanyCode()
					+ "\r\n" + "Your Email :" + saveEmployeeLoginInfo.getEmailId() + "\r\n" + "\r\n"
					+ "Kindly use the zealhr official email id for any further communications." + "\r\n"
					+ "Thanks and Regards," + "\r\n" + "Team zealhr");
			emailService.sendMail(mailDto);

		}

		return null;
	}

	public static CompanyBranchInfo findBranch(CompanyInfo companyInfo, Long branchId) {
		List<CompanyBranchInfo> companyBranchInfoList = companyInfo.getCompanyBranchInfoList();
		CompanyBranchInfo companyBranchInfo1;

		List<Long> listOfBranch = companyBranchInfoList.stream().map(CompanyBranchInfo::getBranchId)
				.collect(Collectors.toList());

		if (branchId == null) {
			throw new BranchNotFoundException(AdminConstants.COMPANY_BRANCH_NOT_FOUND);
		}

		int indexOfBranch = listOfBranch.indexOf(branchId);

		if (indexOfBranch < 0) {
			throw new BranchNotFoundException(AdminConstants.COMPANY_BRANCH_NOT_FOUND);
		} else {
			companyBranchInfo1 = companyBranchInfoList.get(indexOfBranch);
		}
		return companyBranchInfo1;
	}

	public static CompanyWorkWeekRule findWorkWeekRule(CompanyInfo companyInfo, Long workWeekRuleId) {
		CompanyWorkWeekRule companyWorkWeekRule;

		List<CompanyWorkWeekRule> companyWorkWeekRuleList = companyInfo.getCompanyWorkWeekRuleList();

		List<Long> listOfRule = companyWorkWeekRuleList.stream().map(CompanyWorkWeekRule::getWorkWeekRuleId)
				.collect(Collectors.toList());

		if (workWeekRuleId == null) {
			throw new WorkWeekRuleNotFoundException(AdminConstants.WORK_WEEK_RULE_NOT_FOUND);
		}

		int indexOfRuleName = listOfRule.indexOf(workWeekRuleId);

		if (indexOfRuleName < 0) {
			throw new WorkWeekRuleNotFoundException(AdminConstants.WORK_WEEK_RULE_NOT_FOUND);
		} else {
			companyWorkWeekRule = companyWorkWeekRuleList.get(indexOfRuleName);
		}
		return companyWorkWeekRule;
	}

	/*
	 * API for update user
	 */

	@Override
	@Transactional
	public String updateUserDetails(Long companyId, Long officialId, String employeeId,
			EmployeeOfficialInfoDTO employeeOfficialInfoDTO) {

		log.info(
				"service method of UserDetailsServiceImpl class company id is : {},official id is : {},eployee details update request : {}",
				companyId, officialId, employeeOfficialInfoDTO);

		Optional<CompanyInfo> companyInfo = companyInfoRepo.findById(companyId);
		if (!companyInfo.isPresent()) {
			throw new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND);
		}

		employeeOfficialInfoDTO.setOfficialId(officialId);

		EmployeeLoginInfo employeeLoginInfo = employeeLoginInfoRepo
				.findByEmployeePersonalInfoCompanyInfoCompanyIdAndEmployeePersonalInfoEmployeeOfficialInfoOfficialId(
						companyId, officialId)
				.orElseThrow(() -> new EmployeeNotFoundException(AdminConstants.EMPLOYEE_NOT_FOUND));

		boolean sendMail = false;

		if (employeeOfficialInfoDTO.getEmployeeId()
				.equals(employeeLoginInfo.getEmployeePersonalInfo().getEmployeeOfficialInfo().getEmployeeId())) {
			sendMail = true;
		}

		Boolean companyWorkWeekRulePresent = true;

		if (employeeOfficialInfoDTO.getWorkWeekRuleId() == null) {
			if (!companyInfo.get().getCompanyWorkWeekRuleList().isEmpty()) {
				Optional<CompanyWorkWeekRule> defaultCompanyWorkWeekRule = companyInfo.get()
						.getCompanyWorkWeekRuleList().stream().filter(CompanyWorkWeekRule::getIsDefault).findFirst();
				if (defaultCompanyWorkWeekRule.isPresent()) {
					employeeOfficialInfoDTO.setWorkWeekRuleId(defaultCompanyWorkWeekRule.get().getWorkWeekRuleId());
				}
			} else {
				companyWorkWeekRulePresent = false;
			}
		}

		EmployeePersonalInfo employeePersonalInfo = employeeLoginInfo.getEmployeePersonalInfo();

		Optional<EmployeeLoginInfo> findByEmployeeId = employeeLoginInfoRepo
				.findByEmployeeIdAndEmployeePersonalInfoCompanyInfoCompanyId(employeeOfficialInfoDTO.getEmployeeId(),
						companyId);

		if (!findByEmployeeId.isEmpty() && !findByEmployeeId.get().getEmployeePersonalInfo().getEmployeeOfficialInfo()
				.getOfficialId().equals(employeeOfficialInfoDTO.getOfficialId())) {
			throw new DuplicateEmployeeIdException("Employee id already exists");
		}

		employeeOfficialInfoDTO.setIsActive(employeePersonalInfo.getIsActive());

		EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();

		BeanUtils.copyProperties(employeeOfficialInfoDTO, employeeOfficialInfo);
		employeeOfficialInfo.setCompanyBranchInfo(
				UserDetailsServiceImpl.findBranch(companyInfo.get(), employeeOfficialInfoDTO.getBranchId()));

		if (Boolean.TRUE.equals(companyWorkWeekRulePresent)) {
			employeeOfficialInfo.setCompanyWorkWeekRule(UserDetailsServiceImpl.findWorkWeekRule(companyInfo.get(),
					employeeOfficialInfoDTO.getWorkWeekRuleId()));
		} else {
			employeeOfficialInfo.setCompanyWorkWeekRule(null);
		}

		BeanUtils.copyProperties(employeeOfficialInfoDTO, employeePersonalInfo);

		employeePersonalInfo.setCompanyInfo(companyInfo.get());
		employeePersonalInfo.setEmployeeOfficialInfo(employeeOfficialInfo);

		employeePersonalInfo.setStatus(Map.of(AdminConstants.APPROVED_BY, employeeId));

		BeanUtils.copyProperties(employeeOfficialInfoDTO, employeeLoginInfo);
		employeePersonalInfo.setIsActive(Boolean.TRUE);
		employeeLoginInfo.setEmployeePersonalInfo(employeePersonalInfo);

		employeeLoginInfo.setRoles(employeeOfficialInfoDTO.getRoles());

		String toMail = employeeLoginInfo.getEmployeePersonalInfo().getEmployeeOfficialInfo().getOfficialEmailId();

		if (toMail != null && !sendMail) {

			MailDto mailDto = new MailDto();
			mailDto.setTo(toMail);
			mailDto.setSubject("Your password For Verification");
			mailDto.setBody("Dear " + employeeLoginInfo.getEmployeePersonalInfo().getFirstName() + ",\r\n" + "\r\n"
					+ "your official email ID login credentials. Please login and access zealhr. If you are unable to login, please share a screenshot of the error page to projectmailer2021@gmail.com"
					+ "\r\n" + "\r\n" + "Your Company Code :" + employeePersonalInfo.getCompanyInfo().getCompanyCode()
					+ "\r\n" + "Your Email :" + employeeLoginInfo.getEmailId() + "\r\n"
					+ " Your Password remains the same " + "\r\n" + "\r\n"
					+ "Kindly use the zealhr official email id for any further communications." + "\r\n"
					+ "Thanks and Regards," + "\r\n" + "Team zealhr");
			emailService.sendMail(mailDto);

		}
		log.info("service method returned values--- employee details : {}", employeeLoginInfo);
		return null;
	}

	@Override
	public List<BranchInfoDto> getAllBranchInfo(Long companyId) {
		log.info("service method getAllBranchInfo of UserDetailsServiceImpl class company id is : {}", companyId);
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND));

		List<CompanyBranchInfo> companyBranchInfoList = companyInfo.getCompanyBranchInfoList();

		List<BranchInfoDto> branchInfoDtos = new ArrayList<>();
		for (CompanyBranchInfo companyBranchInfo : companyBranchInfoList) {
			BranchInfoDto branchInfoDto = new BranchInfoDto();
			branchInfoDto.setBranchId(companyBranchInfo.getBranchId());
			branchInfoDto.setBranchName(companyBranchInfo.getBranchName());
			branchInfoDtos.add(branchInfoDto);
		}
		return branchInfoDtos;
	}

	@Override
	public List<DepartmentInfoDto> getAllDepartmentInfo(Long companyId) {

		log.info("service method getAllDepartmentInfo of UserDetailsServiceImpl class company id is : {}", companyId);

		Optional<CompanyInfo> optionalCompanyInfo = companyInfoRepo.findById(companyId);
		if (!optionalCompanyInfo.isPresent()) {
			throw new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND);
		}

		List<DepartmentInfoDto> departmentInfoDtos = new ArrayList<>();
		for (CompanyDepartmentDetails department : departmentInfoRepository.findByCompanyInfoCompanyId(companyId)) {

			DepartmentInfoDto departmentInfoDto = new DepartmentInfoDto();
			departmentInfoDto.setDepartmentId(department.getCompanyDepartmentId());
			departmentInfoDto.setDepartmentName(department.getCompanyDepartmentName());
			departmentInfoDtos.add(departmentInfoDto);
		}
		return departmentInfoDtos;
	}

	@Override
	public List<DesignationInfoDto> getAllDesignationInfo(Long companyId, String department) {

		log.info("service method getAllDesignationInfo of UserDetailsServiceImpl class company id is : {}", companyId);

		Optional<CompanyInfo> optionalCompanyInfo = companyInfoRepo.findById(companyId);
		if (!optionalCompanyInfo.isPresent()) {
			throw new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND);
		}

		List<CompanyDesignationInfo> companyDesignationInfoList = companyDesignationInfoRepository
				.findByDepartmentAndCompanyInfoCompanyId(department, companyId);
		List<DesignationInfoDto> designationInfoDtos = new ArrayList<>();
		for (CompanyDesignationInfo companyDesignationInfo : companyDesignationInfoList) {

			DesignationInfoDto designationInfoDto = new DesignationInfoDto();
			designationInfoDto.setDesignationId(companyDesignationInfo.getDesignationId());
			designationInfoDto.setDesignationName(companyDesignationInfo.getDesignationName());
			designationInfoDto.setRoles(companyDesignationInfo.getRoles());
			designationInfoDtos.add(designationInfoDto);
		}
		return designationInfoDtos;
	}

	@Override
	public List<WorkWeekInfoDto> getAllWorkInfo(Long companyId) {

		log.info("service method getAllWorkInfo of UserDetailsServiceImpl class company id is : {}", companyId);
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new CompanyNotFound(AdminConstants.COMPANY_NOT_FOUND));

		List<CompanyWorkWeekRule> companyWorkWeekRuleList = companyInfo.getCompanyWorkWeekRuleList();
		List<WorkWeekInfoDto> weekInfoDtos = new ArrayList<>();
		for (CompanyWorkWeekRule companyWorkWeekRule : companyWorkWeekRuleList) {

			WorkWeekInfoDto workWeekInfoDto = new WorkWeekInfoDto();
			workWeekInfoDto.setWorkWeekRuleId(companyWorkWeekRule.getWorkWeekRuleId());
			workWeekInfoDto.setRuleName(companyWorkWeekRule.getRuleName());
			weekInfoDtos.add(workWeekInfoDto);
		}
		return weekInfoDtos;
	}

	@Override
	public List<EmployeeIdDto> getAllEmployeeName(Long companyId) {
		return employeePersonalInfoRepository.getEmployeeNames(companyId).orElse(Collections.emptyList());
	}

	@org.springframework.transaction.annotation.Transactional
	@Override
	public String employeeBlukUpload(List<EmployeeUploadDTO> employeeUploadDTOList, Long companyId, Long userId) {
		CompanyInfo companyInfo = companyInfoRepo.findById(companyId)
				.orElseThrow(() -> new DataNotFoundException("Company not Found"));
		List<EmployeePersonalInfo> employeePersonalInfoList = new ArrayList<>();
		List<EmployeeLoginInfo> employeeLoginInfoList = new ArrayList<>();
		List<String> departmentNotFoundList = new ArrayList<>();
		List<String> designationNotFoundForDepartmentList = new ArrayList<>();
		List<EmployeePersonalInfo> filteredEmployeeInfoList = companyInfo.getEmployeePersonalInfoList().stream()
				.filter(employee -> employee.getEmployeeOfficialInfo() != null).collect(Collectors.toList());
		try {
			employeeUploadDTOList.stream().filter(
					employee -> employee.getOfficialEmailId() != null && (filteredEmployeeInfoList.stream().filter(
							filteredEmployee -> (filteredEmployee.getEmployeeOfficialInfo().getOfficialEmailId() != null
									&& employee.getOfficialEmailId().equalsIgnoreCase(
											filteredEmployee.getEmployeeOfficialInfo().getOfficialEmailId()))
									|| employee.getEmployeeId().equalsIgnoreCase(
											filteredEmployee.getEmployeeOfficialInfo().getEmployeeId()))
							.collect(Collectors.toList()).isEmpty()))
					.map(employee -> {
						employee.setEmployeeId(
								employee.getEmployeeId() == null
										? employeeManagementServiceImpl.generateEmployeeId(companyId, employee.getDoj())
												.toString()
										: employee.getEmployeeId());
						Optional<CompanyDepartmentDetails> department = departmentInfoRepository
								.findByCompanyDepartmentNameAndCompanyInfoCompanyId(employee.getDepartment(),
										companyId);
						if (department.isPresent()) {
							Optional<CompanyDesignationInfo> designation = companyDesignationInfoRepository
									.findByDesignationNameAndDepartmentAndCompanyInfoCompanyId(
											employee.getDesignation(), employee.getDepartment(), companyId);
							if (designation.isPresent()) {
								EmployeePersonalInfo employeePersonalInfo = new EmployeePersonalInfo();
								employeePersonalInfo.setFirstName(employee.getFirstName());
								employeePersonalInfo.setIsActive(Boolean.TRUE);
								Map<String, String> status = employeePersonalInfo.getStatus() == null
										? new LinkedHashMap<>()
										: employeePersonalInfo.getStatus();
								status.put("approvedBy", userId + "");
								employeePersonalInfo.setStatus(status);
								employeePersonalInfo.setCompanyInfo(companyInfo);
								EmployeeOfficialInfo employeeOfficialInfo = new EmployeeOfficialInfo();
								employeeOfficialInfo.setEmployeeId(employee.getEmployeeId());
								employeeOfficialInfo.setOfficialEmailId(employee.getOfficialEmailId());
								employeeOfficialInfo.setDoj(employee.getDoj());
								EmployeeLoginInfo employeeLoginInfo = new EmployeeLoginInfo();
								employeeLoginInfo.setEmailId(employee.getOfficialEmailId());
								employeeLoginInfo.setEmployeeId(employee.getEmployeeId());
								employeeLoginInfo.setEmployeePersonalInfo(employeePersonalInfo);
								employeePersonalInfo.setEmployeeOfficialInfo(employeeOfficialInfo);
								employeePersonalInfoList.add(employeePersonalInfo);
								employeeLoginInfoList.add(employeeLoginInfo);
								return CompletableFuture.runAsync(() -> {
									MailDto mailDto = new MailDto();
									mailDto.setTo(employee.getOfficialEmailId());
									mailDto.setSubject("Your Credentials For ZealHR");
									mailDto.setBody("Dear " + employee.getFirstName() + ",\r\n" + "\r\n"
											+ "Please login and access ZealHR. If you are unable to login, please share a screenshot of the error page to projectmailer2021@gmail.com"
											+ "\r\n" + "\r\n" + "Your Company Code :"
											+ employeePersonalInfo.getCompanyInfo().getCompanyCode() + "\r\n"
											+ "Your Email :" + employee.getOfficialEmailId() + "\r\n" + "\r\n"
											+ "Kindly use the official email id for any further communications."
											+ "\r\n" + "Thanks and Regards," + "\r\n" + "Team ZealHR");
									emailService.sendMail(mailDto);
								});
							} else {
								designationNotFoundForDepartmentList.add(employee.getFirstName());
								return new CompletableFuture<>();
							}

						} else {
							departmentNotFoundList.add(employee.getFirstName());
							return new CompletableFuture<>();
						}
					}).collect(Collectors.toList());
			employeePersonalInfoRepository.saveAll(employeePersonalInfoList);
			employeeLoginInfoRepo.saveAll(employeeLoginInfoList);

			String result = "";
			if (!departmentNotFoundList.isEmpty()) {
				result = result + "Departments Not Present for : "
						+ StringUtils.join(departmentNotFoundList.toArray(), ',') + ". ";
			}
			if (!designationNotFoundForDepartmentList.isEmpty()) {
				result = result + "Designations Not Present for : "
						+ StringUtils.join(designationNotFoundForDepartmentList.toArray(), ',') + ". ";
			}

			return result.isEmpty() ? "Employee Details Uploaded Successfully" : result;

		} catch (Exception exception) {
			exception.printStackTrace();
			throw new DataNotFoundException(exception.getMessage());
		}
	}

}
