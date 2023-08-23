package com.te.zealhr.controller.hr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.audit.BaseConfigController;
import com.te.zealhr.dto.DashboardRequestDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.employee.HRLeaveService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/hr/leave")
public class HRLeaveController extends BaseConfigController {

	@Autowired
	private HRLeaveService hrLeaveService;

	@PostMapping
	public ResponseEntity<SuccessResponse> getLeaveDetails(@RequestBody DashboardRequestDTO dashboardRequestDTO) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Leave Details Fetched Successfully")
						.data(hrLeaveService.getLeaveDetails(dashboardRequestDTO, getCompanyId())).build());
	}

}
