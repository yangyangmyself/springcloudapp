package org.spring.zuul.server.security.access;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.spring.zuul.server.security.AccessDecisionVoter;
import org.spring.zuul.server.security.ConfigAttribute;
import org.spring.zuul.server.security.TokenService;

public class WebUrlVoter implements AccessDecisionVoter<HttpServletRequest> {

	@Override
	public boolean supports(ConfigAttribute attribute) {
		
		return false;
	}

	@Override
	public int vote(TokenService tokenService, HttpServletRequest object, Collection<ConfigAttribute> attributes) {
		
		return 0;
	}

}
