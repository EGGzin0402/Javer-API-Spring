package com.eggzin.cliente_service.web.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.eggzin.cliente_service.exception.EntityNotFoundException;
import com.eggzin.cliente_service.exception.PasswordInvalidException;
import com.eggzin.cliente_service.exception.UsernameUniqueViolationException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex,
																		HttpServletRequest request,
																		BindingResult result){
		log.error("CLIENTE-SERVICE Error - ", ex);
		return ResponseEntity
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Campos Inválidos: ",result));
	}
	
	@ExceptionHandler(UsernameUniqueViolationException.class)
	public ResponseEntity<ErrorMessage> dataIntegrityViolationException(RuntimeException ex,
																		HttpServletRequest request){
		log.error("CLIENTE-SERVICE Error - ", ex);
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.CONFLICT, ex.getMessage()));
	}
	
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorMessage> entityNotFoundException(RuntimeException ex,
																		HttpServletRequest request){
		log.error("CLIENTE-SERVICE Error - ", ex);
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.NOT_FOUND, ex.getMessage()));
	}
	@ExceptionHandler(PasswordInvalidException.class)
	public ResponseEntity<ErrorMessage> passwordInvalidException(RuntimeException ex,
																HttpServletRequest request){
		log.error("CLIENTE-SERVICE Error - ", ex);
		return ResponseEntity
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage()));
	}

}
