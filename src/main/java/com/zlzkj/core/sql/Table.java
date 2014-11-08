package com.zlzkj.core.sql;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)

/**
 * 数据库表名注解,引入jpa太大，所以自己写一个
 * @author Simon
 *
 */
public @interface Table {
	
	/**
	 * 数据库中的表名
	 * @return
	 */
	String name() default "";
}
