package com.te.zealhr.exception.admindept;

/**
 * @author Brunda
 *
 * 
 */
public class NoIssuesFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NoIssuesFoundException(String message) {
		super(message);
	}
}
