package com.te.zealhr.controller.hr.mongo;

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
import com.te.zealhr.service.hr.mongo.HRTimesheetApprovalService;
import com.te.zealhr.audit.BaseConfigController;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RequestMapping("api/v1/hr/timesheet-approval")
@RestController
@RequiredArgsConstructor
public class HRTimesheetApprovalController extends BaseConfigController {

	private final HRTimesheetApprovalService timesheetApprovalService;

	@GetMapping("/{status}")
	public ResponseEntity<SuccessResponse> getAllTimesheetDetails(@PathVariable String status) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE)
						.message("Fetch All Timesheet Detalis Successfully!!!")
						.data(timesheetApprovalService.getAllEmployeeTimesheetDetails(getCompanyId(), status, getUserId())).build());
	}

	@GetMapping("by-id/{timesheetObjectId}")
	public ResponseEntity<SuccessResponse> getTimesheetDetails(@PathVariable String timesheetObjectId) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Fetch Timesheet Detalis Successfully!!!")
						.data(timesheetApprovalService.getEmployeeTimesheetDetail(timesheetObjectId, getCompanyId()))
						.build());
	}

	@PutMapping("/{timesheetObjectId}")
	public ResponseEntity<SuccessResponse> updateTimesheetDetails(
			@RequestBody AdminApprovedRejectDto adminApprovedRejectDto, @PathVariable String timesheetObjectId) {
		
		String updateEmployeeTimesheetDetails = timesheetApprovalService.updateEmployeeTimesheetDetails(getCompanyId(), timesheetObjectId,
				getEmployeeId(), adminApprovedRejectDto);

		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message(updateEmployeeTimesheetDetails)
						.data(updateEmployeeTimesheetDetails)
						.build());
	}

}
