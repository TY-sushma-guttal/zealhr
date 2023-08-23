package com.te.zealhr.dto.hr;

import lombok.Data;

@Data
public class RejectCandidateRequestDTO {
	private Long personalId;
	private String rejectedBy;
	private String reason;
}
