package com.te.zealhr.service.hr;

import java.util.List;

import com.te.zealhr.dto.employee.EmployeeReviseSalaryDTO;
import com.te.zealhr.dto.employee.ReviseSalaryByIdDTO;
import com.te.zealhr.dto.hr.UpdateReviseSalaryDTO;

public interface NotificationReviseSalaryService {
	public List<EmployeeReviseSalaryDTO> reviseSalary(Long companyId);

	public ReviseSalaryByIdDTO reviseSalaryById(Long companyId, Long reviseSalaryId);

	public UpdateReviseSalaryDTO updateRevisedsalary(UpdateReviseSalaryDTO reviseSalaryDTO);

}
