package com.te.zealhr.controller.sales;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.DashboardRequestDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.sales.SalesDashboardService;
import com.te.zealhr.audit.BaseConfigController;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/sales/dashboard")
@RequiredArgsConstructor
@RestController
public class SalesDashboardController extends BaseConfigController {

	@Autowired
	private SalesDashboardService dashboardService;

	@PostMapping("/client")
	public ResponseEntity<SuccessResponse> getClientDetails(@RequestBody DashboardRequestDTO dashboardRequestDTO) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Dashboard details fetched")
						.data(dashboardService.getClientDetails(dashboardRequestDTO, getCompanyId())).build());
	}

	@PostMapping("/client-details")
	public ResponseEntity<SuccessResponse> getClientDetailsByStatus(
			@RequestBody DashboardRequestDTO dashboardRequestDTO) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("Dashboard details fetched")
						.data(dashboardService.getClientDetailsByStatus(getCompanyId(), dashboardRequestDTO.getType()))
						.build());
	}

}
