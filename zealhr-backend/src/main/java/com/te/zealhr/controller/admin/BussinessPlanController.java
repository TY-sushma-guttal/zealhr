package com.te.zealhr.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.admin.BussinessPlanDTO;
import com.te.zealhr.dto.admin.PlanDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.employee.BussinessPlanService;
import com.te.zealhr.audit.BaseConfigController;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BussinessPlanController extends BaseConfigController {

	private final BussinessPlanService bussinessPlanService;

	@PostMapping("bussiness-plan/{companyId}")
	public ResponseEntity<SuccessResponse> addBussinessPlan(@RequestBody BussinessPlanDTO bussinessPlanDTO,
			@PathVariable(name = "companyId", required = false) Long comapnyId) {
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(Boolean.FALSE)
				.message(bussinessPlanService.addBussinessPlan(bussinessPlanDTO, getTerminalId(), comapnyId)).build());
	}

	@PostMapping("user-plan")
	public ResponseEntity<SuccessResponse> addUserPlan(@RequestBody BussinessPlanDTO bussinessPlanDTO) {
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(Boolean.FALSE)
				.message(bussinessPlanService.addUserPlan(bussinessPlanDTO, getTerminalId())).build());
	}

	@GetMapping("bussiness-plan")
	public ResponseEntity<SuccessResponse> bussinessRegistration() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Fetch Bussiness Plan Details")
						.data(bussinessPlanService.bussinessRegistration(getTerminalId())).build());
	}

	@PostMapping("plan-details")
	public ResponseEntity<SuccessResponse> addPlan(@RequestBody PlanDTO planDTO) {
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(Boolean.FALSE)
				.message(bussinessPlanService.planDTO(getTerminalId(), planDTO)).build());
	}

}
