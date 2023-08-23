package com.te.zealhr.exception;

@SuppressWarnings("serial")
public class FailedToUploadException extends RuntimeException {

	public FailedToUploadException(String message) {
		super(message);
	}
}
