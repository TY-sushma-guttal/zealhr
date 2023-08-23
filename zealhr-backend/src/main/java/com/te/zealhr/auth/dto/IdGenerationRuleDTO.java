package com.te.zealhr.auth.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class IdGenerationRuleDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private String value;

}
