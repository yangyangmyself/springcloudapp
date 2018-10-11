package org.spring.eureka.consumer.restapi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rest/consumer/test")
public class ConsumerRestController {
	
	@Autowired
	private TestService testService;
	
	@RequestMapping("/response")
	public String read(){
		
		return testService.read();
	}

}
