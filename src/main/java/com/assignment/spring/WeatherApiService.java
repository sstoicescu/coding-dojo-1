package com.assignment.spring;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.assignment.spring.api.WeatherResponse;
import com.assignment.spring.exceptions.WeatherApiAuthorizationException;
import com.assignment.spring.exceptions.WeatherApiRateLimitException;
import com.assignment.spring.exceptions.WeatherApiRequestException;

@Service
public class WeatherApiService {

	private static final Logger log = LoggerFactory.getLogger(WeatherApiService.class);

	@Value("${com.assignment.spring.app.id}")
	private String appId;

	@Value("${com.assignment.spring.app.url}")
	private String weatherApiUrl;

	@Autowired
	private RestTemplate restTemplate;

	private <T> T call(java.util.function.Supplier<ResponseEntity<T>> s) {
		try {
			ResponseEntity<T> response = s.get();
			return response.getBody();
		}
		// this part right here, this is common to the communication structure itself
		// per https://openweathermap.org/faq
		catch (HttpClientErrorException e) {
			switch (e.getStatusCode()) {
			case UNAUTHORIZED:
				throw new WeatherApiAuthorizationException(e);
			case NOT_FOUND:
				throw new WeatherApiRequestException(e);
			case TOO_MANY_REQUESTS:
				throw new WeatherApiRateLimitException(e);
			default:
				throw e;
			}
		} catch (RestClientException e) {
			log.error("Uncaught exception while calling OpenWeather API: " + e.getMessage());
			throw e;
		}
	}

	public WeatherResponse getWeatherForCity(String city) {
		log.debug("Looking up " + city);
		return call(() -> {
			// the /weather API is coupled to the weather response type
			return restTemplate.getForEntity(this.weatherApiUrl, WeatherResponse.class,
					Map.of("appid", this.appId, "city", city));
		});
	}

}
