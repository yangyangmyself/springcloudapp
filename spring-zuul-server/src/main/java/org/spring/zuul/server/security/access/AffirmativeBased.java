package org.spring.zuul.server.security.access;

import java.util.Collection;
import java.util.List;

import org.spring.zuul.server.security.AbstractAccessDecisionService;
import org.spring.zuul.server.security.AccessDecisionVoter;
import org.spring.zuul.server.security.ConfigAttribute;
import org.spring.zuul.server.security.TokenService;

public class AffirmativeBased extends AbstractAccessDecisionService {

	protected AffirmativeBased(List<AccessDecisionVoter<? extends Object>> decisionVoters) {
		super(decisionVoters);
	}

	@Override
	public void decide(TokenService tokenService, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException {
		
		int deny = 0;

		for (AccessDecisionVoter voter : getDecisionVoters()) {
			int result = voter.vote(tokenService, object, configAttributes);

			if (logger.isDebugEnabled()) {
				logger.debug("Voter: " + voter + ", returned: " + result);
			}

			switch (result) {
			case AccessDecisionVoter.ACCESS_GRANTED:
				return;

			case AccessDecisionVoter.ACCESS_DENIED:
				deny++;

				break;

			default:
				break;
			}
		}

		if (deny > 0) {
			throw new AccessDeniedException("Access is denied");
		}

	}

}
