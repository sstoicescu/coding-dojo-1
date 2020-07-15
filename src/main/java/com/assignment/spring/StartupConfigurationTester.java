package com.assignment.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.assignment.spring.exceptions.WeatherApiAuthorizationException;

@Component
public class StartupConfigurationTester {
	
	private static final Logger log = LoggerFactory.getLogger(StartupConfigurationTester.class);
	
	@Autowired
	private WeatherApiService weatherApiService;
	
	@EventListener
	public void checkWeatherApiIsAvailable(ApplicationStartedEvent event) {
		log.info("Testing OpenWeather API configuration");
		try {
			weatherApiService.getWeatherForCity("Amsterdam");
		} catch (WeatherApiAuthorizationException e) {
			log.error("Failed to properly configure weather api authorization. Application cannot function.");
			throw e;
		}
	}

}
