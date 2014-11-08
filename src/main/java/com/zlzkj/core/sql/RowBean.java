package com.zlzkj.core.sql;

import java.util.Iterator;
import java.util.Set;

import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;

import com.zlzkj.core.util.Fn;

/**
 * 根据数据库结果集动态创建bean对象
 * @author Simon
 *
 */
public class RowBean {
	/**
	 * 实体Object
	 */
	public Object object = null;

	/**
	 * 属性map
	 */
	public BeanMap beanMap = null;

	public RowBean() {
		super();
	}

	public RowBean(Row row) {
		this.object = generateBean(row);
		this.beanMap = BeanMap.create(this.object);
		//初始化值
		setInitValue(row);
	}
	
	/**
	 * 自动设值
	 * @param row
	 */
	public void setInitValue(Row row){
		Set<String> keySet = row.keySet();
		for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
			String key = (String) i.next();
			setValue(Fn.camelName(key),row.get(key));
		}
	}
	
	/**
	 * 给bean属性赋值
	 * 
	 * @param property
	 * @param value
	 */
	public void setValue(String property, Object value) {
		beanMap.put(property, value);
	}

	/**
	 * 通过属性名得到属性值
	 * 
	 * @param property
	 * @return 值
	 */
	public Object getValue(String property) {
		return beanMap.get(property);
	}

	/**
	 * 得到该实体bean对象
	 * @return
	 */
	public Object getObject() {
		return this.object;
	}

	private Object generateBean(Row row) {
		BeanGenerator generator = new BeanGenerator();
		Set<String> keySet = row.keySet();
		for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
			String key = (String) i.next();
			try {
				generator.addProperty(Fn.camelName(key), Class.forName("java.lang.Object"));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return generator.create();
	}
	
	/**
	 * 静态工厂
	 * @param row
	 * @return
	 */
	public static Object getBean(Row row){
		RowBean bean = new RowBean(row);
		return bean.getObject();
	}
}
