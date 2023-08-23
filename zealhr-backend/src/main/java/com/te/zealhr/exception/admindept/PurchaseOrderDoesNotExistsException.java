package com.te.zealhr.exception.admindept;

/**
 * 
 * @author Brunda
 *
 */
public class PurchaseOrderDoesNotExistsException  extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public PurchaseOrderDoesNotExistsException(String message) {
		super(message);
	}
}
