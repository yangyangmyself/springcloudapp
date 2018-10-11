package org.spring.hystrix.client;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
/**
 * 断路器
 * @author oyyl
 * @since 2018/09/26
 */
@SpringBootApplication
@EnableCircuitBreaker
@EnableDiscoveryClient
@EnableHystrixDashboard
public class HystrixClientApplication {
	
	/**
	 * 1)@Bean用于实例化对象RestTemplate,在服务层及控制层使用
	 * 2)@LoadBalanced用于服务名映射及负载均衡
	 * @param builder
	 * @return
	 */
	@Bean
	@LoadBalanced
	public RestTemplate rest(RestTemplateBuilder builder) {
		return builder.build();
	}
	
    public static void main( String[] args ) {
    	
        SpringApplication.run(HystrixClientApplication.class, args);
        
    }
}
