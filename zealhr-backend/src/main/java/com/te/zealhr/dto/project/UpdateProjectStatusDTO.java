package com.te.zealhr.dto.project;

import lombok.Data;

@Data
public class UpdateProjectStatusDTO {

	private Long projectId;

	private String status;

	private String rejectionReason;

}
