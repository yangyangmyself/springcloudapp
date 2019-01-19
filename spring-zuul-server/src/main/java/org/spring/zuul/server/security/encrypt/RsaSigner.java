package org.spring.zuul.server.security.encrypt;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAPrivateKeySpec;

import org.spring.zuul.server.utils.RsaKeyTools;

public class RsaSigner implements Signer {

	static final String DEFAULT_ALGORITHM = "SHA256withRSA";

	private final RSAPrivateKey key;
	private final String algorithm;

	public RsaSigner(BigInteger n, BigInteger d) {
		this(createPrivateKey(n,d));
	}

	public RsaSigner(RSAPrivateKey key) {
		this(key, DEFAULT_ALGORITHM);
	}

	public RsaSigner(RSAPrivateKey key, String algorithm) {
		this.key = key;
		this.algorithm = algorithm;
	}

	public RsaSigner(String sshKey) {
		this(loadPrivateKey(sshKey));
	}

	public byte[] sign(byte[] bytes) {
		try {
			Signature signature = Signature.getInstance(algorithm);
			signature.initSign(key);
			signature.update(bytes);
			return signature.sign();
		}
		catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

	public String algorithm() {
		return algorithm;
	}

	private static RSAPrivateKey createPrivateKey(BigInteger n, BigInteger d) {
		try {
			return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateKeySpec(n, d));
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static RSAPrivateKey loadPrivateKey(String key) {
		KeyPair kp = RsaKeyTools.parseKeyPair(key);

		if (kp.getPrivate() == null) {
			throw new IllegalArgumentException("Not a private key");
		}

		return (RSAPrivateKey) kp.getPrivate();
	}

}
