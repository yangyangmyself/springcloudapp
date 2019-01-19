package org.spring.zuul.server.security;

import javax.servlet.http.HttpServletRequest;

public interface RequestMatcher {
	
	boolean matches(HttpServletRequest request);

}
