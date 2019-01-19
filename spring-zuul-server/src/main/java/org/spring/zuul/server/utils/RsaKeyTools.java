package org.spring.zuul.server.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bouncycastle.asn1.ASN1Sequence;
import static org.spring.zuul.server.utils.Codecs.utf8Encode;
import static org.spring.zuul.server.utils.Codecs.b64Decode;


public abstract class RsaKeyTools {

	private static final String DEFAULT_ALG = "RSA";
	private static String BEGIN = "-----BEGIN";
	private static Pattern PEM_DATA = Pattern.compile("-----BEGIN (.*)-----(.*)-----END (.*)-----", Pattern.DOTALL);

	public static KeyPair parseKeyPair(String pemData) {
		Matcher m = PEM_DATA.matcher(pemData.trim());

		if (!m.matches()) {
			throw new IllegalArgumentException("String is not PEM encoded data");
		}

		String type = m.group(1);
		final byte[] content = b64Decode(utf8Encode(m.group(2)));

		PublicKey publicKey;
		PrivateKey privateKey = null;

		try {
			KeyFactory fact = KeyFactory.getInstance("RSA");
			if (type.equals("RSA PRIVATE KEY")) {
				ASN1Sequence seq = ASN1Sequence.getInstance(content);
				if (seq.size() != 9) {
					throw new IllegalArgumentException("Invalid RSA Private Key ASN1 sequence.");
				}
				org.bouncycastle.asn1.pkcs.RSAPrivateKey key = org.bouncycastle.asn1.pkcs.RSAPrivateKey
						.getInstance(seq);
				RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
				RSAPrivateCrtKeySpec privSpec = new RSAPrivateCrtKeySpec(key.getModulus(), key.getPublicExponent(),
						key.getPrivateExponent(), key.getPrime1(), key.getPrime2(), key.getExponent1(),
						key.getExponent2(), key.getCoefficient());
				publicKey = fact.generatePublic(pubSpec);
				privateKey = fact.generatePrivate(privSpec);
			} else if (type.equals("PUBLIC KEY")) {
				KeySpec keySpec = new X509EncodedKeySpec(content);
				publicKey = fact.generatePublic(keySpec);
			} else if (type.equals("RSA PUBLIC KEY")) {
				ASN1Sequence seq = ASN1Sequence.getInstance(content);
				org.bouncycastle.asn1.pkcs.RSAPublicKey key = org.bouncycastle.asn1.pkcs.RSAPublicKey.getInstance(seq);
				RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
				publicKey = fact.generatePublic(pubSpec);
			} else {
				throw new IllegalArgumentException(type + " is not a supported format");
			}

			return new KeyPair(publicKey, privateKey);
		} catch (InvalidKeySpecException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}

	private static final Pattern SSH_PUB_KEY = Pattern.compile("ssh-(rsa|dsa) ([A-Za-z0-9/+]+=*) (.*)");

	public static RSAPublicKey parsePublicKey(String key) {
		Matcher m = SSH_PUB_KEY.matcher(key);

		if (m.matches()) {
			String alg = m.group(1);
			String encKey = m.group(2);
			// String id = m.group(3);

			if (!"rsa".equalsIgnoreCase(alg)) {
				throw new IllegalArgumentException("Only RSA is currently supported, but algorithm was " + alg);
			}

			return parseSSHPublicKey(encKey);
		} else if (!key.startsWith(BEGIN)) {
			// Assume it's the plain Base64 encoded ssh key without the
			// "ssh-rsa" at the start
			return parseSSHPublicKey(key);
		}

		KeyPair kp = parseKeyPair(key);

		if (kp.getPublic() == null) {
			throw new IllegalArgumentException("Key data does not contain a public key");
		}

		return (RSAPublicKey) kp.getPublic();
	}

	private static RSAPublicKey parseSSHPublicKey(String encKey) {
		final byte[] PREFIX = new byte[] { 0, 0, 0, 7, 's', 's', 'h', '-', 'r', 's', 'a' };
		ByteArrayInputStream in = new ByteArrayInputStream(b64Decode(utf8Encode(encKey)));

		byte[] prefix = new byte[11];

		try {
			if (in.read(prefix) != 11 || !Arrays.equals(PREFIX, prefix)) {
				throw new IllegalArgumentException("SSH key prefix not found");
			}

			BigInteger e = new BigInteger(readBigInteger(in));
			BigInteger n = new BigInteger(readBigInteger(in));

			return createPublicKey(n, e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static RSAPublicKey createPublicKey(BigInteger n, BigInteger e) {
		try {
			return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(n, e));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private static byte[] readBigInteger(ByteArrayInputStream in) throws IOException {
		byte[] b = new byte[4];

		if (in.read(b) != 4) {
			throw new IOException("Expected length data as 4 bytes");
		}

		int l = (b[0] << 24) | (b[1] << 16) | (b[2] << 8) | b[3];

		b = new byte[l];

		if (in.read(b) != l) {
			throw new IOException("Expected " + l + " key bytes");
		}

		return b;
	}
	
	public static PublicKey convertPublicKey(String keyStr) throws Exception {
		byte[] keyBytes = Base64.decode(keyStr.getBytes());
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(DEFAULT_ALG);
		return keyFactory.generatePublic(keySpec);
	}

	public static PrivateKey convertPrivateKey(String keyStr) throws Exception {
		byte[] keyBytes = Base64.decode(keyStr.getBytes());
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(DEFAULT_ALG);
		return keyFactory.generatePrivate(pkcs8KeySpec);
	}
}
