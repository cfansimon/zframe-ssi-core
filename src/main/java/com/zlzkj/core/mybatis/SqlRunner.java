package com.zlzkj.core.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlzkj.core.base.SOMap;
import com.zlzkj.core.sql.Row;
import com.zlzkj.core.sql.SQLBuilder;

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
	 * 自定义单个属性查询
	 * @param field
	 * @param value
	 * @param className
	 * @return
	 */
	public List<Row> selectByProperty(String field,String value,Class<?> className){
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(className);
		Map<String, Object> whereMap =new HashMap<String, Object>();
		whereMap.put(field, value);
		String sql = sqlBuilder.fields("*").where(whereMap).selectSql();
		return sqlMapper.select(createSOM(sql));
	}
	/**
	 * 自定义单个属性查询是否存在
	 * @param field
	 * @param value
	 * @param id
	 * @param className
	 * @return
	 */
	public Integer count(String field,String value,Integer id,Class<?> className){
		if (field==null||value==null||field==""||value=="") {
			return 0;
		}
		SQLBuilder sqlBuilder = SQLBuilder.getSQLBuilder(className);
		Map<String, Object> whereMap =new HashMap<String, Object>();
		if(field!=null&&value!=null)
			whereMap.put(field, value);
		if(id!=null&&id>0)
			whereMap.put("id", new String[]{"<>",id.toString()});
		String sql = sqlBuilder.fields("count(*)").where(whereMap).selectSql();
		return sqlMapper.count(createSOM(sql));
	}
	
	/**
	 * insert方法
	 * @param somap
	 * @return
	 */
	public Integer insert(String sql,Object... params){
		return sqlMapper.insert(createSOM(sql,params));
	}
	
	/**
	 * update方法
	 * @param somap
	 * @return
	 */
	public Integer update(String sql,Object... params){
		return sqlMapper.update(createSOM(sql,params));
	}
	
	/**
	 * delete方法
	 * @param somap
	 * @return
	 */
	public Integer delete(String sql,Object... params){
		return sqlMapper.delete(createSOM(sql,params));
	}

	/**
	 * 组装参数
	 * @param sql
	 * @param params
	 * @return
	 */
	private SOMap createSOM(String sql,Object... params){
		SOMap somap = new SOMap();
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
