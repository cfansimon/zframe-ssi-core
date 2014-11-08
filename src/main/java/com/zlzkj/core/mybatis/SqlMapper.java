package com.zlzkj.core.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;

import com.zlzkj.core.base.SoMap;
import com.zlzkj.core.sql.Row;

/**
 * select数据专用
 * @author Simon
 *
 */
public interface SqlMapper {
    
	/**
	 * 返回二维结果集
	 * @param som
	 * @return
	 */
	@SelectProvider(type = SqlMapperProvider.class,method = "select")
	public List<Row> select(SoMap somap);
	 
	/**
	 * 返回单行记录
	 * @param som
	 * @return
	 */
	@SelectProvider(type = SqlMapperProvider.class,method = "select")
	public Row find(SoMap somap);
	
	/**
	 * 返回统计数量
	 * @param som
	 * @return
	 */
	@SelectProvider(type = SqlMapperProvider.class,method = "select")
	public Integer count(SoMap somap);
	
	/**
	 * 返回特定字段的值
	 * @param som
	 * @return
	 */
	@SelectProvider(type = SqlMapperProvider.class,method = "select")
	public Object getValue(SoMap somap);
}