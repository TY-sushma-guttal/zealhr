package com.te.zealhr.exception;

public class EventDetailsNotFoundException extends RuntimeException {

	String message;
	
	public EventDetailsNotFoundException(String message) {
		super(message);
	}
}
