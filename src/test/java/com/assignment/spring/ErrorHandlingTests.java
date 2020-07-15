package com.assignment.spring;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import com.assignment.spring.exceptions.WeatherApiAuthorizationException;
import com.assignment.spring.exceptions.WeatherApiRateLimitException;
import com.assignment.spring.exceptions.WeatherApiRequestException;

@RunWith(SpringRunner.class)
@WebMvcTest(WeatherController.class)
public class ErrorHandlingTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WeatherService weatherService;

	@MockBean
	private WeatherApiService weatherApiService;

	@Test
	public void nullCityShouldFailValidation() throws Exception {

		mockMvc.perform(get("/weather")).andExpect(status().isBadRequest());

		verifyZeroInteractions(weatherService, weatherApiService);
	}

	@Test
	public void emptyCityShouldFailValidation() throws Exception {

		mockMvc.perform(get("/weather?city=")).andExpect(status().isBadRequest());

		verifyZeroInteractions(weatherService, weatherApiService);
	}

	@Test
	public void blankCityShouldFailValidation() throws Exception {

		mockMvc.perform(get("/weather?city=   ")).andExpect(status().isBadRequest());

		verifyZeroInteractions(weatherService, weatherApiService);
	}

	@Test
	public void unknownCityShouldReturnAnExplanation() throws Exception {
		final String city = "NobodyWouldNameTheirCityThis";
		when(weatherApiService.getWeatherForCity(city))
				.thenThrow(new WeatherApiRequestException(new HttpClientErrorException(HttpStatus.NOT_FOUND)));

		mockMvc.perform(get("/weather?city=" + city)).andExpect(status().isNotFound());
		verifyZeroInteractions(weatherService);
	}

	@Test
	public void rateLimitsShouldReturnAnExplanation() throws Exception {
		when(weatherApiService.getWeatherForCity(any(String.class))).thenThrow(
				new WeatherApiRateLimitException(new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS)));

		mockMvc.perform(get("/weather?city=Amsterdam")).andExpect(status().isTooManyRequests());
		verifyZeroInteractions(weatherService);
	}

	@Test
	public void misconfiguredShouldReturnAnExplanation() throws Exception {
		when(weatherApiService.getWeatherForCity(any(String.class)))
				.thenThrow(new WeatherApiAuthorizationException(new HttpClientErrorException(HttpStatus.UNAUTHORIZED)));

		mockMvc.perform(get("/weather?city=Amsterdam")).andExpect(status().isUnauthorized());
		verifyZeroInteractions(weatherService);
	}

}
