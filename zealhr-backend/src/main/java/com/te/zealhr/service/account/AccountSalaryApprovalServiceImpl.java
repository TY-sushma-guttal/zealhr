package com.te.zealhr.service.account;

import static com.te.zealhr.common.account.AccountConstants.EMPLOYEE_SALARY_DETAILS_NOT_FOUND;
import static com.te.zealhr.common.account.AccountConstants.EMPLOYEE_SALARY_FINALIZE_SUCCESSFULLY;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.zealhr.dto.account.AccountSalaryDTO;
import com.te.zealhr.dto.account.AccountSalaryInputDTO;
import com.te.zealhr.dto.account.MarkAsPaidInputDTO;
import com.te.zealhr.dto.account.MarkAsPaidSalaryListDTO;
import com.te.zealhr.entity.admin.CompanyDepartmentDetails;
import com.te.zealhr.entity.employee.EmployeeOfficialInfo;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.employee.EmployeeSalaryDetails;
import com.te.zealhr.exception.account.CustomExceptionForAccount;
import com.te.zealhr.repository.admin.CompanyDepartmentDetailsRepository;
import com.te.zealhr.repository.admin.CompanyInfoRepository;
import com.te.zealhr.repository.employee.EmployeeSalaryDetailsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountSalaryApprovalServiceImpl implements AccountSalaryApprovalService {
	@Autowired
	private EmployeeSalaryDetailsRepository employeeSalaryDetailsRepo;
	@Autowired
	private CompanyInfoRepository companyInfoRepo;
	@Autowired
	private EmployeeSalaryDetailsRepository salaryDetailsRepo;
	@Autowired
	private CompanyDepartmentDetailsRepository departmentRepo;

	@Override
	public List<MarkAsPaidSalaryListDTO> finalizeSalary(MarkAsPaidInputDTO finalizesalaryInputDTO, Long companyId) {
		
		ArrayList<Long> salaryIdList = finalizesalaryInputDTO.getAdvanceSalaryId();
		List<EmployeeSalaryDetails> salaryDetails = employeeSalaryDetailsRepo
				.findByEmployeeSalaryIdInAndCompanyInfoCompanyId(salaryIdList, companyId);
		if (salaryDetails == null || salaryDetails.isEmpty()) {
			throw new CustomExceptionForAccount(EMPLOYEE_SALARY_DETAILS_NOT_FOUND);
		}
		List<MarkAsPaidSalaryListDTO> finalizeList = new ArrayList<>();
		for (EmployeeSalaryDetails employeeSalaryDetails : salaryDetails) {
			employeeSalaryDetails.setIsFinalized(true);
			EmployeeSalaryDetails employeeSalary = employeeSalaryDetailsRepo.save(employeeSalaryDetails);
			MarkAsPaidSalaryListDTO markAsPaidSalaryListDTO = new MarkAsPaidSalaryListDTO();
			BeanUtils.copyProperties(employeeSalary, markAsPaidSalaryListDTO);
			finalizeList.add(markAsPaidSalaryListDTO);
		}
		log.info(EMPLOYEE_SALARY_FINALIZE_SUCCESSFULLY);
		return finalizeList;
	}

	@Override
	public List<AccountSalaryDTO> salaryApproval(AccountSalaryInputDTO accountSalaryInputDTO) {

		List<EmployeeSalaryDetails> salaryDetails;
		if (accountSalaryInputDTO.getDepartment() == null || accountSalaryInputDTO.getDepartment().isEmpty()) {
			salaryDetails = salaryDetailsRepo.findByCompanyInfoCompanyIdAndMonthInAndIsFinalizedFalse(
					accountSalaryInputDTO.getCompanyId(), accountSalaryInputDTO.getMonth());
		} else {
			List<Long> departmentIdList = accountSalaryInputDTO.getDepartment().stream().map(Long::parseLong)
					.collect(Collectors.toList());
			List<CompanyDepartmentDetails> findAllById = departmentRepo.findAllById(departmentIdList);
			
			List<String> departmentNameList = findAllById.stream().map(CompanyDepartmentDetails::getCompanyDepartmentName)
					.collect(Collectors.toList());
			salaryDetails = salaryDetailsRepo
					.findByCompanyInfoCompanyIdAndMonthInAndIsFinalizedFalseAndEmployeePersonalInfoEmployeeOfficialInfoDepartmentIn(
							accountSalaryInputDTO.getCompanyId(), accountSalaryInputDTO.getMonth(), departmentNameList);
		}
		// method for adding data
		List<AccountSalaryDTO> addData = addData(salaryDetails);

		log.info("employees Salary details fetch successfully");
		return addData;
	}

	private List<AccountSalaryDTO> addData(List<EmployeeSalaryDetails> salaryDetails) {
		ArrayList<AccountSalaryDTO> dropDownlist = new ArrayList<>();
		Double totalDeduction = 0.0d;
		Double totalAdditional = 0.0d;
		for (EmployeeSalaryDetails employeeSalaryDetails : salaryDetails) {
			Map<String, String> deduction = employeeSalaryDetails.getDeduction();
			Map<String, String> additional = employeeSalaryDetails.getAdditional();
			EmployeePersonalInfo employeePersonalInfo = employeeSalaryDetails.getEmployeePersonalInfo();
			EmployeeOfficialInfo employeeOfficialInfo = employeePersonalInfo.getEmployeeOfficialInfo();
			if ((employeePersonalInfo != null) && (employeeOfficialInfo != null)) {
				String dummyStatus;
				if (Boolean.TRUE.equals(employeeSalaryDetails.getIsPayslipGenerated())) {
					dummyStatus = "Payslip Generated";
				} else if (Boolean.TRUE.equals(employeeSalaryDetails.getIsPaid())) {
					dummyStatus = "Paid";
				} else if (Boolean.TRUE.equals(employeeSalaryDetails.getIsFinalized())) {
					dummyStatus = "Finalised";
				} else {
					dummyStatus = "Pending";
				}

				String lop = null;
				if (additional != null) {
					for (Entry<String, String> entry : additional.entrySet()) {
						Double parseDouble = Double.parseDouble(entry.getValue());
						totalAdditional += parseDouble;
					}
				}
				if (deduction != null) {
					for (Map.Entry<String, String> entry : deduction.entrySet()) {
						Double parseDouble = Double.parseDouble(entry.getValue());
						totalDeduction += parseDouble;

					}
				}
				if ((deduction != null)) {
					for (Map.Entry<String, String> entry : deduction.entrySet()) {
						if (entry.getKey().equalsIgnoreCase("lop")) {
							lop = entry.getValue();
							break;
						}
					}
				}

				lop = (lop == null) ? Integer.toString(0) : lop;
				dropDownlist.add(new AccountSalaryDTO(employeeOfficialInfo.getEmployeeId(),
						employeePersonalInfo.getFirstName(),
						employeeSalaryDetails.getTotalSalary(), employeeSalaryDetails.getAdditional(),
						employeeSalaryDetails.getDeduction(), lop, employeeSalaryDetails.getNetPay(), dummyStatus,
						employeeSalaryDetails.getEmployeeSalaryId(), totalAdditional == null ? null : totalAdditional,
						totalDeduction == null ? null : totalDeduction,
						Month.of(employeeSalaryDetails.getMonth()).name()));
				totalAdditional = 0.0d;
				totalDeduction = 0.0d;
			}

		}
		return dropDownlist;
	}
}
