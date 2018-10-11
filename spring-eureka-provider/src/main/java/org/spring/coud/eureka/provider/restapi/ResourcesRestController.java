package org.spring.coud.eureka.provider.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author oyyl
 */
@RestController
@RequestMapping("/api/rest/provider/test")
public class ResourcesRestController {
	
	@RequestMapping("/response")
	public String readMsg(){
		
		return "Request restfull resources success!";
	}
	
}