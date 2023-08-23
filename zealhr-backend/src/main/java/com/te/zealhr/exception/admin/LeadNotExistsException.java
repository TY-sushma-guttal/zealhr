package com.te.zealhr.exception.admin;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

public class LeadNotExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LeadNotExistsException(String message) {
		super(message);
	}

}
