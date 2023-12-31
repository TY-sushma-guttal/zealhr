package com.te.zealhr.dto.employee;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLoginResponseDto implements Serializable {
	private String employeeId;
	private Long companyId;
	private Long employeeInfoId;
	private String name;
	private String designation;
	private String department;
	private String logo;
	private Object roles;
//	private String dateFormat;
	private String msg;
	private String accessToken;
	private String refreshToken;
	private Double availableLeaves;
	private Double usedLeaves;
//	private String timesheet;
	private Boolean isChatEnabled;
	private String fiscalMonth;
	private Double appliedLeaves;
	private Double rejectedLeaves;
	private Double allottedLeaves;
	private String pictureURL;
	private List<LeaveDetailsDTO> leaveDetails;
	private String attendanceType;
	private Boolean isRegularizationEnabled;
	private Boolean isIdAutoGenerated;
	private Boolean isDateRequiredInId;
	private Boolean isReportingManager;
}