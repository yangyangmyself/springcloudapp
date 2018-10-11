package org.spring.zuul.server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 网关
 * @author oyyl
 * @since 2018/09/26
 */
@SpringBootApplication
@EnableZuulProxy
@EnableHystrixDashboard
public class ZuulServerApplication {
	
    public static void main(String[] args) {
    	
        SpringApplication.run(ZuulServerApplication.class, args);
        
    }
}