package com.te.zealhr.dto.project.mongo;

import lombok.Data;

@Data
public class UnAssignTaskDTO {
	private Long projectId;
	private String taskId;
	private String reason;


}
