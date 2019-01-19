package org.spring.zuul.server.security;

import com.alibaba.fastjson.JSONObject;

public interface TokenService {

	/**
	 * 签名验证并解析Token内容
	 * @param token: Oauther server 统一发放的Token
	 * @return 使用公钥验证通过后,返回用户信息;验证失败返回null值
	 */
	JSONObject readAccessToken(String token);
	
	/**
	 * 验证Token有效期
	 * @param time 秒
	 * @return true=未过期, false=过期
	 */
	boolean verifier(long time);
	
}
