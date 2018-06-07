package com.qdone.framework.filter;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.qdone.common.util.JwtUtils;
import com.qdone.common.util.Result;
import com.qdone.framework.annotation.Login;
import com.qdone.framework.core.constant.Constants;
import com.qdone.framework.exception.RRException;
import com.qdone.module.model.User;

import io.jsonwebtoken.Claims;

/**
 * @author 付为地
 *    APP登录token验证，需要登录的路径，必须同时传递token和userId
 *  防止A客户模拟B客户，进行系统操作
 */

@Component
public class AppInterceptor extends HandlerInterceptorAdapter {
	
    @Autowired
    private JwtUtils jwtUtils;

    public static final String USER_KEY = "userId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	request.setCharacterEncoding(Constants.CHARSET.UTF8);
    	response.setCharacterEncoding(Constants.CHARSET.UTF8);
    	Login annotation;
        if(handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Login.class);
        }else{
            return true;
        }
        if(annotation == null){
            return true;
        }
        //获取用户凭证
        String token = request.getHeader(jwtUtils.getHeader());
        if(StringUtils.isBlank(token)){
            token = request.getParameter(jwtUtils.getHeader());
        }
        //凭证为空
        if(StringUtils.isBlank(token)){
            throw new RRException(jwtUtils.getHeader() + ",不能为空", HttpStatus.UNAUTHORIZED.value());
        }
        //获取用户编号
        String userId =StringUtils.isBlank(request.getHeader(USER_KEY))?request.getParameter(USER_KEY):request.getHeader(USER_KEY);
        if(StringUtils.isBlank(userId)){
        	throw new RRException(USER_KEY + ",不能为空", HttpStatus.UNAUTHORIZED.value());
        }
        //验证token时效
        Claims claims = jwtUtils.getClaimByToken(token);
        if(claims == null || jwtUtils.isTokenExpired(claims.getExpiration())){
            throw new RRException(jwtUtils.getHeader() + "失效，请重新登录", HttpStatus.UNAUTHORIZED.value());
        }
        if(StringUtils.isNotBlank(userId)&&StringUtils.isNotBlank(token)
            &&claims!=null&&!jwtUtils.isTokenExpired(claims.getExpiration())){
        	if(!jwtUtils.exists(jwtUtils.AppTokenPrefix+claims.getSubject())){
        		throw new RRException(jwtUtils.getHeader() + "失效，请重新登录", HttpStatus.UNAUTHORIZED.value());
        	}
            if(!userId.equals(claims.getSubject())){
            	throw new RRException(jwtUtils.getHeader() + "非法,请确保本人操作", HttpStatus.UNAUTHORIZED.value());
            }
        }
        /*token自动续期策略，重新生成新token使用，不考虑强制替换cookie内存储的旧token,半小时内重新生成新token*/
        long remain=claims.getExpiration().getTime()-new Date().getTime();
        if(remain<=180000000){
        	String freshToken=jwtUtils.refreshTokenExpiration(token);
        	request.setAttribute("freshToken", freshToken);
        	Result<User> resp=new Result<User>();
        	resp.setCode(200);
        	resp.setBizCode(401);
        	resp.setMsg("令牌失效,请使用新令牌请求!");
        	resp.setData(new User(claims.getSubject(),token,freshToken));
        	PrintWriter pw=response.getWriter();
        	pw.write(JSON.toJSONString(resp));
        	pw.close();
        	return false;
        }
        //设置userId到request里，后续根据userId，获取用户信息
        /*request.setAttribute(USER_KEY, Long.parseLong(claims.getSubject()));*/
        return true;
    }
}
