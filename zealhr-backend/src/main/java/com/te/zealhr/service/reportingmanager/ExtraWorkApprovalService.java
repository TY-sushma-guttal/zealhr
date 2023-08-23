package com.te.zealhr.service.reportingmanager;

import java.util.List;

import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.dto.reportingmanager.ExtraWorkDTO;

public interface ExtraWorkApprovalService {
	
	ExtraWorkDTO getExtraWorkById(Long extraWorkId);
	
	List<ExtraWorkDTO> getAllExtraWorkDetails(Long employeeInfoId, String status);
	
	Boolean updateStatus(Long extraWorkId, AdminApprovedRejectDto adminApprovedRejectDTO);

}
