package com.te.zealhr.service.admin;

import com.te.zealhr.dto.admin.LevelsOfApprovalDto;

public interface LevelsOfApprovalService {

	String addLevelsOfApproval(LevelsOfApprovalDto approvalDto,Long companyId);

	LevelsOfApprovalDto getLevelsOfApproval(Long companyId);

}
