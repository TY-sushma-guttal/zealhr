package com.te.zealhr.dto.project;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SubMilestoneIDNameDTO {
    private Long milestoneId;
    private String milestoneName;
}
