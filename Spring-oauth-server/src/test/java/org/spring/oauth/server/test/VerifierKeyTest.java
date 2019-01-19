package org.spring.oauth.server.test;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class VerifierKeyTest {

	private SignatureVerifier verifier;

	private String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkp77WAHq6Cjhg4lCFGQJwjVHhxCGJYa85kujXIshmbTY3ZQOQUfvWtOl5R1+YDl0XMH6oHRxlQjV8x6QUk+f/nQVLNIkjWVEK8qr2fVux+7SbXwuWVRRhfbyVima4fP9a7yNM4R9V+xswj8UVm4V96sXasx4kEF1Lxi14VPSkXfPvvyXAvbCdV7VdKNjnkOOZCNnmCcRqXbCzNDKUpTZdKvPyIe7G1NsjBh5RdUiHyk0Xo9pK7+mBXLiG7W/UBVyMfmwpjwxWn07x8gjkFIGT5lYIFNugAlEcICXfRSGIRLvWw30mT6G58V4B3SyukBQzvI2JRdqzHe7QmS3m+onBwIDAQAB";

	private String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NDMzNzA2NzksInVzZXJfbmFtZSI6Im95eWwiLCJqdGkiOiIzYzY0ZmY0MS1mNmE0LTRlMzktYTcyYS1lODFkNDZmZGEwNjgiLCJjbGllbnRfaWQiOiJxcSIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdfQ.b-DW_j30RxJbfFUuhPjCF0aglV0ZOJSF-aaf96e6MJu1xUwqt6u1x5JPtOZT5hX6AcXwZHac_BOFizWmfhM1ms16Z2JDV8cQD-6fFbndJmE6OJEWA4P765cL9rgsVTCtv-jaoBAsLNS2yc9qWE-ocDpBYHP_66qTbgb_hE6CUQDHJeCXVtLlLCA1xYbgv_c_O7v4rg3L86H11-uIWsM8VU2PbXvcl-f-pdq7BAy1NF1mWmQGSpg3Kj96YJ0xHYI1DY4NNL1xYiKVtKvmqyaNmzSH7Q9M4MYPl-_zjIjsD0e0ZsbzAEn_hizZduyMW_vADt4tixJBol2SdGKnldizNw";

	@Test
	public void verifier() {
		try {
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKey.getBytes()));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
			// if publicKey is string, then backend code check publickey is use ssh-keygen generator standard format
			// {@link RsaVerifier#RsaVerifier(String key)}
			verifier = new RsaVerifier(publicKey);
			Jwt jwt = JwtHelper.decodeAndVerify(token, verifier);
			String content = jwt.getClaims();
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
