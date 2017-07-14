package com.trda.core.shiro.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;

import com.trda.common.model.UUser;
import com.trda.common.utils.LoggerUtils;
import com.trda.core.shiro.token.manager.TokenManager;

/**
 * @company trda
 * @author xp.fu
 * @version 2017年7月13日 下午4:41:37 登录信息判断
 */
public class LoginFilter extends AccessControlFilter {

	private final static Class<LoginFilter> CLASS = LoginFilter.class;

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mapperValue)
			throws Exception {

		UUser token = TokenManager.getToken();
		if (null != token || isLoginRequest(request, response)) {
			return Boolean.TRUE;
		}
		// ajax请求
		if (ShiroFilterUtils.isAjax(request)) {
			Map<String, String> resultMap = new HashMap<String, String>();
			LoggerUtils.debug(getClass(), "当前用户没有登录，并且是ajax请求");
			resultMap.put("login_status", "300");
			// 消息提示：当前用户没有登录
			resultMap.put("message", "\u5F53\u524D\u7528\u6237\u6CA1\u6709\u767B\u5F55\uFF01");
			ShiroFilterUtils.out(response, resultMap);
		}

		return Boolean.FALSE;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

		//保存request和response到登录后的链接
		saveRequestAndRedirectToLogin(request, response);
		return Boolean.FALSE;
	}

}
