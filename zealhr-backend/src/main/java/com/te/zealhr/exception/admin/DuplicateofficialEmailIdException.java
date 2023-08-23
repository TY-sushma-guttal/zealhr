package com.te.zealhr.exception.admin;
/**
 * @author Tapas
 *
 */
public class DuplicateofficialEmailIdException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public DuplicateofficialEmailIdException(String msg) {
		super(msg);
	}
}
