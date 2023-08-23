package com.te.zealhr.exception;

public class ReimbursementNotAppliedException extends RuntimeException {

	String message;
	
	public ReimbursementNotAppliedException(String message) {
		
		super(message);
	}
}
