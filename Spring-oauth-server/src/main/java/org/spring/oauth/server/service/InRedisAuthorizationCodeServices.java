package org.spring.oauth.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.stereotype.Service;
/**
 * code默认存储JVM内存且无过期策略,不能跨节点访问,换取token后才进行删除操作
 * 复写默认实现过期清除、多节点共享等
 * @author oyyl
 *
 */
@Service
public class InRedisAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

	// table
	private static final String ACCESS = "code:";
	
	// expire time by default(10 minutes)
	private static final long EXPIRE_SECOND = 600; 

	@Autowired
	private RedisConnectionFactory connectionFactory;	
	
	
	private RedisConnection getConnection() {
		return connectionFactory.getConnection();
	}
	
	@Override
	protected void store(String code, OAuth2Authentication authentication) {
		byte[] accessCodeKey = String.valueOf(ACCESS+code).getBytes();
		RedisConnection conn = getConnection();
		try {
			conn.openPipeline();
			conn.set(accessCodeKey, SerializationUtils.serialize(authentication));
			conn.expire(accessCodeKey, EXPIRE_SECOND);
			conn.closePipeline();
		} finally {
			conn.close();
		}
	}

	@Override
	protected OAuth2Authentication remove(String code) {
		byte[] accessCodeKey = String.valueOf(ACCESS + code).getBytes();
		RedisConnection conn = getConnection();
		try {
			byte[] data = conn.get(accessCodeKey);
			OAuth2Authentication authentication = (OAuth2Authentication)SerializationUtils.deserialize(data);
			conn.del(accessCodeKey);
			return authentication;
		} finally {
			conn.close();
		}
	}

}
