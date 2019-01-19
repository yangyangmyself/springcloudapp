package org.spring.zuul.server.security.encrypt;

public interface SignatureVerifier extends AlgorithmMetadata {
	
	void verify(byte[] content, byte[] signature);
	
}
