package com.te.zealhr.exception;

public class AdvanceSalaryNotAppliedException extends RuntimeException {

	String message ;
	
	public AdvanceSalaryNotAppliedException(String message) {
		
		super(message);
	}
}
