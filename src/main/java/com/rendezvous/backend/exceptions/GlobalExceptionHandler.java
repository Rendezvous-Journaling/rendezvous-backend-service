package com.rendezvous.backend.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<?> methodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request){
		
		StringBuilder errors = new StringBuilder("");
		for(FieldError fe : ex.getBindingResult().getFieldErrors()) {
			errors.append(fe.getField() + " : " + fe.getDefaultMessage());
		}
		
		ErrorDetails errorDetails = new ErrorDetails(new Date(), errors.toString(), request.getDescription(false));
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<?> resourceNotFound(ResourceNotFoundException ex, WebRequest request){
		
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
	}
	
	@ExceptionHandler(RateExceededException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ResponseEntity<?> rateExceeded(RateExceededException ex, WebRequest request){
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
	}

	@ExceptionHandler(InvalidPermissionsException.class)
	public ResponseEntity<?> invalidPermissions(InvalidPermissionsException ex, WebRequest request){
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
	}
	
	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<?> invalidToken(InvalidTokenException ex, WebRequest request){
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
	}
}
