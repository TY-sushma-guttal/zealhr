package com.te.zealhr.exception.admin;
/**
 * @author Tapas
 *
 */
public class StockUnitNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public StockUnitNotFoundException(String message) {
		super(message);
	}
}
