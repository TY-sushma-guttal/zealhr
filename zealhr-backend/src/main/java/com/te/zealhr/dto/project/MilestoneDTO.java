package com.te.zealhr.dto.project;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Id;

import com.te.zealhr.dto.project.mongo.SubMilestone;

import lombok.Data;

@Data
public class MilestoneDTO {
	
	
	private String mileStoneObjectId;
	private Long projectId;
	private Long milestoneId;
	private String milestoneName;
	private String milestoneDescription;
	private LocalDate dueDate;
	private Double amountToBeReceived;
	private LocalDate deliveredDate;
	private Double amountReceived;
	private String status;
	private String clientFeedback;
	private Integer delayedBy;
	private List<SubMilestone> subMilestone;
}
