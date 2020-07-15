package com.assignment.spring;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EndToEndTests {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testFullFlow() {
		WeatherEntity expected = new WeatherEntity();
		expected.setId(1);
		expected.setCity("Amsterdam");
		expected.setTemperature(16.2);
		expected.setCountry("NL");
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/weather?city=Amsterdam",
				WeatherEntity.class)).isEqualToComparingFieldByField(expected);
	}
}
