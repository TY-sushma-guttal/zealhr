package com.te.zealhr.exception;

public class DataAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	String message;

	public DataAlreadyExistsException(String message) {
		super(message);

	}
}
