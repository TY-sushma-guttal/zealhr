package com.te.zealhr.exception;
/**
 * 
 * @author Manish Kumar
 * */

public class SoftwareAlreadyAvailableException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SoftwareAlreadyAvailableException(String message) {
		super(message);
	}

}
