package com.te.zealhr.controller.sales;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.DepartmentProjectDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.sales.CommonDashboardService;
import com.te.zealhr.audit.BaseConfigController;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/common/dashboard")
@RequiredArgsConstructor
@RestController
public class CommonDashboardController extends BaseConfigController {

	@Autowired
	private CommonDashboardService commonDashboardServuce;

	@PostMapping
	public ResponseEntity<SuccessResponse> getClientDetails(@RequestBody DepartmentProjectDTO departmentProjectDTO) {
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false)
				.message("Dashboard details fetched").data(commonDashboardServuce
						.getEventsBirthdayOtherDetails(getCompanyId(), getEmployeeInfoId(), departmentProjectDTO))
				.build());
	}

	@PostMapping("send-wish/{employeeInfoIdTo}")
	public ResponseEntity<SuccessResponse> sendWishes(@PathVariable Long employeeInfoIdTo) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message(
						commonDashboardServuce.sendWishes(getCompanyId(), getEmployeeInfoId(), employeeInfoIdTo))
						.build());
	}

}