package com.te.zealhr.exception.helpandsupport;

public class EmployeeNotActiveException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmployeeNotActiveException(String message) {
		super(message);
	}

}
