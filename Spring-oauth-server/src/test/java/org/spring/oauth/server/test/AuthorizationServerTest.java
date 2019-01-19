package org.spring.oauth.server.test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
/*import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;*/
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 * Request Hearder格式如下:
 * Authorization: username:password
 * 用户名密码需要通过Request Header传递
 * "username:password" 需要通过Base64编码
 * @author oy
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes=AuthenticationServer.class)
public class AuthorizationServerTest {
	
	public static final String httpURL = "http://localhost:8896/";
	
	/**
	 * 通过implic获取token
	 * 1)response_type:code token
	 * 2)response_type:token
	 * 返回302,重定向跳转页
	 */
	@Test
	public void testGetToKenByImplic(){
		/*HttpClient client = HttpClients.createDefault();
		try {
			HttpPost post = new HttpPost(httpURL + "oauth/authorize");
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("client_id","qq"));
			params.add(new BasicNameValuePair("scope","read"));
			//优先执行获取token
			params.add(new BasicNameValuePair("response_type","token"));
			params.add(new BasicNameValuePair("redirect_uri","http://example.com"));
			post.setEntity(new UrlEncodedFormEntity(params));
			post.addHeader("Authorization", "Basic " + base64EncodeStr("oy" + ":" + "123456"));
			HttpResponse response = client.execute(post);
			int scode = response.getStatusLine().getStatusCode();
			System.out.println(scode + "--" + EntityUtils.toString(response.getEntity()));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	/**
	 * 获取授权码code
	 */
	@Test
	public void testGetCodeByAuthorizationCode(){
		/*HttpClient client = HttpClients.createDefault();
		try {
			HttpPost post = new HttpPost(httpURL + "oauth/authorize");
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("client_id","qq"));
			params.add(new BasicNameValuePair("scope","read"));
			params.add(new BasicNameValuePair("response_type","code"));
			//params.add(new BasicNameValuePair("grant_type","client_credentials")); // 可以不用提供
			params.add(new BasicNameValuePair("redirect_uri","http://example.com"));
			post.setEntity(new UrlEncodedFormEntity(params));
			post.addHeader("Authorization", "Basic " + base64EncodeStr("oy" + ":" + "123456"));
			HttpResponse response = client.execute(post);
			int scode = response.getStatusLine().getStatusCode();
			System.out.println(scode + "--" + EntityUtils.toString(response.getEntity()));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	/**
	 * 通过客户端凭证获取Token
	 * POST /token HTTP/1.1
     * Host: server.example.com
     * Authorization: Basic czZCaGRSa3F0MzpnWDFmQmF0M2JW
     * Content-Type: application/x-www-form-urlencoded
     * 
	 * grant_type=client_credentials
	 */
	@Test
	public void testGetTokenByClientCredentials(){
		/*HttpClient client = HttpClients.createDefault();
		try {
			HttpPost post = new HttpPost(httpURL + "oauth/token");
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("response_type","token"));
			params.add(new BasicNameValuePair("grant_type","client_credentials"));//不支持implicit
			post.setEntity(new UrlEncodedFormEntity(params));
			// 客户端ID及密码
			post.addHeader("Authorization", "Basic " + base64EncodeStr("acme:" + "secret")); // client detail
			HttpResponse response = client.execute(post);
			int scode = response.getStatusLine().getStatusCode();
			System.out.println(scode + "--" + EntityUtils.toString(response.getEntity()));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	/**
	 * 通过授权码(code)换取Token
	 * POST /token HTTP/1.1
 	 * Host: server.example.com
	 * Authorization: Basic czZCaGRSa3F0MzpnWDFmQmF0M2JW
	 * Content-Type: application/x-www-form-urlencoded
	 *
	 * grant_type=authorization_code&code=SplxlOBeZQQYbYS6WxSbIA
	 * &redirect_uri=https%3A%2F%2Fclient%2Eexample%2Ecom%2Fcb
	 */
	@Test
	public void testGetTokenByAuthorizationCode(){
		/*HttpClient client = HttpClients.createDefault();
		try {
			HttpPost post = new HttpPost(httpURL + "oauth/token");
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			//params.add(new BasicNameValuePair("response_type","token")); // 可选项
			params.add(new BasicNameValuePair("grant_type","authorization_code"));//不支持implicit
			// 上次请求生成的CODE
			params.add(new BasicNameValuePair("code","DSfjOr"));
			params.add(new BasicNameValuePair("redirect_uri","http://localhost:8896/token.html"));;
			post.setEntity(new UrlEncodedFormEntity(params));
			// 客户端ID及密码
			post.addHeader("Authorization", "Basic " + base64EncodeStr("qq:" + "123456")); // client detail
			HttpResponse response = client.execute(post);
			int scode = response.getStatusLine().getStatusCode();
			System.out.println(scode + "--" + EntityUtils.toString(response.getEntity()));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	/**
	 * 验证TOKEN
	 */
	@Test
	public void testCheckToken(){
		/*HttpClient client = HttpClients.createDefault();
		String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE0OTQwOTM5NjAsInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJjZmU3OTQxZS1lMTlkLTQ0NTQtOWMzMC00OTJkN2I5Y2EzMGYiLCJjbGllbnRfaWQiOiJhY21lIn0.LuOMTyqMbe70enemjLVnkUNiOwtkxjJDxUga4XJikHg";
		try {
			HttpPost post = new HttpPost(httpURL + "oauth/check_token");
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("token",token));
			post.addHeader("Authorization", "Bearer " + token);
			HttpResponse response = client.execute(post);
			int scode = response.getStatusLine().getStatusCode();
			System.out.println(scode + "--" + EntityUtils.toString(response.getEntity()));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
}