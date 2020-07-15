package com.assignment.spring;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.assignment.spring.exceptions.WeatherApiStatusException;

@RestController
@Validated
public class WeatherController {

	
	private static final Logger log = LoggerFactory.getLogger(WeatherController.class);


    @Autowired
    private WeatherService weatherService;
    
    @Autowired
    private WeatherApiService weatherApiService;
    
    @RequestMapping(value = "/weather",
    		produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public WeatherEntity weather(@Valid @NotBlank(message = "City may not be blank") String city) {
    	log.debug("Processing weather request for " + city);
        return weatherService.storeWeatherResponse(weatherApiService.getWeatherForCity(city));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintError(ConstraintViolationException e) {
    	log.error(e.getMessage()); // TODO: link the error to the request, right?
    	return new ResponseEntity<>("invalid input: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(WeatherApiStatusException.class)
    public ResponseEntity<String> handleKnownApiError(WeatherApiStatusException e) {
    	log.error(e.getWrapped().getMessage());
    	return new ResponseEntity<>(e.getMessage(), e.getWrappedStatusCode());
    }
    
    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleUnknownApiError(RestClientException e) {
    	log.error(e.getMessage()); // TODO: link the error to the request, right?
    	return new ResponseEntity<>("Error while trying to contact OpenWeather API Service: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleDaoError(DataAccessException e) {
    	log.error("DB layer error" + e.getMessage());
    	return new ResponseEntity<>("Error while trying to store weather data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
