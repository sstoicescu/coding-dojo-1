package com.assignment.spring.exceptions;

import org.springframework.web.client.HttpClientErrorException;

public class WeatherApiRateLimitException extends WeatherApiStatusException {

	public WeatherApiRateLimitException(HttpClientErrorException ex) {
		super("Too many requests in the past minute, please try again later", ex);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3322403823643750480L;

}
