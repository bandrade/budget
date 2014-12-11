package com.dupont.budget.bpm.custom.exception;

/**
 * 
 * @since 2014
 *
 */
public class BPMException extends Exception {
	
	private static final long serialVersionUID = -2264259061755238265L;
	
	public BPMException() {}

	public BPMException(String message, Throwable cause) {
		super(message, cause);
	}

	public BPMException(String message) {
		super(message);
	}

	public BPMException(Throwable cause) {
		super(cause);
	}

}
