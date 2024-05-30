package com.park.api.web.exceptions;

import com.park.api.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorMessage> entityNotFoundException(RuntimeException e, 
			HttpServletRequest request){
		log.error("Api error ", e);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.NOT_FOUND, e.getMessage()));
	}

	@ExceptionHandler({UsernameUniqueViolationException.class, CpfUniqueViolationException.class, CodeUniqueViolationException.class})
	public ResponseEntity<ErrorMessage> usernameUniqueViolationException(RuntimeException e, 
			HttpServletRequest request){
		log.error("Api error ", e);
		return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.CONFLICT, e.getMessage()));
	}

	@ExceptionHandler(PasswordInvalidException.class)
	public ResponseEntity<ErrorMessage> passwordInvalidException(RuntimeException e, HttpServletRequest request) {
		log.error("Api Error ", e);
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, e.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException e, 
			HttpServletRequest request, BindingResult result){
		log.error("Api error", e);
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Invalid field", result));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorMessage> accessDeniedException(AccessDeniedException e, HttpServletRequest request){
		log.error("Api error", e);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON)
				.body(new ErrorMessage(request, HttpStatus.FORBIDDEN, "Access Denied! " +e.getMessage()));
	}
}
