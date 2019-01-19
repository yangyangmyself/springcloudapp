package org.spring.zuul.server.security.access;

public class AccessDeniedException extends Exception {

	private static final long serialVersionUID = 1L;

	private String emsg;
	
	public AccessDeniedException(String str){
		super(str);
		this.emsg = str;
	}
	
}
