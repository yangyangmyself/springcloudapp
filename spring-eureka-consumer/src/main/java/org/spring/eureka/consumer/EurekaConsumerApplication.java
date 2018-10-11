package org.spring.eureka.consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Service consumer
 * 服务消费
 * @author oyyl
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaConsumerApplication {
	
	/**
	 * 1)@Bean用于实例化对象RestTemplate,在服务层及控制层使用
	 * 2)@RestTemplate用于服务名映射及负载均衡
	 * @param builder
	 * @return
	 */
	@Bean
	@LoadBalanced
	public RestTemplate rest(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	public static void main(String[] args) {
		
		SpringApplication.run(EurekaConsumerApplication.class, args);
        
    }
}
