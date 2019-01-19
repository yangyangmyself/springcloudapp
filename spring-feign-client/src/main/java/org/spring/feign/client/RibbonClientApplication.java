package org.spring.feign.client;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
/**
 * 客户端负载均衡
 * @author oyyl
 * @since 2018/09/26
 */
@SpringBootApplication
@EnableFeignClients
public class RibbonClientApplication {
	
	@Bean
	@LoadBalanced
	public RestTemplate rest(RestTemplateBuilder builder) {
		return builder.build();
	}
	
    public static void main(String[] args ) {
    	
        SpringApplication.run(RibbonClientApplication.class, args);
        
    }
}