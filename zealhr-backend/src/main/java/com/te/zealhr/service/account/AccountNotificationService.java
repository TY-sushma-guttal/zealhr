package com.te.zealhr.service.account;

import java.util.ArrayList;
import java.util.List;

import com.te.zealhr.dto.account.AccountMarkAsPaidDTO;
import com.te.zealhr.dto.account.AccountPaySlipDTO;
import com.te.zealhr.dto.account.AccountPaySlipListDTO;
import com.te.zealhr.dto.account.AccountReimbursementMarkAsPaidDTO;
import com.te.zealhr.dto.account.AccountSalaryDTO;
import com.te.zealhr.dto.account.AccountSalaryInputDTO;
import com.te.zealhr.dto.account.AdvanceSalaryDTO;
import com.te.zealhr.dto.account.GeneratePayslipInputDTO;
import com.te.zealhr.dto.account.MarkAsPaidInputDTO;
import com.te.zealhr.dto.account.MarkAsPaidSalaryDTO;
import com.te.zealhr.dto.account.MarkAsPaidSalaryListDTO;
import com.te.zealhr.dto.account.MarkAsReimbursedDTO;
import com.te.zealhr.dto.account.ReimbursementInfoByIdDTO;
import com.te.zealhr.dto.account.ReimbursementListDTO;
import com.te.zealhr.dto.employee.EmployeeReviseSalaryDTO;
import com.te.zealhr.entity.employee.EmployeeReimbursementInfo;

public interface AccountNotificationService {

	ArrayList<ReimbursementListDTO> reimbursement(Long companyId);

	ReimbursementInfoByIdDTO reimbursementById(Long companyId, Long reimbursementId);

	List<AccountMarkAsPaidDTO> markAsPaidAdvanceSalary(Long companyId, MarkAsPaidInputDTO advanceSalaryId);

	ArrayList<AccountReimbursementMarkAsPaidDTO> markAsReimbursed(Long companyId, MarkAsReimbursedDTO reimbursementIdList);

	ArrayList<MarkAsPaidSalaryListDTO> markAsPaidSalary(MarkAsPaidSalaryDTO markAsPaidSalaryDTO, Long companyId);

	ArrayList<MarkAsPaidSalaryListDTO> generatePaySlip(Long companyId, GeneratePayslipInputDTO employeeSalaryIdList);

	AccountPaySlipDTO paySlipDetailsById(Long employeeSalaryId, Long companyId);
	
	List<AdvanceSalaryDTO> advanceSalary(Long companyId);
	
	List<AccountSalaryDTO> salaryDetailsList(Long companyId);
	
	List<AccountPaySlipListDTO> paySlip(Long companyId);
	
	List<EmployeeReviseSalaryDTO> reviseSalary(Long companyId);


}
