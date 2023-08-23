package com.te.zealhr.exception.admindept;

/**
 * @author Tapas
 *
 */

@SuppressWarnings("serial")
public class SalesOrderNotFoundException extends RuntimeException {

		public SalesOrderNotFoundException(String msg) {
			super(msg);
		}
}
