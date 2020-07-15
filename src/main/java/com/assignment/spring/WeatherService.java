package com.assignment.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.spring.api.WeatherResponse;

@Service
public class WeatherService {

	@Autowired
	private WeatherRepository weatherRepository;

	public WeatherEntity storeWeatherResponse(WeatherResponse response) {
		WeatherEntity entity = new WeatherEntity();
		entity.setCity(response.getName());
		entity.setCountry(response.getSys().getCountry());
		entity.setTemperature(response.getMain().getTemp());

		return weatherRepository.save(entity);
	}

}
