package com.zlzkj.core.base;

/**
 * 全局系统常量
 * @author Simon
 *
 */
public class Constants {
	
//	//用户登录密码杂质
//	public static String PASSWORD_SALT = "zlzkj.com";
//	
//	//用户标识
//	public static String USER_ID = "userId";
//	public static String USER_INFO = "userInfo";
	
	//css/js/image等网站静态资源目录，跟spring配置文件中一致
	public static String STATIC_PATH = "/static";
	
	//伪静态后缀
	public static String URL_SUFFIX = ".html";
	
	//网站context path - 在BaseInterceptor中赋值
	public static String CONTEXT_PATH;
	
}
