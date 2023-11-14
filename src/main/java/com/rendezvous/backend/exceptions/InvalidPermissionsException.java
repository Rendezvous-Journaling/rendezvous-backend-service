package com.rendezvous.backend.exceptions;

public class InvalidPermissionsException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public InvalidPermissionsException() {
		super("You do not have the proper permissions to access this resource.");
	}

}
