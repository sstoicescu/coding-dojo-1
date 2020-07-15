package com.assignment.spring;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;



import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.assignment.spring.api.Main;
import com.assignment.spring.api.Sys;
import com.assignment.spring.api.WeatherResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceTests {

	private static final double temperature = 23.52;

	private static final String countryCode = "FR";

	private static final String city = "Paris";

	@Autowired
	private WeatherService weatherService;
	
	@MockBean
	private WeatherRepository weatherRepository;
	
	@Test
	public void adapterWorks() {
		WeatherResponse s = new WeatherResponse();
		s.setName(city);
		s.setSys(new Sys());
		s.getSys().setCountry(countryCode);
		s.setMain(new Main());
		s.getMain().setTemp(temperature);

		WeatherEntity w = new WeatherEntity();
		w.setCity(city);
		w.setCountry(countryCode);
		w.setTemperature(23.52);
		
		when(weatherRepository.save(refEq(w))).thenReturn(w);

		assertThat(weatherService.storeWeatherResponse(s)).isEqualTo(w);
		
		verify(weatherRepository).save(refEq(w));
	}
}
