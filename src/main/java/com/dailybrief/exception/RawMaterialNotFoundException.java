package com.dailybrief.exception;

public class RawMaterialNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RawMaterialNotFoundException(String message) {
        super(message);
    }
}
