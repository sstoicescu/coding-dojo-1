package com.assignment.spring.exceptions;

import org.springframework.web.client.HttpClientErrorException;

public class WeatherApiAuthorizationException extends WeatherApiStatusException {

	public WeatherApiAuthorizationException(HttpClientErrorException ex) {
		super("Incorrectly configured application credentials", ex);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4678091127336480683L;

}
