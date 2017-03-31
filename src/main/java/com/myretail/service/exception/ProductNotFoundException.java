package com.myretail.service.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends Exception {
	
	 private HttpStatus status;
	 private String message;

	public ProductNotFoundException() {
        super();
    }

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ProductNotFoundException(HttpStatus status,String message){
        this.status=status;
        this.message=message;
    }

    public ProductNotFoundException(Throwable cause) {
        super(cause);
    }
}
