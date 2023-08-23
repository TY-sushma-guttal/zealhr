package com.te.zealhr.exception.admin;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

public class CompanyNotExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CompanyNotExistException(String message) {
		super(message);
	}
}
