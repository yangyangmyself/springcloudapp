package org.spring.zuul.server.security;

import java.util.Collection;

public interface AccessDecisionVoter<S> {

	int ACCESS_GRANTED = 1;
	int ACCESS_ABSTAIN = 0;
	int ACCESS_DENIED = -1;

	boolean supports(ConfigAttribute attribute);

	int vote(TokenService tokenService, S object, Collection<ConfigAttribute> attributes);
}
