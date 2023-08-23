package com.te.zealhr.exception.admindept;

public class DuplicateProductNameException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DuplicateProductNameException(String message) {
		super(message);
	}

}

