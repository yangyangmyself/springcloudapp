package org.spring.zuul.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class ZuulConfiguration {
	
	@Autowired
	private Environment env;
	
	@Bean
    public JedisConnectionFactory jedisConnectionFactory(){
    	// Redis connect pool configuration
    	JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    	jedisPoolConfig.setMinIdle(env.getProperty("spring.redis.pool.min-idle",Integer.class));
    	jedisPoolConfig.setMaxIdle(env.getProperty("spring.redis.pool.max-idle",Integer.class));
    	jedisPoolConfig.setMaxTotal(env.getProperty("spring.redis.pool.max-total",Integer.class));
    	JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
    	jedisConnectionFactory.setDatabase(env.getProperty("spring.redis.database",Integer.class));
    	jedisConnectionFactory.setHostName(env.getProperty("spring.redis.hostname",String.class));
    	jedisConnectionFactory.setPassword(env.getProperty("spring.redis.password").trim());
    	jedisConnectionFactory.setTimeout(env.getProperty("spring.redis.timeout",Integer.class));
    	jedisConnectionFactory.setPort(env.getProperty("spring.redis.port",Integer.class));
        jedisConnectionFactory.setUsePool(true); 
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        return jedisConnectionFactory;
    }
	
	@Bean
	public RestTemplate rest(RestTemplateBuilder builder) {
		
		return builder.build();
	}
}
