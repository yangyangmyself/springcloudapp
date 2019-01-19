package org.spring.oauth.server.dao;

import java.util.List;
import java.util.Map;
/**
 * 持久层父接口
 * @author oy
 *
 */
public interface IRepos {

	/**
	 * 依据主键值获取对象
	 * @param bkxh
	 * @return
	 */
	Map getData(String primaryKey);
	
	/**
	 * 插入对象
	 * @param susp
	 * @return
	 */
	int saveData(Map dataMap);
	
	/**
	 * 更新信息
	 * @param bkxh
	 * @param data
	 * @return
	 */
	int updateData(String primaryKey, Map dataMap);
	
	
	/**
	 * 获取数据列表
	 * @param paramMap
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	List queryDataList(Map paramMap, int pageIndex, int pageSize);

}
