package org.spring.zuul.server.security.token;

import org.spring.zuul.server.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.JSONObject;

//@Service
public class RedisTokenService implements TokenService {
	
	private static final StringRedisSerializer str_serializer = new StringRedisSerializer();
	
	private static final JdkSerializationRedisSerializer object_serializer = new JdkSerializationRedisSerializer();
	
	@Autowired
	private RedisConnectionFactory connectionFactory;	
	
	private RedisConnection getConnection() {
		return connectionFactory.getConnection();
	}
	
	public JSONObject readAccessToken(String tokenValue) {
		byte[] key = str_serializer.serialize("access:" + tokenValue);
		byte[] bytes = null;
		RedisConnection conn = getConnection();
		try {
			bytes = conn.get(key);
		} finally {
			conn.close();
		}
		if(bytes == null || bytes.length==0)
			return null;
		return (JSONObject)object_serializer.deserialize(bytes);
	}

	@Override
	public boolean verifier(long time) {
		
		return false;
	}
}
