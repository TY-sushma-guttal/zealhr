package com.te.zealhr.service.reportingmanager;

import java.util.List;

import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.dto.reportingmanager.ResignationRequestApprovalDTO;

public interface ReportingManagerResignationApprovalService {

	List<ResignationRequestApprovalDTO> getResignationRequestList(String status,Long employeeInfoId, Long companyId);
	
	ResignationRequestApprovalDTO getResignationRequest(Long resignationId,Long companyId);
	
	String addResignationResponse(Long companyId,Long resignationId,String employeeId,AdminApprovedRejectDto adminApprovedRejectDto);
	
}
