package com.te.zealhr.service.employee;

import java.time.LocalDate;
import java.util.List;

import com.te.zealhr.dto.employee.EmployeeAllotedLeavesDTO;
import com.te.zealhr.dto.employee.EmployeeApplyLeaveDTO;
import com.te.zealhr.dto.employee.EmployeeCalenderLeaveInfoDTO;
import com.te.zealhr.dto.employee.EmployeeDropdownDTO;
import com.te.zealhr.dto.employee.EmployeeLeaveDTO;
import com.te.zealhr.dto.hr.CanlenderRequestDTO;

public interface EmployeeLeaveService {

	EmployeeApplyLeaveDTO saveLeaveApplied(EmployeeApplyLeaveDTO employeeLeaveApplied, Long employeeInfoId);

	List<EmployeeApplyLeaveDTO> getLeavesList(String status, Long employeeInfoId, CanlenderRequestDTO calCanlenderRequestDTO, List<LocalDate> startEndDate);
	
	List<EmployeeAllotedLeavesDTO> getAllotedLeavesList(Long employeeInfoId);
	
	EmployeeLeaveDTO getLeaveById(Long leaveAppliedId, Long employeeInfoId);

	EmployeeApplyLeaveDTO editLeave(EmployeeApplyLeaveDTO applyLeaveDto, Long leaveAppliedId, Long employeeInfoId);

	Boolean deleteLeave(Long leaveAppliedId, Long employeeInfoId);
	
	List<EmployeeCalenderLeaveInfoDTO> getAllCalenderDetails(Long employeeInfoId, Long companyId, CanlenderRequestDTO calCanlenderRequestDTO);
	
	List<String> getLeaveTypesDropdown(Long employeeInfoId);
	
	EmployeeDropdownDTO getReportingManager(Long employeeInfoId);

}
