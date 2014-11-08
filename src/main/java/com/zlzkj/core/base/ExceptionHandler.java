package com.zlzkj.core.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.zlzkj.core.util.Fn;

public class ExceptionHandler implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		String error = ex.getClass().getName(); //获取异常类型
		error += ":" + ex.getLocalizedMessage(); //获取错误信息
		error += "[";
		StackTraceElement[] stack = ex.getStackTrace(); 
		for (StackTraceElement ste:stack){
        	String currentClassName = ste.getClassName();
		    if(currentClassName.indexOf("com.zlzkj")!=-1){ //注意此处与包名耦合
		    	error += currentClassName + "." + ste.getMethodName() + "@line" + ste.getLineNumber() + ";";
		     }
        }
		error.substring(0, error.length()-1);
		error += "]";
		Fn.ajaxReturn(response, error,"系统发生异常,请重试.",0);
		return null;
	}  

}
