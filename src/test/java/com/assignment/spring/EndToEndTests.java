package com.assignment.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.assignment.spring.api.Main;
import com.assignment.spring.api.Sys;
import com.assignment.spring.api.WeatherResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EndToEndTests {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@MockBean
	private RestTemplate restTemplate;

	@Test
	public void testFullFlow() {
		WeatherResponse s = new WeatherResponse();
		String city = "Amsterdam";
		String countryCode = "NL";
		Double temperature = 16.2;

		s.setName(city);
		s.setSys(new Sys());
		s.getSys().setCountry(countryCode);
		s.setMain(new Main());
		s.getMain().setTemp(temperature);

		when(restTemplate.getForEntity(any(String.class), any(Class.class), any(Map.class)))
				.thenReturn(new ResponseEntity<>(s, HttpStatus.OK));

		WeatherEntity expected = new WeatherEntity();
		expected.setId(1);
		expected.setCity(city);
		expected.setTemperature(temperature);
		expected.setCountry(countryCode);
		assertThat(this.testRestTemplate.getForObject("http://localhost:" + port + "/weather?city=Amsterdam",
				WeatherEntity.class)).isEqualToComparingFieldByField(expected);
	}
}
