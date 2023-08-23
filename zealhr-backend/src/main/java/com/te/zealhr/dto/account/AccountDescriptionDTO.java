package com.te.zealhr.dto.account;

import java.io.Serializable;

import lombok.Data;
@SuppressWarnings("serial")
@Data
public class AccountDescriptionDTO implements Serializable{
	
	private Long objectId;
	private String description;

}
