package org.spring.zuul.server.security;

import java.util.Collection;

import org.spring.zuul.server.security.access.AccessDeniedException;

/**
 * 访问决策服务
 * @since 2018/11/29
 * @author oyyl
 *
 */
public interface AccessDecisionService {

	void decide(TokenService tokenService, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException;

	boolean supports(ConfigAttribute attribute);

}