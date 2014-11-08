package com.zlzkj.core.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)

/**
 * api信息注解<br/>
 * 可以用在类上或者方法上<br/>
 * 示例: @ApiInfo(desc="用户api",params={"loginName=登录名","loginPass=密码"})
 * @author Simon
 *
 */
public @interface ApiInfo {
	
	/**
	 * api的描述
	 * @return
	 */
	String desc() default "";
	
	/**
	 * api的参数列表<br/>
	 * 参数名和参数描述用等于号分割
	 * @return
	 */
	String[] params() default {};
}
