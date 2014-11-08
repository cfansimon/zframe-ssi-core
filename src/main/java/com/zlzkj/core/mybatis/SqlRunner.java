package com.zlzkj.core.mybatis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlzkj.core.base.SoMap;
import com.zlzkj.core.sql.Row;

@Service
public class SqlRunner {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	public List<Row> select(String sql,Object... params){
		return sqlMapper.select(createSOM(sql,params));
	}
	
	public Row find(String sql,Object... params){
		return sqlMapper.find(createSOM(sql,params));
	}
	
	public Integer count(String sql,Object... params){
		return sqlMapper.count(createSOM(sql,params));
	}
	
	public Object getValue(String sql,Object... params){
		return sqlMapper.getValue(createSOM(sql,params));
	}
	
	/**
	 * 组装参数
	 * @param sql
	 * @param params
	 * @return
	 */
	private SoMap createSOM(String sql,Object... params){
		SoMap somap = new SoMap();
		somap.put("sql", sql);
		for(Integer i=0;i<params.length;i++){
			somap.put(i.toString(), params[i]);
		}
		return somap;
	}
	
	
	//====================快捷获取 - start==================
	
	/**
	 * 获取int值
	 * @param key
	 * @return
	 */
	public Integer getInt(String sql,Object... params){
		return Integer.valueOf(getValue(sql,params).toString());
	}
	
	/**
	 * 获取long值
	 * @param key
	 * @return
	 */
	public Long getLong(String sql,Object... params){
		return Long.valueOf(getValue(sql,params).toString());
	}
	
	/**
	 * 获取short值
	 * @param key
	 * @return
	 */
	public Short getShort(String sql,Object... params){
		return Short.valueOf(getValue(sql,params).toString());
	}
	
	/**
	 * 获取byte值
	 * @param key
	 * @return
	 */
	public Byte getByte(String sql,Object... params){
		return Byte.valueOf(getValue(sql,params).toString());
	}
	
	/**
	 * 获取float值
	 * @param key
	 * @return
	 */
	public Float getFloat(String sql,Object... params){
		return Float.valueOf(getValue(sql,params).toString());
	}
	
	/**
	 * 获取double值
	 * @param key
	 * @return
	 */
	public Double getDouble(String sql,Object... params){
		return Double.valueOf(getValue(sql,params).toString());
	}
	
	/**
	 * 获取整数值
	 * @param key
	 * @return
	 */
	public String getString(String sql,Object... params){
		return getValue(sql,params).toString();
	}
	//====================快捷获取 - end==================
	
}
