package org.spring.zuul.server.security.access;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import org.spring.zuul.server.security.AccessDecisionVoter;
import org.spring.zuul.server.security.ConfigAttribute;
import org.spring.zuul.server.security.TokenService;
import com.alibaba.fastjson.JSONObject;

public class AuthenticatedVoter implements AccessDecisionVoter<HttpServletRequest> {

	public static final String TOKEN_ID = "token_id";
	public static final String IS_AUTHENTICATED_FULLY = "IS_AUTHENTICATED_FULLY";
	public static final String IS_AUTHENTICATED_REMEMBERED = "IS_AUTHENTICATED_REMEMBERED";
	public static final String IS_AUTHENTICATED_ANONYMOUSLY = "IS_AUTHENTICATED_ANONYMOUSLY";
	
	// TODO
	public AuthenticatedVoter(){
		// Noting to do
	}
	
	private boolean isFullyAuthenticated(TokenService tokenService, HttpServletRequest request) {
		// 获取Token
		String tokenVal = request.getParameter(TOKEN_ID);
		JSONObject obj = tokenService.readAccessToken(tokenVal);
		if(tokenVal == null || obj == null)
			return false;
		// 过期验证
		if(!tokenService.verifier(obj.getLong("exp"))){
			return false;
		}
		return true;
	}
	
	@Override
	public boolean supports(ConfigAttribute attribute) {
		if ((attribute.getAttribute() != null)
				&& (IS_AUTHENTICATED_FULLY.equals(attribute.getAttribute())
						 || IS_AUTHENTICATED_ANONYMOUSLY.equals(attribute.getAttribute()))) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int vote(TokenService tokenService, HttpServletRequest request, Collection<ConfigAttribute> attributes) {
		for(ConfigAttribute attribute : attributes){
			if(supports(attribute)){
				
			}
		}	
		return 0;
	}
	
}
