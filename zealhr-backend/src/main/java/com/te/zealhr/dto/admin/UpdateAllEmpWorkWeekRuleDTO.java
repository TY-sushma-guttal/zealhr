package com.te.zealhr.dto.admin;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

//@author Rakesh Kumar Nayak 
@Data
public class UpdateAllEmpWorkWeekRuleDTO implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long workWeekRuleId;
	
	private String ruleName;
	
	private List<Long> officialIdList;

}
