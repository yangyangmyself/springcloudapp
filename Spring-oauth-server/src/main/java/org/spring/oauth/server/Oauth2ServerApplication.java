package org.spring.oauth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class Oauth2ServerApplication {
	
	public static void main(String[] args) {
		
		SpringApplication.run(Oauth2ServerApplication.class, args);
	}
}
