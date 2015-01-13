package com.zlzkj.core.sql;

import java.util.HashMap;

/**
 * 数据元组
 * @author Simon
 *
 */
public class Row extends HashMap<String, Object>{

	private static final long serialVersionUID = 129701856673425221L;

	@Override
    public Object get(Object key) {
        if (super.get(key)!=null) {
            String oType = super.get(key).getClass().getSimpleName();
            if (oType.equals("String[]")){
                return ((String[])super.get(key))[0];
            }else if(oType.equals("byte[]")){
                return (new String((byte[])super.get(key)));
            }
        }
        return super.get(key);
    }
	
	/**
	 * 获取int值
	 * @param key
	 * @return
	 */
	public Integer getInt(Object key){
		if (get(key)!=null) {
			return Integer.valueOf(get(key).toString());
		}else {
			return null;
		}
	}
	
	/**
	 * 获取long值
	 * @param key
	 * @return
	 */
	public Long getLong(Object key){
		if (get(key)!=null) {
			return Long.valueOf(get(key).toString());
		}else {
			return null;
		}
		
	}
	
	/**
	 * 获取short值
	 * @param key
	 * @return
	 */
	public Short getShort(Object key){
		if (get(key)!=null) {
			return Short.valueOf(get(key).toString());
		}else {
			return null;
		}
	}
	
	/**
	 * 获取byte值
	 * @param key
	 * @return
	 */
	public Byte getByte(Object key){
		if (get(key)!=null) {
			return Byte.valueOf(get(key).toString());
		}else {
			return null;
		}
		
	}
	
	/**
	 * 获取float值
	 * @param key
	 * @return
	 */
	public Float getFloat(Object key){
		if (get(key)!=null) {
			return Float.valueOf(get(key).toString());
		}else {
			return null;
		}
	}
	
	/**
	 * 获取double值
	 * @param key
	 * @return
	 */
	public Double getDouble(Object key){
		if (get(key)!=null) {
			return Double.valueOf(get(key).toString());
		}else {
			return null;
		}
	}
	
	/**
	 * 获取整数值
	 * @param key
	 * @return
	 */
	public String getString(Object key){
		if (get(key)!=null) {
			return String.valueOf(get(key).toString());
		}else {
			return null;
		}
	}
	
	
}
