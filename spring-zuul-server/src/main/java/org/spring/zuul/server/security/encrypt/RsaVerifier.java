package org.spring.zuul.server.security.encrypt;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;

import org.spring.zuul.server.utils.RsaKeyTools;

public class RsaVerifier implements SignatureVerifier {

	private final RSAPublicKey key;
	
	private final String algorithm;

	public RsaVerifier(BigInteger n, BigInteger e) {
		this(RsaKeyTools.createPublicKey(n, e));
	}

	public RsaVerifier(RSAPublicKey key) {
		this(key, RsaSigner.DEFAULT_ALGORITHM);
	}

	public RsaVerifier(RSAPublicKey key, String algorithm) {
		this.key = key;
		this.algorithm = algorithm;
	}

	public RsaVerifier(String key) {
		this(RsaKeyTools.parsePublicKey(key.trim()), RsaSigner.DEFAULT_ALGORITHM);
	}

	public void verify(byte[] content, byte[] sig) {
		try {
			Signature signature = Signature.getInstance(algorithm);
			signature.initVerify(key);
			signature.update(content);

			if (!signature.verify(sig)) {
				throw new RuntimeException("RSA Signature did not match content");
			}
		}
		catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

	public String algorithm() {
		return algorithm;
	}
}
