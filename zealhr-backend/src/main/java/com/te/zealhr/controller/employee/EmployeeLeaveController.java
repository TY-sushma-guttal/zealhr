package com.te.zealhr.controller.employee;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.employee.EmployeeAllotedLeavesDTO;
import com.te.zealhr.dto.employee.EmployeeApplyLeaveDTO;
import com.te.zealhr.dto.employee.EmployeeCalanderDetailsDTO;
import com.te.zealhr.dto.employee.EmployeeCalenderLeaveInfoDTO;
import com.te.zealhr.dto.employee.EmployeeDropdownDTO;
import com.te.zealhr.dto.employee.EmployeeLeaveDTO;
import com.te.zealhr.dto.employee.mongo.EmployeeCalendarDTO;
import com.te.zealhr.dto.hr.CanlenderRequestDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.employee.EmployeeLeaveService;

import kotlin.collections.ArrayDeque;

import com.te.zealhr.audit.BaseConfigController;

@RestController
@RequestMapping("api/v1/employee-leave")
@CrossOrigin(origins = "*")
public class EmployeeLeaveController extends BaseConfigController {

	@Autowired
	private EmployeeLeaveService appliedService;

	// To add new leave request
	@PostMapping("/apply-leave")
	public ResponseEntity<SuccessResponse> applyLeave(@RequestBody EmployeeApplyLeaveDTO applyLeave) {

		EmployeeApplyLeaveDTO leaveDto = appliedService.saveLeaveApplied(applyLeave, getUserId());
		if (leaveDto != null) {
			return new ResponseEntity<>(SuccessResponse.builder().data(leaveDto).error(false)
					.message("Leave Request Raised Successfully!!!").build(), HttpStatus.OK);
		} else
			return new ResponseEntity<>(SuccessResponse.builder().data(leaveDto).error(true)
					.message("Unable to raise leave request.").build(), HttpStatus.OK);

	}

	@PutMapping("/{leaveAppliedId}")
	public ResponseEntity<SuccessResponse> editLeave(@PathVariable Long leaveAppliedId,
			@RequestBody EmployeeApplyLeaveDTO applyLeaveDto) {
		EmployeeApplyLeaveDTO editLeave = appliedService.editLeave(applyLeaveDto, leaveAppliedId, getUserId());
		if (editLeave != null) {
			return new ResponseEntity<>(SuccessResponse.builder().data(editLeave).error(false)
					.message("Leave details updated successfully").build(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(SuccessResponse.builder().data(editLeave).error(true)
					.message("Leave details not updated successfully").build(), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{leaveAppliedId}")
	public ResponseEntity<SuccessResponse> deleteLeave(@PathVariable Long leaveAppliedId) {
		Boolean result = appliedService.deleteLeave(leaveAppliedId, getUserId());
		if (result == Boolean.TRUE) {
			return new ResponseEntity<>(SuccessResponse.builder().error(false).message("Deleted successfully").build(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					SuccessResponse.builder().error(true).message("Leave Request Not Found").build(),
					HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/leaves-list/{status}")
	public ResponseEntity<SuccessResponse> getLeavesList(@PathVariable String status,
			@RequestBody CanlenderRequestDTO calCanlenderRequestDTO) {

		List<EmployeeApplyLeaveDTO> leavesList = appliedService.getLeavesList(status, getUserId(),
				calCanlenderRequestDTO, new ArrayList<>());
		if (!leavesList.isEmpty()) {
			return new ResponseEntity<>(SuccessResponse.builder().data(leavesList).error(false).message("").build(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(leavesList).error(false).message("No deatils found").build(),
					HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/leave-by-id")
	public ResponseEntity<SuccessResponse> getLeaveById(@RequestParam Long leaveAppliedId) {
		EmployeeLeaveDTO employeeLeaveDTO = appliedService.getLeaveById(leaveAppliedId, getUserId());
		if (employeeLeaveDTO != null) {
			return new ResponseEntity<>(
					SuccessResponse.builder().data(employeeLeaveDTO).error(false).message("").build(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(SuccessResponse.builder().data(employeeLeaveDTO).error(true)
					.message("No leave request found").build(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/alloted-leaves-list")
	public ResponseEntity<SuccessResponse> getAllotedLeavesList() {
		List<EmployeeAllotedLeavesDTO> leavesList = appliedService.getAllotedLeavesList(getUserId());
		if (!leavesList.isEmpty()) {
			return new ResponseEntity<>(SuccessResponse.builder().data(leavesList).error(false).message(null).build(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(SuccessResponse.builder().data(leavesList).error(true).message(null).build(),
					HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/calender")
	public ResponseEntity<SuccessResponse> getCalenderDetails(@RequestBody CanlenderRequestDTO calCanlenderRequestDTO) {
		List<EmployeeCalenderLeaveInfoDTO> allCalenderDetails = appliedService.getAllCalenderDetails(getUserId(),
				getCompanyId(), calCanlenderRequestDTO);
		return new ResponseEntity<>(
				SuccessResponse.builder().data(allCalenderDetails).error(false).message(null).build(), HttpStatus.OK);
	}

	@GetMapping("/leave-type-dropdown")
	public ResponseEntity<SuccessResponse> getLeaveTypesDropdown() {
		List<String> leaveTypes = appliedService.getLeaveTypesDropdown(getUserId());

		return new ResponseEntity<>(SuccessResponse.builder().data(leaveTypes).error(false).message(null).build(),
				HttpStatus.OK);
	}

	@GetMapping("/reporting-manager")
	public ResponseEntity<SuccessResponse> getReportingManager() {
		EmployeeDropdownDTO reportingManager = appliedService.getReportingManager(getUserId());

		return new ResponseEntity<>(SuccessResponse.builder().data(reportingManager).error(false).message(null).build(),
				HttpStatus.OK);
	}

}