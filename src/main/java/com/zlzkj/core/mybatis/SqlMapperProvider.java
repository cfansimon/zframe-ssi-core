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
      
   
    
    
}
