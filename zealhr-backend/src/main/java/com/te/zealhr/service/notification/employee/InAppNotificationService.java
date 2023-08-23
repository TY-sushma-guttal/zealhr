package com.te.zealhr.service.notification.employee;

import java.util.List;

import com.te.zealhr.dto.employee.EmployeeNotificationDTO;
import com.te.zealhr.dto.sales.EventBirthdayOtherDetailsDTO;

public interface InAppNotificationService {

	List<EmployeeNotificationDTO> getNotification(Long employeeInfoId);

	Boolean updateNotification(Long employeeInfoId);

	EventBirthdayOtherDetailsDTO getCompanyEventsAndAnnouncementsAndWishes(Long employeeInfoId, Long companyId);

}
