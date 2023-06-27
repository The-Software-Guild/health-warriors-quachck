package com.wileyedge.healthyrecipe.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	
	// USER
	@ExceptionHandler(UserNotFoundException.class)
	public final ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest req){
		String msg = ex.getMsg(); 	    
	    String message = "User not found." + msg;
	    ExceptionResponse exResponse = new ExceptionResponse(new Date(), message, "Exception Occur.");
	    return new ResponseEntity<>(exResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DuplicateEmailException.class)
    public final ResponseEntity<Object> handleDuplicateEmailException(DuplicateEmailException ex, WebRequest req) {
        ExceptionResponse exResponse = new ExceptionResponse(new Date(), ex.getMessage(), "Duplicate Email.");
        return new ResponseEntity<>(exResponse, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(UsernameAlreadyExistsException.class)
    public final ResponseEntity<Object> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex, WebRequest req) {
        ExceptionResponse exResponse = new ExceptionResponse(new Date(), ex.getMessage(), "Username already exists. Please choose a different one.");
        return new ResponseEntity<>(exResponse, HttpStatus.BAD_REQUEST);
    }
	
	
	@ExceptionHandler(InvalidCredentialException.class)
    public final ResponseEntity<Object> handleInvalidCredentialException(InvalidCredentialException ex, WebRequest req) {
        ExceptionResponse exResponse = new ExceptionResponse(new Date(), ex.getMessage(), "Username already exists. Please choose a different one.");
        return new ResponseEntity<>(exResponse, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(InvalidTokenException.class)
    public final ResponseEntity<Object> handleInvalidTokenException(InvalidTokenException ex, WebRequest req) {
        ExceptionResponse exResponse = new ExceptionResponse(new Date(), ex.getMessage(), "Invalid Token.");
        return new ResponseEntity<>(exResponse, HttpStatus.UNAUTHORIZED);
    }
	
	@ExceptionHandler(UnauthorizedAccessException.class)
    public final ResponseEntity<Object> handleUnauthorizedAccessException(UnauthorizedAccessException ex, WebRequest req) {
        ExceptionResponse exResponse = new ExceptionResponse(new Date(), ex.getMessage(), "You are not authorized to perform this action.");
        return new ResponseEntity<>(exResponse, HttpStatus.UNAUTHORIZED);
    }

	// RECIPE
	@ExceptionHandler(RecipeNotFoundException.class)
	public final ResponseEntity<Object> handleRecipeNotFoundExceptionn(RecipeNotFoundException ex, WebRequest req){
		String searchCriteria = ex.getSearchCriteria(); 	    
	    String message = "RECIPE NOT FOUND. " + searchCriteria;
	    ExceptionResponse exResponse = new ExceptionResponse(new Date(), message, "Exception Occur.");
	    return new ResponseEntity<>(exResponse, HttpStatus.NOT_FOUND);
	}
	
	
	
	
	// GENERAL

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ExceptionResponse exResponse = new ExceptionResponse(new Date(), "Validation Failed", ex.getBindingResult().toString());
		System.out.println(exResponse);
		return new ResponseEntity(exResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest req){
		ExceptionResponse exResponse = new ExceptionResponse(new Date(), ex.getMessage(), "Exception Occur.");
		return new ResponseEntity(exResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
}
