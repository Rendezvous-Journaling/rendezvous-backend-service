package com.rendezvous.backend.exceptions;

public class ResourceNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String resource) {
		super("Could not find " + resource);
	}
	
	
	
}
