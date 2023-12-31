package com.te.zealhr.controller.notification.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.te.zealhr.dto.employee.EmployeeExpoTokenDTO;
import com.te.zealhr.response.SuccessResponse;
import com.te.zealhr.service.notification.employee.PushNotificationServiceImpl;

@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/notification")
@RestController
public class PushNotificationController {

	@Autowired
	private PushNotificationServiceImpl pushNotificationService;

	@PutMapping("/token")
	public ResponseEntity<SuccessResponse> getNotification(@RequestBody EmployeeExpoTokenDTO employeeExpoTokenDTO) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(SuccessResponse.builder().error(Boolean.FALSE).message("Expo Token Updated Successfully")
						.data(pushNotificationService.updateExpoToken(employeeExpoTokenDTO)).build());
	}

}
