package com.te.zealhr.exception.admin;
/**
 * @author Tapas
 *
 */
public class DuplicateShiftNameException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public DuplicateShiftNameException(String msg){
		super(msg);
	}
}
