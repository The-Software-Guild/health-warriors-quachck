package com.wileyedge.healthyrecipe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

	private String msg;

	    public UserNotFoundException(String msg) {
	        super("User not found " + msg);
	        this.msg = msg;
	    }

	    public String getMsg() {
	        return msg;
	    }
}
