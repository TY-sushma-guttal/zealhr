package com.te.zealhr.dto.hr;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateListDTO {
	
	private Long candidateId;
	
	private String firstName;

	private String emailId;

	private Long mobileNumber;

	private String designationName;

	private BigDecimal yearOfExperience;

	private String employementStatus;

}
