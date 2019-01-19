package org.spring.zuul.server.security;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

public abstract class AbstractAccessDecisionService implements AccessDecisionService {

	protected final Log logger = LogFactory.getLog(getClass());

	private List<AccessDecisionVoter<? extends Object>> decisionVoters;

	protected AbstractAccessDecisionService(List<AccessDecisionVoter<? extends Object>> decisionVoters) {
		Assert.notEmpty(decisionVoters, "A list of AccessDecisionVoters is required");
		this.decisionVoters = decisionVoters;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notEmpty(this.decisionVoters, "A list of AccessDecisionVoters is required");
	}
	
	public List<AccessDecisionVoter<? extends Object>> getDecisionVoters() {
		return this.decisionVoters;
	}

	public boolean supports(ConfigAttribute attribute) {
		for (AccessDecisionVoter voter : this.decisionVoters) {
			if (voter.supports(attribute)) {
				return true;
			}
		}
		return false;
	}
}
