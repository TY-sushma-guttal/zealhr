package com.te.zealhr.dto.hr;

import java.util.Map;

import lombok.Data;
@Data
public class UpdateFeedbackDTO {
	private Map<String, String> feedback;
	private Long companyId;

}
