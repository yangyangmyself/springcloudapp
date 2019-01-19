package org.spring.oauth.server.config;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.oauth.server.service.JwtAccessTokenEnhance;
import org.spring.oauth.server.tools.RsaKeyTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableAuthorizationServer
public class OAuthJwtTokenConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;
    
    @Autowired
    private Environment env;
    
    @Autowired
    private AuthorizationCodeServices codeServices;
    
    private Logger log = LoggerFactory.getLogger(OAuthJwtTokenConfig.class);
    
    /**
     * defines the authorization and token endpoints and the token services
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancer())
                .authenticationManager(authenticationManager)
                .authorizationCodeServices(codeServices)
                .setClientDetailsService(clientDetailsService());
    }
    
    @Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    	security.checkTokenAccess("permitAll()");
	}

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
    public TokenStore tokenStore() {
		@Deprecated
		//TokenStore tokenStore = new RedisTokenStore(jedisConnectionFactory());
		
		TokenStore tokenStore = new JwtTokenStore(tokenEnhancer());
    	return tokenStore;
    }
	
	/**
	 * Use {@link DefaultAccessTokenConverter} by default.
	 * You can extends {@link DefaultAccessTokenConverter} class and invoke 
	 * {@link JwtAccessTokenConverter#setAccessTokenConverter(AccessTokenConverter)}
	 * @return
	 */
	@Bean
	public JwtAccessTokenConverter tokenEnhancer(){
		//JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenEnhance();
		try {
			/**
			InputStream input = getClass().getClassLoader().getResourceAsStream("sso.jks");
			KeyStore keyStore = KeyStore.getInstance("JKS");
			keyStore.load(input, "123456".toCharArray());
			KeyPair keyPair = getKeyPair(keyStore, "server", "123456".toCharArray());
			*/
			
			// Read the keypair of openssl tools create
			InputStream input = getClass().getClassLoader().getResourceAsStream("kong-private.pem");
			int len = input.available();
			byte[] buffer = new byte[len];
			input.read(buffer, 0, len);
			input.close();
			KeyPair keyPair = RsaKeyTools.parseKeyPair(new String(buffer));
			
			tokenConverter.setKeyPair(keyPair);
			System.out.println("-----BEGIN PUBLIC KEY-----\n" + new String(Base64.encode(keyPair.getPublic().getEncoded())) 
					+ "\n-----END PUBLIC KEY-----");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tokenConverter;
	}
    
	private static KeyPair getKeyPair(KeyStore keystore, String alias, char[] password) {  
		Key key = null;;  
		PublicKey publicKey = null;
		try {
			key = keystore.getKey(alias,password); 
			if(key instanceof PrivateKey) {  
			   Certificate cert = keystore.getCertificate(alias);  
			   publicKey = cert.getPublicKey();
			}
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}  
		return new KeyPair(publicKey,(PrivateKey)key);
	} 
	
    @Bean
    public ClientDetailsService clientDetailsService(){
    	return new JdbcClientDetailsService(dataSource);
    }
    
    /**
     * a configurer that defines the client details service. Client details can be initialized
     * Implements have two method:
     * the first is {@link ClientDetailsServiceConfigurer} withClientDetails method and pass by 
     * ClientDetailsService subclass
     * the second is {@link ClientDetailsServiceConfigurer} init method 
     * pass by ClientDetailsServiceBuilder
     * 
     * Notice: must create table according to init.sql(project/sql/init.sql) file and database type
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    	clients.withClientDetails(clientDetailsService());			
    }
}