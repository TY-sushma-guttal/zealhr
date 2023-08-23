package com.te.zealhr.exception.admin;
/**
 * @author Tapas
 *
 */
public class DuplicateCompanyStockCategoriesNameException extends RuntimeException {
	
		private static final long serialVersionUID = 1L;
		public DuplicateCompanyStockCategoriesNameException(String msg) {
			super(msg);
		}
}
