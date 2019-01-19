package org.spring.oauth.server.config;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier("userDetailsService")
	private UserDetailsService userDetailsService;

	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;

	@Autowired
	@Qualifier("bcryptPasswordEncoder")
	private PasswordEncoder passwordEncoder;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		// 自定义实现
		auth.userDetailsService(userDetailsService)
		                .passwordEncoder(passwordEncoder);
		// 内存实现
		// auth.inMemoryAuthentication().withUser("admin").password("123456").roles("USER");

		// Jdbc基于Spring security 标准实现(表结构保持一致)
		/*auth.jdbcAuthentication()
				.dataSource(dataSource)
				.usersByUsernameQuery(
						"select name, password, 'true' as enabled from jm_user where name=?")
				.authoritiesByUsernameQuery("select username,role from jm_user_roles where username=?")
				.passwordEncoder(passwordEncoder);*/
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {		
		http.authorizeRequests()
				.antMatchers("/**", "/oauth/authorize", "/oauth/token", "/oauth/check_token", "/oauth/token_key").permitAll()
				.anyRequest().authenticated()
				.and().formLogin()
				.and().logout()
				.and().csrf().disable();
	}
}
