package com.te.zealhr.exception.admin;
/**
 * @author Tapas
 *
 */
public class DuplicateStockGroupNameException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public DuplicateStockGroupNameException(String msg) {
		super(msg);
	}
}
