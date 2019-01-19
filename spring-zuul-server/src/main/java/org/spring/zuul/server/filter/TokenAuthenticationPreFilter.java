package org.spring.zuul.server.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.zuul.server.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 前缀过滤器
 * 用于安全验证,访问控制
 * @author oyyl
 */
@Component
public class TokenAuthenticationPreFilter extends ZuulFilter {

	private static final String HEADER_KEY = "Authorization";
	
	private static final String TOKEN_ACCESS_KEY = "access_token";
	
	private Logger logger = LoggerFactory.getLogger(TokenAuthenticationPreFilter.class);
	
	@Autowired
	@Qualifier("jwtTokenService")
	private TokenService tokenService;
	
	/**
	 * 值为true是才能执行下述run方法
	 */
	@Override
	public boolean shouldFilter() {
		return true;
	}

	/**
	 * 检查是否带Token,检查顺序:
	 * 1)请求头{@link HttpServletRequest.request.getHeader}
	 * 2)表单及URL
	 * @param request
	 * @return
	 */
	private String getToken(HttpServletRequest request){
		if(request.getHeader(HEADER_KEY) != null){
			String _authorization = request.getHeader(HEADER_KEY);
			if(_authorization.startsWith("Bearer") && _authorization.length() > 7){
				String _token = _authorization.substring(7, _authorization.length());
				if(!StringUtils.isEmpty(_token))
					return _token;
			}  else {
				logger.error("Token format is error![Bearer + 空格 + token]");
			}
		}
		String result = request.getParameter(TOKEN_ACCESS_KEY);
		if(!StringUtils.isEmpty(result)) {
			return result;
		} else {
			logger.error("Please provider token!");
		}
		return null;
	}
	
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String outtoken = getToken(request);
		if(outtoken == null || outtoken.length() == 0){
			throw new ZuulRuntimeException(new ZuulException("Token is null",-1,"Authorization validate failure! Token is null"));
		} else {
			JSONObject obj = tokenService.readAccessToken(outtoken);
			if(obj == null){
				throw new ZuulRuntimeException(new ZuulException("Token verifier failure",-1,"Token verifier failure"));
			}
			boolean verifer = tokenService.verifier(obj.getLongValue("exp"));
			if(!verifer){
				throw new ZuulRuntimeException(new ZuulException("Token has expired",-1,"Token has expired"));
			} else {
				/**
				 * Resolve token and store key-value to HttpServletRequest
				 * BackEnd can get user information from HttpServletRequest instance
				 */
				request.setAttribute("user_name", obj.getString("user_name"));
				request.setAttribute("client_id", obj.getString("client_id"));
				request.setAttribute("scope", obj.getString("scope"));
			}
		}	
		return null;
	}
	
	/**
	 * 过滤类型
	 * pre/post/error
	 */
	@Override
	public String filterType() {
		return "pre";
	}

	/**
	 * 过滤顺序
	 */
	@Override
	public int filterOrder() {
		return -1;
	}
	
	/**
	 * 默认是静态过滤(=true)
	 * 值为false,才能执行下述run方法
	 */
	@Override
	public boolean isStaticFilter() {
		return false;
	}

	/**
	 * 启用过滤
	 */
	@Override
	public boolean isFilterDisabled() {
		return false;
	}
}