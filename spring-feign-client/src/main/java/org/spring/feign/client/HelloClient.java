package org.spring.feign.client;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="rb1")
public interface HelloClient {

	@RequestMapping(method = RequestMethod.GET, value = "/msgs")
	public List<?> getMsgs();

	@RequestMapping(method = RequestMethod.GET, value = "/msgs/{id}", consumes = "application/json")
	public String getMsg(@PathVariable("id") Long id);

}