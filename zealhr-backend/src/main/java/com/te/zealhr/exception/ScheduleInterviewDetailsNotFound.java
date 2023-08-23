package com.te.zealhr.exception;

public class ScheduleInterviewDetailsNotFound extends RuntimeException {

	String message;
	
	public ScheduleInterviewDetailsNotFound(String message) {
		
		super(message);
	}
}
