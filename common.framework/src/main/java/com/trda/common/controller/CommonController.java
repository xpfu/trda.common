package com.trda.common.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trda.permission.service.RoleService;

/** 
* @company trda
* @author xp.fu
* @version 2017年6月27日 下午3:06:03
*/

@Controller
@RequestMapping("open")
@Scope(value="prototype")
public class CommonController extends BaseController {

	@Resource
	RoleService roleService;
	
	@RequestMapping("refreshDB")
	@ResponseBody
	public Map<String,Object> refreshDB(){
		roleService.initData();
		resultMap.put("status", 200);
		
		return resultMap;
	}
}
