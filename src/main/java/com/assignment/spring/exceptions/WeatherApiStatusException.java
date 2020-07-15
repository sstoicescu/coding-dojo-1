package com.assignment.spring.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class WeatherApiStatusException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -745702271274168747L;

	private String message;
	
	private HttpClientErrorException wrapped;

	
	
	public WeatherApiStatusException(String message, HttpClientErrorException wrapped) {
		super();
		this.message = message;
		this.wrapped = wrapped;
	}

	public String getMessage() {
		return message;
	}
	
	public HttpClientErrorException getWrapped() {
		return wrapped;
	}

	public HttpStatus getWrappedStatusCode() {
		return wrapped.getStatusCode();
	}
	
}
