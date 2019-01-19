package org.spring.zuul.server.security.encrypt;

public interface Jwt extends BinaryFormat {
	
	String getClaims();

	String getEncoded();

	void verifySignature(SignatureVerifier verifier);
}
