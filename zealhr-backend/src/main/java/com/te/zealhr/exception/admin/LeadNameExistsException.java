package com.te.zealhr.exception.admin;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

public class LeadNameExistsException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public LeadNameExistsException(String message) {
		super(message);
	}

}
