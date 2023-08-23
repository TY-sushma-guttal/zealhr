package com.te.zealhr.service.employee;

import java.util.List;
import java.util.Map;

import com.te.zealhr.dto.employee.EmployeeAdvanceSalaryDTO;
import com.te.zealhr.dto.employee.EmployeePerformanceDetailsDTO;
import com.te.zealhr.dto.employee.EmployeePerformanceInfoDTO;
import com.te.zealhr.dto.hr.CanlenderRequestDTO;

public interface EmployeesAdvanceSalaryService {

	EmployeeAdvanceSalaryDTO saveAdvanceSalaryRequest(Long companyId, EmployeeAdvanceSalaryDTO advanceSalaryDTO,
			Long employeeInfoId);

	EmployeeAdvanceSalaryDTO getAdvanceSalary(Long advanceSalaryId, Long companyId);

	List<EmployeeAdvanceSalaryDTO> getAdvanceSalaryDTOList(CanlenderRequestDTO canlenderRequestDTO, Long employeeInfoId,
			Long companyId);

	void deleteAdvanceSalaryRequest(Long advanceSalaryId, Long companyId);

	EmployeeAdvanceSalaryDTO editAdvanceSalaryRequest(EmployeeAdvanceSalaryDTO advanceSalaryDTO, Long advanceSalaryId,
			Long companyId);

	Map<String, EmployeePerformanceInfoDTO> getPerformanceMonthly(EmployeePerformanceDetailsDTO performanceDto,
			Long companyId);
}
