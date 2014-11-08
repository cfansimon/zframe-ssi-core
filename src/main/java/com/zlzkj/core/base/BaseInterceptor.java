package com.zlzkj.core.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zlzkj.core.util.Fn;
import com.zlzkj.core.util.SpringContextUtil;

/**
 * 核心拦截器，配置request的一些初始值
 * @author Simon
 *
 */
public class BaseInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		String contextPath = Fn.rtrim(SpringContextUtil.getContextPath(),"/");
		//网站根路径
		request.setAttribute("__root__", contextPath);
		//把以上值放入全局常量中
		if(Constants.CONTEXT_PATH==null){
			Constants.CONTEXT_PATH = contextPath;
		}
		//CSS、JS静态资源目录
		request.setAttribute("__static__", contextPath + "/" +Fn.ltrim(Constants.STATIC_PATH,"/"));
		//当前页面的URL
		request.setAttribute("__url_with_query__",request.getRequestURI()+"?"+Fn.nullToEmpty(request.getQueryString()));
		
		request.setAttribute("__url__",request.getRequestURI());
		
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
