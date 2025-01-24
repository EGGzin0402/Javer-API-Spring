package com.eggzin.javer_service.exception;

public class UsernameUniqueViolationException extends RuntimeException {
	
	public UsernameUniqueViolationException(String message) {
		super(message);
	}

}
