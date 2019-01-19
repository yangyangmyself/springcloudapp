package org.spring.oauth.server.tools;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTools {

	private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(
			10);

	public static String bcryptEncode(String rawpassword) {
		return passwordEncoder.encode(rawpassword);
	}

	public static boolean matche(String rawpassword, String encodePassword) {

		return passwordEncoder.matches(rawpassword, encodePassword);
	}

	public static void main(String[] args) {
		//String result = "$2a$10$O1/SEdj8hHaf5W3TtToYtOCfEfGECEi0zn9nYPBGdxgNeJt5mEwhS";
		//System.out.println(result);
		System.out.println(PasswordEncoderTools.bcryptEncode("123456"));
		//System.out.println(PasswordEncoderTools.matche("123456", result));
	}

}
