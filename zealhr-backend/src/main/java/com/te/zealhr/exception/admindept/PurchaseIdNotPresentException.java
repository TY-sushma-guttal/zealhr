package com.te.zealhr.exception.admindept;

/**
 * 
 * 
 * @author Vinayak More *
 *
 *
 **/

@SuppressWarnings("serial")
public class PurchaseIdNotPresentException extends RuntimeException {
	
	public PurchaseIdNotPresentException (String message) {
		
		super(message);
	}

}
