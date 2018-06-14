package com.qdone.framework.annotation;

import java.lang.annotation.*;

/**
 * app登录效验
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Login {
	String value() default "";
	boolean isCheck() default true;//是否开启rate限流，针对多读情况，默认开启，不想使用请设置false
 }
