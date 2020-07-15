package com.assignment.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Function;
import com.google.common.util.concurrent.RateLimiter;

@Component
public class RateLimitedRestTemplate {

	private RestTemplate restTemplate;

	private RateLimiter rateLimiter;

	@Autowired
	public RateLimitedRestTemplate(RestTemplate restTemplate,
			@Value("${com.assignment.spring.app.rate}") String requestsPerSecond) {
		this.restTemplate = restTemplate;
		this.rateLimiter = RateLimiter.create(Integer.parseInt(requestsPerSecond));
	}

	public <T> T execute(Function<RestTemplate, T> f) {
		this.rateLimiter.acquire();
		return f.apply(this.restTemplate);
	}

}
