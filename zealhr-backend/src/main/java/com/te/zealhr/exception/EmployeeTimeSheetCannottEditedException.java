package com.te.zealhr.exception;

public class EmployeeTimeSheetCannottEditedException extends RuntimeException{

	 String message;
	
	public EmployeeTimeSheetCannottEditedException(String message) {
		
		super(message);
	}
}
