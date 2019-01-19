package org.spring.zuul.server.security.token;

import java.security.interfaces.RSAPublicKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.zuul.server.security.TokenService;
import org.spring.zuul.server.security.encrypt.Jwt;
import org.spring.zuul.server.security.encrypt.RsaVerifier;
import org.spring.zuul.server.security.encrypt.SignatureVerifier;
import org.spring.zuul.server.utils.JwtHelper;
import org.spring.zuul.server.utils.RsaKeyTools;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Service
public class JwtTokenService implements TokenService {

	private SignatureVerifier verifier;

	// Test1
	//private String pKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkp77WAHq6Cjhg4lCFGQJwjVHhxCGJYa85kujXIshmbTY3ZQOQUfvWtOl5R1+YDl0XMH6oHRxlQjV8x6QUk+f/nQVLNIkjWVEK8qr2fVux+7SbXwuWVRRhfbyVima4fP9a7yNM4R9V+xswj8UVm4V96sXasx4kEF1Lxi14VPSkXfPvvyXAvbCdV7VdKNjnkOOZCNnmCcRqXbCzNDKUpTZdKvPyIe7G1NsjBh5RdUiHyk0Xo9pK7+mBXLiG7W/UBVyMfmwpjwxWn07x8gjkFIGT5lYIFNugAlEcICXfRSGIRLvWw30mT6G58V4B3SyukBQzvI2JRdqzHe7QmS3m+onBwIDAQAB";
	// Test2
	private String pKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA1HEUFEg2bYOM1sBD9mrx3pl40sllZTbxcDnoaOKw7GNJ1x7QETL/6Qa4Ko4OEyfEma7k+6QnC7khxO7qWQQ8pSoFu8Ag2lkR4uvmAIF7T/FjRIRgKdEHRX0KQX3Ms+4n+ddJWCvZeV17PUTp3g3k0Vp5KTQcLXByVr5B/rGTdgGaNfBpfxy67NtgPIlWogMdscumPV1rImE/0h3Tzyh31Q/k4Mw0AJdAE+J6SOQaHJAPJ8vPAI2iD9eBit8K46Y6qEiHIPr2Jrk/ZNbwYd8UHuqC2tyf54pySjWUJaI1CsOAknn9H2ovxeOHSy3szTSKEcbAuY41xerZXiR499sQ0QolpcueodbNUcoyIX2EVXy6PMoWQZ/r325W2KCLkidx9xjTjMPlxu3fznb0iBgs45ofG8qXALEIHQtmO/C7EeKp3xnF9Y+CskFAj6f5jL8Hk9Zu/BfOE0ZVIGoOzPzUi3sFtPcr1xgq+KJDYW5tepUuGYfw5GEIXukm+pF+HYKNuVhR+nzHXbVzEzel/aoB1b+TqH7DsQLlb3cyZf+rkkDhsgcxX6daG6Bgs7cOsQk2KnIRf1JViYn15B5Kxfn8VRb9w6tfIvJCOb9ywLkLqekrlHaJh6E9sNANILQy+MdIIfI0dIaxeJjbcDSJpDr4jxxGD9tmNUKevta75Q7vlzECAwEAAQ==";
	
	private Logger logger = LoggerFactory.getLogger(JwtTokenService.class);
	
	/**
	 * 使用公钥初始化Token验证对象
	 * @throws Exception
	 */
	public JwtTokenService() throws Exception{
		try {
			RSAPublicKey publicKey = (RSAPublicKey)RsaKeyTools.convertPublicKey(pKey);
			verifier = new RsaVerifier(publicKey);
		} catch (Exception e) {
			throw new Exception("Public key is not null or format is error, please check!");
		}
	}
	
	/**
	 * 公钥验证签名并解析JWT格式Token
	 */
	@Override
	public JSONObject readAccessToken(String token) {
		try {
			Jwt jwt = JwtHelper.decodeAndVerify(token, verifier);
			System.out.println("==========>"+jwt.getClaims());
			String content = jwt.getClaims();
			logger.debug("JWT verifier success!");
			return JSON.parseObject(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 验证Token是否过期
	 */
	@Override
	public boolean verifier(long time) {
		if(System.currentTimeMillis() > time * 1000){
			logger.debug("Verifier token is expired!");
			return false;
		}
		return true;
	}
}