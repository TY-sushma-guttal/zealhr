package com.te.zealhr.exception.admindept;

/**
 * @author Tapas
 *
 */

@SuppressWarnings("serial")
public class PurchaseOrderNotFoundException extends RuntimeException {
	public PurchaseOrderNotFoundException(String msg) {
		super(msg);
	}
}
