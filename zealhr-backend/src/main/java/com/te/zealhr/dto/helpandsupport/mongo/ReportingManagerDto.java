package com.te.zealhr.dto.helpandsupport.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportingManagerDto {
	private String employeeId;
	private String firstName;

}
