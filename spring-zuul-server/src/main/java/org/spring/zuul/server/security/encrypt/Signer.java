package org.spring.zuul.server.security.encrypt;

public interface Signer extends AlgorithmMetadata {
	
	byte[] sign(byte[] bytes);
	
}
