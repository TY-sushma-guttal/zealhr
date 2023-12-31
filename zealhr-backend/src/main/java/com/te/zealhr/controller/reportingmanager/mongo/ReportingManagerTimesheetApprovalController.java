package com.te.zealhr.controller.reportingmanager.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.admin.AdminApprovedRejectDto;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.reportingmanager.mongo.ReportingManagerTimesheetApprovalService;
import com.te.zealhr.audit.BaseConfigController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/rm/timesheet-approval")
public class ReportingManagerTimesheetApprovalController extends BaseConfigController {

	@Autowired
	private ReportingManagerTimesheetApprovalService service;
	
	@GetMapping("/all/{status}/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> getTimesheetDetailsList(@PathVariable String status,@PathVariable Long employeeInfoId) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Data Fetched Sucessfully")
						.data(service.getEmployeeTimesheetList(getCompanyId(), status, employeeInfoId)).build());
	}
	
	@GetMapping("/{timesheetObjectId}")
	public ResponseEntity<SuccessResponse> getTimesheetDetails(@PathVariable String timesheetObjectId) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Data Fetched Sucessfully")
						.data(service.getEmployeetimesheetDetails(timesheetObjectId, getCompanyId())).build());
	}
	
	@PutMapping("/{timesheetObjectId}/{employeeId}")
	public ResponseEntity<SuccessResponse> addResponseToTimesheet(@PathVariable String timesheetObjectId,@PathVariable String employeeId,
			@RequestBody AdminApprovedRejectDto adminApprovedRejectDto) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Response Added Sucessfully")
						.data(service.addResponseToTimesheet(timesheetObjectId, getCompanyId(), employeeId, adminApprovedRejectDto)).build());
	}
}
