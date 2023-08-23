package com.te.zealhr.dto.hr.mongo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HRTicketsBasicDTO {
	
	private String ticketObjectId;
	
	private Long hrTicketId;

	private String category;
	
	private LocalDate raisedDate;
	
	private String ticketOwner;

	private String status;
	
	private String description;
	
	private String reportingManager;
	
	private String department;
}
