package org.spring.oauth.server.config;

import org.spring.oauth.server.service.SystemUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSourceConfiguration {

	@Bean("dataSource")
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
		driverManagerDataSource.setUrl("jdbc:mysql://192.168.2.178:3306/vehicle_db?useUnicode=true&characterEncoding=utf8");
		driverManagerDataSource.setUsername("root");
		driverManagerDataSource.setPassword("123456");
		return driverManagerDataSource;
	}

	@Bean("bcryptPasswordEncoder")
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean("userDetailsService")
	public UserDetailsService userDetailsService() {
		return new SystemUserDetailsService();
	}

}
