package org.spring.cloud.ribbon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
/**
 * 客户端负载均衡
 * @author oyyl
 * @since 2018/09/26
 */
@SpringBootApplication
@RibbonClient(name = "rb1", configuration=RbConfiguration.class)
public class RibbonClientApplication {
	
    public static void main(String[] args ) {
    	
        SpringApplication.run(RibbonClientApplication.class, args);
        
    }
}