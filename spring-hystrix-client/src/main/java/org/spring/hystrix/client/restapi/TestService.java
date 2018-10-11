package org.spring.hystrix.client.restapi;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class TestService {

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 基于注解增加熔断机制并服务降级处理
	 * 远程请求调用存在问题时,则使用本地方法reliable处理并返回
	 * @return
	 */
	@HystrixCommand(fallbackMethod = "reliable")
	public String read() {
		URI uri = URI.create("http://ABC-SERVICE/api/rest/provider/test/response");
		return this.restTemplate.getForObject(uri, String.class);
	}

	public String reliable() {
		return "Cloud Native Java (O'Reilly)";
	}
}
