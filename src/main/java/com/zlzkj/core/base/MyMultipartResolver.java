package com.zlzkj.core.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class MyMultipartResolver extends CommonsMultipartResolver {
	
	public String[] excludeUrl; 

	public void setExcludeUrl(String[] excludeUrl) {
		this.excludeUrl = excludeUrl;
	}

	@Override
    public boolean isMultipart(HttpServletRequest request) {
    	
    	//把地址解析成控制器和方法
		String uri = request.getRequestURI().substring(request.getContextPath().length());
		
		//url白名单放行，不交给spring处理
		if(excludeUrl!=null && excludeUrl.length!=0){
			for(String one:excludeUrl){
				if(uri.startsWith(one)){
					return false;
				}
			}
		}

        return super.isMultipart(request);
    }
     
}
