package org.spring.cloud.ribbon.restapi;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TestService {

	@Autowired
	private RestTemplate restTemplate;

	public String read() {
		URI uri = URI.create("http://ABC-SERVICE/api/rest/provider/test/response");
		return this.restTemplate.getForObject(uri, String.class);
	}
}
