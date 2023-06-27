package com.wileyedge.healthyrecipe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RecipeNotFoundException extends RuntimeException {
	 private String searchCriteria;

	    public RecipeNotFoundException(String searchCriteria) {
	        super("User not found " + searchCriteria);
	        this.searchCriteria = searchCriteria;
	    }

	    public String getSearchCriteria() {
	        return searchCriteria;
	    }
}
