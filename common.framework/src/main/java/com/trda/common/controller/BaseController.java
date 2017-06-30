package com.trda.common.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/** 
* @company trda
* @author xp.fu
* @version 2017年6月27日 下午2:54:29
*/
public class BaseController {
	
	protected int pageNo = 1;
	public static int pageSize = 10;
	protected final static Logger logger = Logger.getLogger(BaseController.class);
	protected Map<String,Object> resultMap = new LinkedHashMap<String,Object>();
	public static String URL404 = "/404.html";
	
	private final static String PARAM_PAGE_NO = "pageNo";
	protected String pageSizeName = "pageSize";
	
	/**
	 * 向request中设置值
	 */
	protected static void setValue2Request(HttpServletRequest request,String key,Object value){
		request.setAttribute(key, value);
	}
	
	/**
	 * 获取session
	 */
	public static HttpSession getSession(HttpServletRequest request){
		return request.getSession();
	}
	
	public int getPageNo(){
		return pageNo;
	}
	public void setPageNo(int pageNo){
		this.pageNo = pageNo;
	}
	public int getPageSize(){
		return pageSize;
	}
}
