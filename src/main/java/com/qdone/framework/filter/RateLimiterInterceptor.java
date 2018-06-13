package com.qdone.framework.filter;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.qdone.framework.annotation.RateLimiter;
import com.qdone.framework.exception.RRException;
import com.qdone.framework.util.rate.RedisRateLimiter;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author 付为地
 *   接口限流拦截器
 */
@Component
public class RateLimiterInterceptor extends HandlerInterceptorAdapter {
	
	private Logger logger = LoggerFactory.getLogger(RateLimiterInterceptor.class);
	
	 @Autowired
     private JedisPool jedisPool;
	  
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		 HandlerMethod handlerMethod = (HandlerMethod) handler;
         Method method = handlerMethod.getMethod();
         RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);
         if (rateLimiter != null) {
             int limit = rateLimiter.limit();
             int timeout = rateLimiter.timeout();
             Jedis jedis = jedisPool.getResource();
             String token = RedisRateLimiter.acquireTokenFromBucket(jedis, method.getName(), limit, timeout);
             if (token == null) {
            	 /*继续让Spring全局异常处理，跳转页面*/
				throw new RRException("接口请求超过限流", HttpStatus.INTERNAL_SERVER_ERROR.value());
             }
             logger.debug("token -> {}", token);
             jedis.close();
         }
         return true;
	}

}
