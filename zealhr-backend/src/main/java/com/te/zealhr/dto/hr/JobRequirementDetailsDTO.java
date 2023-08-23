package com.te.zealhr.dto.hr;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobRequirementDetailsDTO {

	private Long requirementId;

	private String jobRole;

	private String jobDescription;

	private Long noOfOpenings;

	private Long closedSlots;

	private List<String> skills;

}
