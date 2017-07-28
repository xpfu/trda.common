package com.trda.core.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;


/** 
* @company trda
* @author xp.fu
* @version 2017年7月27日 上午9:14:47
* 角色判断校验
*/
public class RoleFilter extends AccessControlFilter {

	//登录页面
	private static final String LOGIN_URL = "http://localhost:8080/common.project/user/open/toLogin.shtml";
	//未认证页面
	private static final String UNAUTHORIZED_URL = "http://localhost:8080/common.project/unauthorized.html";
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		String[] area = (String[])mappedValue;
		
		Subject subject = getSubject(request, response);
		for(String role : area){
			if(subject.hasRole("role:" +role)){
				return true;
			}
		}
		
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		
		Subject subject = getSubject(request, response);
		//没有登录重定向到登录页面
		if(null == subject.getPrincipal()){
			saveRequest(request);
			WebUtils.issueRedirect(request, response, LOGIN_URL);
		}else{
			//有未授权页面则跳转过去
			if(StringUtils.hasText(UNAUTHORIZED_URL)){
				WebUtils.issueRedirect(request, response, UNAUTHORIZED_URL);
			}else{
				//否则返回404页面
				WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
		
		return false;
	}

}
