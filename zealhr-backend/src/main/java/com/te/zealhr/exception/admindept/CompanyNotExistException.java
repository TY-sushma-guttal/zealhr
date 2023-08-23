package com.te.zealhr.exception.admindept;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

@SuppressWarnings("serial")
public class CompanyNotExistException extends RuntimeException{

	public CompanyNotExistException (String message) {
		
		super(message);
	}
}
