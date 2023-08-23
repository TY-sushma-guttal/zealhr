package com.te.zealhr.service.hr;

import java.util.List;

import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.dto.admin.EmployeeLeaveInfoDTO;
import com.te.zealhr.dto.hr.CanlenderRequestDTO;

public interface HRLeaveApprovalService {

	String updateLeaveStatus(Long companyIdLong, Long employeeInfoId, Long leaveAppliedId, String employeeId,
			AdminApprovedRejectDto adminApprovedRejectDto);

	List<EmployeeLeaveInfoDTO> getLeaveDetailsByStatus(Long companyId, Long employeeInfoId, CanlenderRequestDTO canlenderRequestDTO);

	EmployeeLeaveInfoDTO getLeaveDetailsById(Long leaveAppliedId, Long employeeInfoId, Long companyId);
	
}
