package com.te.zealhr.exception.admin;

public class NotSuchEmployeeAvailableException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public NotSuchEmployeeAvailableException(String message) {
		super(message);
		
	}
}
