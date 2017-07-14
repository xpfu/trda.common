package com.trda.core.shiro.filter;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import com.trda.common.utils.LoggerUtils;
import com.trda.core.shiro.cache.VCache;
import com.trda.core.shiro.session.ShiroSessionRepository;

import net.sf.json.JSONObject;

/** 
* @company trda
* @author xp.fu
* @version 2017年7月14日 上午10:24:54
* 相同账号登录控制
*/
public class KickoutSessionFilter extends AccessControlFilter {

	static String kickoutUrl;
	//在线用户
	final static String ONELINE_USER = KickoutSessionFilter.class.getCanonicalName() + "_online_user";
	//踢出状态,true表示踢出
	final static String KICKOUT_STATUS = KickoutSessionFilter.class.getCanonicalName() + "_kickout_status";
	//缓存数据
	static VCache cache;
	//获取session
	static ShiroSessionRepository shiroSessionRepository;
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedResult) throws Exception {
		
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		String url = httpRequest.getRequestURI();
		Subject subject = getSubject(request,response);
		
		//如果是相关目录 或者 没有登录 直接返回true
		if(url.startsWith("/open/") || (!subject.isAuthenticated() && !subject.isRemembered())){
			return Boolean.TRUE;
		}
		
		Session session = subject.getSession();
		Serializable sessionId = session.getId();
		
		//判断是否已经踢出
		//1.如果是ajax请求则返回json值
		//2.如果是普通请求则直接跳转登录页面
		Boolean marker = (Boolean)session.getAttribute(KICKOUT_STATUS);
		if(null != marker && marker){
			Map<String,String> resultMap = new HashMap<String,String>();
			
			//判断是否是ajax请求
			if(ShiroFilterUtils.isAjax(request)){
				LoggerUtils.debug(getClass(), "当前用户已经在其他地方登录，并且是Ajax请求！");
				resultMap.put("user_status", "300");
				resultMap.put("message", "您已经在其他地方登录，请重新登录！");
				out(response, resultMap);
			}
			return Boolean.FALSE;
		}
		
		//从缓存获取用户session信息<UserId,SessionId>
		LinkedHashMap<Long, Serializable> infoMap = cache.get(ONELINE_USER, LinkedHashMap.class);
		
		return Boolean.FALSE;
	}
	
	
	
	

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void out(ServletResponse response,Map<String,String> resultMap){
		try {
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.println(JSONObject.fromObject(resultMap).toString());
			out.flush();
			out.close();
		} catch (Exception e) {
			LoggerUtils.error(getClass(), "KickoutSessionFilter.class 输出JSON异常，可以忽略。");
		}
	}

}
