package com.te.zealhr.dto.hr;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class EmployementInformationDTO {
	
	private Long employmentId;
	private String companyName;
	private LocalDate startDate;
	private LocalDate endDate;
	private String designation;
	private String jobDescription;
	private BigDecimal salary;
	private List<ReferencePersonDTO> referencePersonDetails;	
}
