package org.spring.cloud.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
/**
 * Register Center(Register Table)
 * 注册中心
 * @author oyyl
 *
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
	
	public static void main(String[] args) {
		
		SpringApplication.run(EurekaServerApplication.class, args);
        
    }
}
