package com.te.zealhr.exception.admin;

@SuppressWarnings("serial")
public class HolidayNotFoundException extends RuntimeException {

	public HolidayNotFoundException(String msg) {
		super(msg);
	}
}
