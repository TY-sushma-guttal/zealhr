package com.te.zealhr.exception;

public class PermissionDeniedException extends RuntimeException {
	String message;
	
	public PermissionDeniedException(String message) {

		super(message);
	}

}
