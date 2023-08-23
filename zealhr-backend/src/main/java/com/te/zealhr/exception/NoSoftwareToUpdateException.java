package com.te.zealhr.exception;

/**
 * 
 * @author Manish kumar
 *
 */
public class NoSoftwareToUpdateException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public NoSoftwareToUpdateException(String msg) {
		super(msg);
	}

}
