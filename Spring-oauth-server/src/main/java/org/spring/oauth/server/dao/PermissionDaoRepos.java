package org.spring.oauth.server.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
@Repository
public class PermissionDaoRepos extends AbstractRepos implements PermissionDao {

	public PermissionDaoRepos() {
		this(null, null);
	}
	
	public PermissionDaoRepos(String tableName, String primaryKey) {
		super(tableName, primaryKey);
	}

	@Override
	public List findPermissionById(String yhdh) {
		String sql = "select sr.yhdh, r.role_name from t_sys_sysuser_role sr, t_sys_role r "
				   + "where sr.role_id=r.role_id and sr.yhdh = ?";
		return this.queryDataBySQL(sql, new Object[]{yhdh});
	}

}
