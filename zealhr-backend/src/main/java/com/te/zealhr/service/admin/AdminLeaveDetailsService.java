package com.te.zealhr.service.admin;

import java.util.List;

import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.dto.admin.EmployeeLeaveInfoDTO;

public interface AdminLeaveDetailsService {

	String addEmployeeLeaveDetails(Long companyIdLong, Long employeeInfoId, Long leaveAppliedId, String employeeId,
			AdminApprovedRejectDto adminApprovedRejectDto);

	List<EmployeeLeaveInfoDTO> leaveApprovals(Long companyId, String status);

	EmployeeLeaveInfoDTO leaveApproval(Long leaveAppliedId, Long employeeInfoId, Long companyId);

}
