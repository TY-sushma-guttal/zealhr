package com.te.zealhr.exception.employee;

@SuppressWarnings("serial")
public class OTPNotFoundExpireException extends RuntimeException {

	public OTPNotFoundExpireException(String msg) {
		super(msg);
	}

}
