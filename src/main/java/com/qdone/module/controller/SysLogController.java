package com.qdone.module.controller;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qdone.common.util.log.LogUtil;
import com.qdone.common.util.log.SysLog;
import com.qdone.framework.core.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 *sysLog管理
 * @付为地
 * @date 2018-07-13 01:07:48
 */
@Controller
@RequestMapping("/sysLog")
@Api(tags = "日志表管理",description = "日志表信息管理")
public class SysLogController extends BaseController{
  

    @Autowired
	private LogUtil logUtil;

    /**
	 * 页面初始化
	 */
	@ApiOperation(value = "日志表列表",notes = "进入日志表列表页", httpMethod = "GET")
	@RequestMapping(value = "init",method = RequestMethod.GET)
	public String init(){
		return "sysLog/selectSysLog";
	}
	
	 /**
	 * 分页查询
	 * @param entity 查询参数
	 * @return 分页查询结果
	 */
	@RequestMapping(value="/selectPage",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "日志表分页列表", notes = "日志表分页列表", httpMethod = "POST",response = Map.class)
	public Map<String, Object> selectPage(@RequestBody SysLog entity){
		return responseSelectPage(logUtil.selectPage(entity));
	}
	
	/**
	 * 跳转添加
	 * @param req 请求参数
	 * @return 跳转添加页面
	*/
    @RequestMapping(value="/preInsert",method=RequestMethod.GET)
    @ApiOperation(value = "跳转日志表添加", notes = "进入日志表添加页面", httpMethod = "GET")
	public String preInsert(HttpServletRequest req){
		return "sysLog/insertSysLog";
	} 
	
    /**
     * 添加数据
     * @param entity 对象参数
	 * @return 添加数据
     */
	@RequestMapping(value="/insert",method=RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "日志表添加", notes = "创建日志表", httpMethod = "PUT",response = Boolean.class)
	public Boolean insert(@ApiParam(name = "日志表对象", value = "传入json格式", required = true)  @RequestBody SysLog entity) {
		return logUtil.insert(entity)>0?Boolean.TRUE:Boolean.FALSE;
	}
	
	/**
	 * 跳转更新
	 * @param req 请求参数
	 * @return 跳转更新页面
	*/
    @RequestMapping(value="/preUpdate",method=RequestMethod.GET)
    @ApiOperation(value = "跳转日志表更新", notes = "进入日志表更新页面", httpMethod = "GET")
	public String preUpdate(HttpServletRequest request){
	    request.setAttribute("sysLog", logUtil.view(Integer.parseInt(request.getParameter("id"))));
		return "sysLog/updateSysLog";
	} 
    
}
