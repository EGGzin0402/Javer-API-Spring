package com.eggzin.cliente_service.exception;

public class UsernameUniqueViolationException extends RuntimeException {
	
	public UsernameUniqueViolationException(String message) {
		super(message);
	}

}
