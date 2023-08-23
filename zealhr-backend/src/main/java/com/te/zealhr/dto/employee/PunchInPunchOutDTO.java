package com.te.zealhr.dto.employee;

import java.io.Serializable;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PunchInPunchOutDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LocalTime punchIn;

	private LocalTime punchOut;

	private Boolean isPresentInOffice;
	
	private Boolean isHoliday;
	
	private Boolean isOnLeave;

}
