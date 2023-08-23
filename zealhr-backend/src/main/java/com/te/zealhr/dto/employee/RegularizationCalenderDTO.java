package com.te.zealhr.dto.employee;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegularizationCalenderDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<EmployeeRegularizationDetailsDTO> regularizationList;

	private Map<LocalDate, PunchInPunchOutDTO> attendanceDetails;

}
