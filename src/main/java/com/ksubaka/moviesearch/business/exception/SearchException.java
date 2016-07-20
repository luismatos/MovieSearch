package com.ksubaka.moviesearch.business.exception;

/**
 * Search Exception.
 * 
 * @author Luis Matos
 */
public class SearchException extends Exception {
	private static final long serialVersionUID = -8077745702849064604L;

	public SearchException() {

	}

	public SearchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SearchException(String message, Throwable cause) {
		super(message, cause);
	}

	public SearchException(String message) {
		super(message);
	}

	public SearchException(Throwable cause) {
		super(cause);
	}
}
