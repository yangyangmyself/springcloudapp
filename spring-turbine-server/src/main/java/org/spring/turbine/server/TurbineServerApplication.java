package org.spring.turbine.server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
/**
 * 断路器集群监控服务端
 * @author oyyl
 * @since 2018/09/26
 */
@SpringBootApplication
@EnableTurbine
@EnableHystrixDashboard
public class TurbineServerApplication {
	
    public static void main( String[] args ) {
    	
        SpringApplication.run(TurbineServerApplication.class, args);
        
    }
}
