package org.spring.coud.eureka.provider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
/**
 * Service provider
 * 服务提供
 * @author oyyl
 *
 */
@EnableEurekaClient
@SpringBootApplication
public class EurekaProviderApplication {
	
	public static void main(String[] args) {
		
		SpringApplication.run(EurekaProviderApplication.class, args);
        
    }
}
