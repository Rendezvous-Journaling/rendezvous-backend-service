package com.rendezvous.backend.exceptions;

public class InvalidTokenException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public InvalidTokenException() {
		super("Invalid token has been provided");
	}

}
