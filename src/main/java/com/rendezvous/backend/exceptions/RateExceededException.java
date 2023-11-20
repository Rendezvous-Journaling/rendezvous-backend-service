package com.rendezvous.backend.exceptions;

public class RateExceededException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public RateExceededException() {
		super("Rate Limit Exceeded");
	}
}
