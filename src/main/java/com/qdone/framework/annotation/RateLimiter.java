package com.qdone.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.qdone.framework.core.constant.Constants;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    int limit() default 5;//放行数量,5个
    int timeout() default 1;//限流时间间隔，默认1秒
    String rateKey() default "";//限流器，自定义key
    //限流器，默认限流时间单位(SECONDS,MINUTES,HOURS,DAYS)
    String timeUnit() default Constants.RateLimiterTimeUnit.SECONDS;
}