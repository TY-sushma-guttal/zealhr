package com.te.zealhr.service.reportingmanager;

import java.util.List;

import com.te.zealhr.dto.employee.EmployeeReviseSalaryDTO;
import com.te.zealhr.dto.reportingmanager.AppraisalMeetingFeedbackDTO;
import com.te.zealhr.dto.reportingmanager.AppraisalMeetingListDto;
import com.te.zealhr.dto.reportingmanager.EmployeeDetailsDTO;

public interface ReportingManagerAppraisalMeetingService {

	List<AppraisalMeetingListDto> teamAppraisalMeeting(Long userId, String date);

	EmployeeDetailsDTO employeeDetail(Long meetingId, Long userId);

	EmployeeReviseSalaryDTO appraisalMeetingFeedback(AppraisalMeetingFeedbackDTO feedbackDTO);

}
