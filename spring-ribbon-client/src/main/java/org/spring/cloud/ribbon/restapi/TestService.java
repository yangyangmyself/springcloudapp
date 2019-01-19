package org.spring.cloud.ribbon.restapi;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TestService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
    private LoadBalancerClient loadBalancer;

	public String read() {
		//URI uri = URI.create("http://ABC-SERVICE/api/rest/provider/test/response");
		//return this.restTemplate.getForObject(uri, String.class);
		ServiceInstance instance = loadBalancer.choose("rb1");
	    URI storesUri = URI.create(String.format("http://%s:%s", instance.getHost(), instance.getPort()));
		return storesUri.toString();
	}
}
