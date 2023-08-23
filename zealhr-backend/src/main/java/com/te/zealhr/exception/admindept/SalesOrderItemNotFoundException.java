package com.te.zealhr.exception.admindept;

/**
 * 
 * @author Brunda
 *
 */
public class SalesOrderItemNotFoundException  extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public SalesOrderItemNotFoundException(String message) {
		super(message);
	}
}
