package com.te.zealhr.exception.admin;



public class EmployeeListNotFoundException extends RuntimeException{
	

	private static final long serialVersionUID = 1L;

	public EmployeeListNotFoundException(String message) {
		super(message);
	}
}
