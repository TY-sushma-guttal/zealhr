package com.te.zealhr.exception.admin;

@SuppressWarnings("serial")
public class ExpenseNotUpdatedException extends RuntimeException {

	public ExpenseNotUpdatedException (String message) {
		
		super(message);
	}
}
