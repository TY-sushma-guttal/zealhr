package com.te.zealhr.exception;

public class EventNotFoundException extends RuntimeException{

String message;
	
	public EventNotFoundException(String message) {
		super(message);
	}
}
