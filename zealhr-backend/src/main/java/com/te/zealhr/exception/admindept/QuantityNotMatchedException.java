package com.te.zealhr.exception.admindept;


/**
 * @author Tapas
 *
 */

@SuppressWarnings("serial")
public class QuantityNotMatchedException extends RuntimeException {

	public QuantityNotMatchedException(String msg) {
		super(msg);
	}
}
