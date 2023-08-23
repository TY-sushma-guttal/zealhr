package com.te.zealhr.service.reportingmanager;

import java.util.List;

import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.dto.reportingmanager.RegularizationDTO;

public interface RegularizationApprovalService {
	
	List<RegularizationDTO> getAllRegularizationDetails(Long employeeInfoId, Long companyId);
	
	String updateLeaveStatus(Long companyId, Long employeeInfoId, Long regularizationId,
			AdminApprovedRejectDto adminApprovedRejectDto);

}
