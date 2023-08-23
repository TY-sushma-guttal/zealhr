package com.te.zealhr.service.employee;

import java.util.List;

import com.te.zealhr.dto.employee.EmployeeReimbursementDTO;
import com.te.zealhr.dto.employee.EmployeeReimbursementExpenseCategoryDTO;
import com.te.zealhr.dto.hr.CanlenderRequestDTO;

public interface EmployeeReimbursementService {

	List<EmployeeReimbursementExpenseCategoryDTO> findByExpenseCategoryId(Long companyId);

	EmployeeReimbursementDTO saveEmployeeReimbursement(EmployeeReimbursementDTO reimbursementDTO, Long employeeInfoId,
			Long companyId);

	List<EmployeeReimbursementDTO> getReimbursementDTOList(CanlenderRequestDTO canlenderRequestDTO, Long employeeInfoId,
			Long companyId, String status);

	EmployeeReimbursementDTO getEmployeeReimbursement(Long employeeInfoId, Long reimbursementId, Long companyId);

	void deleteReimbursementRequest(Long reimbursementId);

	EmployeeReimbursementDTO editReimbursementRequest(EmployeeReimbursementDTO reimbursementDTO, Long reimbursementId);
}
