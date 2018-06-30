package com.qdone.module.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.qdone.common.util.SessionUtil;
import com.qdone.framework.core.BaseController;
import com.qdone.framework.core.constant.Constants;
import com.qdone.module.model.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 
 * @author 付为地
 *   简单演示登陆流程，不做权限验证
 */
@Api(description = "用户登陆管理", tags = "用户登陆")
@Controller
@RequestMapping("/")
public class LoginController extends BaseController{
	
	/**
	 * 登陆页面
	 */
	@ApiOperation(value = "登陆", notes = "登陆", httpMethod = "GET")
	@GetMapping(value = "/init")
	public String init(){
		return "login";
	}
	
	/**
	 * 执行页面
	 */
	@ApiOperation(value = "登陆系统", notes = "登陆系统", httpMethod = "POST")
	@PostMapping(value = "/login")
	public String welcome(User user){
		if(ObjectUtils.isEmpty(user)){
			user=new User("灭霸","123456",1500,"");
		}
		SessionUtil.setSessionObject(Constants.CURRENT_USER, user);
		return "redirect:/main";
	}
	
	/**
	 * 注册页
	 */
	@ApiOperation(value = "注册页", notes = "注册页", httpMethod = "GET")
	@GetMapping(value = "/toRegister")
	public String toRegister(){
		return "register";
	}
	
	/**
	 * 执行注册
	 */
	@ApiOperation(value = "执行注册", notes = "执行注册", httpMethod = "POST")
	@PostMapping(value = "/register")
	public String register(User user){
		System.err.println(user);
		/*if(ObjectUtils.isEmpty(user)){
			user=new User("灭霸","123456",1500,"");
		}
		SessionUtil.setSessionObject(Constants.CURRENT_USER, user);*/
		return "login";
	}
	
	/**
	 * 主页
	 */
	@ApiOperation(value = "主页", notes = "主页", httpMethod = "GET")
	@GetMapping(value = "/main")
	public String index(HttpServletRequest request,Device device){
		User user = (User) SessionUtil.getSessionObject(Constants.CURRENT_USER);
		if(ObjectUtils.isEmpty(user)){
			return "login";
		}
		String deviceType="unknown";
	    if(device.isNormal()){
        	deviceType = "pc";//Pc端
        }
        else if (device.isMobile()){
        	deviceType = "mobile";//手机端
        }
        else if (device.isTablet()){
        	deviceType = "tablet";//平板
        }
	    System.err.println("设备类型:"+deviceType);
		request.setAttribute(Constants.CURRENT_DEVICE_TYPE, deviceType);
		return "index";
	}
	
	@ApiOperation(value = "退出登录", notes = "退出登录", httpMethod = "GET")
	@RequestMapping(value = "/logout", method =RequestMethod.GET)
	public String logout(HttpServletRequest request){
		request.getSession().invalidate();
		return "redirect:/";
	}
	
	
	@ApiOperation(value = "控制台", notes = "控制台页面", httpMethod = "GET")
	@GetMapping(value = "/hello")
	public String hello(){
		return "hello";
	}
}
