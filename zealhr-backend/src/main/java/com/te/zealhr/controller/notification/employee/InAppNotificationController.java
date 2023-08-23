package com.te.zealhr.controller.notification.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.audit.BaseConfigController;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.notification.employee.InAppNotificationService;

@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/notification")
@RestController
public class InAppNotificationController extends BaseConfigController {

	@Autowired
	private InAppNotificationService inAppNotificationService;

	@GetMapping("/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> getNotification(@PathVariable Long employeeInfoId) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Notification Fetched Successfully")
						.data(inAppNotificationService.getNotification(employeeInfoId)).build());
	}

	@PutMapping("/{employeeInfoId}")
	public ResponseEntity<SuccessResponse> updateNotification(@PathVariable Long employeeInfoId) {
		return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.builder().error(false).message("Updated")
				.data(inAppNotificationService.updateNotification(employeeInfoId)).build());

	}

	@GetMapping("/events/announcements/wishes")
	public ResponseEntity<SuccessResponse> getCompanyEventsAndAnnouncementsAndWishes() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(SuccessResponse.builder().error(false).message("").data(
						inAppNotificationService.getCompanyEventsAndAnnouncementsAndWishes(getUserId(), getCompanyId()))
						.build());

	}

}
