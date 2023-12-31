package com.te.zealhr.controller.reportingmanager;

import static com.te.zealhr.common.reportingmanager.ReportingManagerConstants.APPRISAL_DETAILS_FETCHED_SUCCESSFULLY;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.employee.EmployeeReviseSalaryDTO;
import com.te.zealhr.dto.reportingmanager.AppraisalMeetingFeedbackDTO;
import com.te.zealhr.dto.reportingmanager.AppraisalMeetingListDto;
import com.te.zealhr.dto.reportingmanager.EmployeeDetailsDTO;
import com.te.zealhr.entity.employee.EmployeePersonalInfo;
import com.te.zealhr.entity.employee.EmployeeReportingInfo;
import com.te.zealhr.entity.employee.EmployeeReviseSalary;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.reportingmanager.ReportingManagerAppraisalMeetingService;
import com.te.zealhr.audit.BaseConfigController;

import static com.te.zealhr.common.reportingmanager.ReportingManagerConstants.APPRAISAL_MEETING_FEEDBACK_DETAILS_UPDATED_SUCCESSFULLY;
/**
 * 
 * @author Ravindra
 *
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/reportingManager")
public class ReportingManagerAppraisalMeetingController extends BaseConfigController {
	@Autowired
	ReportingManagerAppraisalMeetingService service;
	
	
	
	@GetMapping("/team")
	public  ResponseEntity<SuccessResponse> teamAppraisalMeeting(@RequestParam String date){
		   List<AppraisalMeetingListDto> team = service.teamAppraisalMeeting(getUserId(),date);
		return new ResponseEntity<>(new SuccessResponse(false, APPRISAL_DETAILS_FETCHED_SUCCESSFULLY, team),
				HttpStatus.OK);

	}
	@GetMapping("/employeeAppraisalDetail")
	public ResponseEntity<SuccessResponse> employeeDetail(@RequestParam Long meetingId){
		EmployeeDetailsDTO employeeDetail = service.employeeDetail(meetingId,getUserId());
		return new ResponseEntity<>(new SuccessResponse(false, APPRISAL_DETAILS_FETCHED_SUCCESSFULLY, employeeDetail),
				HttpStatus.OK);
	}
	@PostMapping("/appraisalMeetingFeedback")
	public ResponseEntity<SuccessResponse> appraisalMeetingFeedback(@RequestBody AppraisalMeetingFeedbackDTO FeedbackDTO){
		EmployeeReviseSalaryDTO appraisalMeetingFeedback = service.appraisalMeetingFeedback(FeedbackDTO);
		return new ResponseEntity<>(new SuccessResponse(false, APPRAISAL_MEETING_FEEDBACK_DETAILS_UPDATED_SUCCESSFULLY, appraisalMeetingFeedback),
				HttpStatus.OK);
	}
	

}
