package com.poseidon.app.exceptions;

@SuppressWarnings("serial")
public class UserServiceException extends Exception {

	public UserServiceException(String error) {
		super(error);
	}

}
