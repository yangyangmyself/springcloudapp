package org.spring.cloud.ribbon;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.PingUrl;

@Configuration
public class RbConfiguration {
	
	@Bean
    public IPing ribbonPing() {
        return new PingUrl();
    }
	
	/*@Bean
    public IPing ribbonPing(IClientConfig config) {
        return new PingUrl();
    }
	
	@Bean
	public IClientConfig ribbonClientConfig() {
		DefaultClientConfigImpl config = new DefaultClientConfigImpl();
		config.loadProperties("rb1");
		return config;
	}*/
}
