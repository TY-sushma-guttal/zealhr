package com.te.zealhr.controller.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.audit.BaseConfigController;
import com.te.zealhr.dto.hr.CanlenderRequestDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.employee.RaisedTicketService;

import io.swagger.v3.oas.annotations.Operation;

@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/raised/tickets")
@RestController
public class RaisedTicketController extends BaseConfigController {

	@Autowired
	private RaisedTicketService raisedTicketService;

	@Operation(summary = "This API is used to fetch all the tickets raised by an employee")
	@PostMapping("/get/all")
	public ResponseEntity<SuccessResponse> getAllRaisedTickets(@RequestBody CanlenderRequestDTO canlenderRequestDTO) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder()
						.data(raisedTicketService.getAllRaisedTickets(canlenderRequestDTO, getUserId(), getCompanyId()))
						.message("Employee Raised Tickets Are Fetched Successfully!!").error(false).build());
	}

	@Operation(summary = "This API is used to fetch pending tickets raised by employee's team")
	@GetMapping("/get/as-manager")
	public ResponseEntity<SuccessResponse> getTeamPendingTickets() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().data(raisedTicketService.getTeamPendingTickets(getUserId()))
						.message("Tickets Fetched Successfully!!").error(false).build());
	}

}
