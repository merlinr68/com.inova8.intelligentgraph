/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.exceptions;

/**
 * The Class NotSupportedException.
 */
public class NotSupportedException extends HandledException{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new not supported exception.
	 *
	 * @param code the code
	 */
	public NotSupportedException(String code) {
		super(code);
	}

	/**
	 * Instantiates a new not supported exception.
	 *
	 * @param code the code
	 * @param e the e
	 */
	public NotSupportedException(String code, Exception e) {
		super(code, e);
	}


	/**
	 * Instantiates a new not supported exception.
	 *
	 * @param code the code
	 * @param message the message
	 * @param e the e
	 */
	public NotSupportedException(String code,String message, Exception e) {
		super(code, message,e);
	}

	/**
	 * Instantiates a new not supported exception.
	 *
	 * @param code the code
	 * @param message the message
	 */
	public NotSupportedException(String code, String message) {
		super(code, message);
	}

}
