package org.spring.oauth.server.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;
@Repository
public class UserDaoRepos extends AbstractRepos implements UserDao{

	public UserDaoRepos() {
		this("t_ac_sysuser", "yhdh");
	}
	
	public UserDaoRepos(String tableName, String primaryKey) {
		super(tableName, primaryKey);
	}

	@Override
	public Map findByUserName(String yhdh) {
		String sql = "select * from " + this.getMasterTable() + " where yhdh=?";
		return findOneBySQL(sql, new Object[]{yhdh});
	}

}
