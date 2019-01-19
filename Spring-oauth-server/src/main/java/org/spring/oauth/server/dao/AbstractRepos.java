package org.spring.oauth.server.dao;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunshine.common.repository.DataRepositoryJDBC;

/**
 * 父接抽象实现
 * @author oy
 * 
 */
public abstract class AbstractRepos extends DataRepositoryJDBC
		implements
			IRepos {
	
	protected Logger log = LoggerFactory.getLogger(getClass());

	public AbstractRepos(String tableName, String primaryKey) {
		// 表名
		super.masterTable = tableName;
		// 表主键字段名
		super.masterTablePK = primaryKey;
	}

	@Override
	public Map getData(String primaryVal) {
		if (this.masterTablePK == null)
			log.error("未设置表主键字段名！", new Exception("表主键字段属性masterTablePK未初始化！"));
		String sql = "select * from " + this.masterTable + " where "
				+ this.masterTablePK + " = ?";
		return this.findOneBySQL(sql, new Object[]{primaryVal});
	}

	@Override
	public int saveData(Map dataMap) {
		return Integer.parseInt(String.valueOf(super.doInsert(dataMap)));
	}

	@Override
	public int updateData(String primaryKey, Map dataMap) {
		return super.doUpdate(primaryKey, dataMap);
	}

	@Override
	public List queryDataList(Map paramMap, int pageIndex, int pageSize) {

		throw new UnsupportedOperationException("不支持此方法,子类需重写！");
	}
}