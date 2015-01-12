package com.zlzkj.core.mybatis;

import com.zlzkj.core.base.SoMap;



/**
 * 运行自定义sql的Provider实现类
 * @author Simon
 */
public class SqlMapperProvider {
	
	
	public String select(SoMap somap) {
        return somap.get("sql").toString();
    }  
	public String update(SoMap somap) {
		return somap.get("sql").toString();
	}  
	public String insert(SoMap somap) {
		return somap.get("sql").toString();
	}  
	public String delete(SoMap somap) {
		return somap.get("sql").toString();
	}  
      
   
    
    
}
