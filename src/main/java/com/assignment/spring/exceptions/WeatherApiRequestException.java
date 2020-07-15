package com.assignment.spring.exceptions;

import org.springframework.web.client.HttpClientErrorException;

public class WeatherApiRequestException extends WeatherApiStatusException {

	public WeatherApiRequestException(HttpClientErrorException ex) {
		super("Input query not found", ex);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4215562266244398947L;

}
