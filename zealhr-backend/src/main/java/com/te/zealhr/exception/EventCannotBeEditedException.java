package com.te.zealhr.exception;

public class EventCannotBeEditedException extends RuntimeException {

	String message;
	
	public EventCannotBeEditedException(String message) {
		super(message);
	}
}
