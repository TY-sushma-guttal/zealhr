package com.te.zealhr.exception.employee;

@SuppressWarnings("serial")
public class EmployeeNotRegisteredException extends RuntimeException{
	public EmployeeNotRegisteredException(String msg) {
		super(msg);
	}
}
