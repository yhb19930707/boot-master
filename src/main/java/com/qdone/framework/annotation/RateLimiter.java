package com.qdone.framework.annotation;

import java.lang.annotation.*;
/**
 * 限流注解
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    int limit() default 5;//放行数量,5个
    int timeout() default 1000;//限流时间间隔，1秒
}