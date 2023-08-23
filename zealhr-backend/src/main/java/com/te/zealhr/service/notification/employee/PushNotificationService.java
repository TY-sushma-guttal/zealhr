package com.te.zealhr.service.notification.employee;

import com.te.zealhr.dto.employee.EmployeeExpoTokenDTO;

public interface PushNotificationService {
	
	void pushMessage(String title, String message, String recipient);
	
	Boolean updateExpoToken(EmployeeExpoTokenDTO employeeExpoTokenDTO);

}
