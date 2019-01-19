package org.spring.oauth.server.dao;

import java.util.Map;

public interface UserDao {
	
	public Map findByUserName(String yhdh);
	
}