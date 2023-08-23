package com.te.zealhr.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TermsAndConditionDTO {

	private Long termsAndConditionId;

	private String type;

	private String description;

}
