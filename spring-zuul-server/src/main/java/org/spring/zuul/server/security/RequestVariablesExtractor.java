package org.spring.zuul.server.security;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface RequestVariablesExtractor {

	Map<String, String> extractUriTemplateVariables(HttpServletRequest request);
}
