package com.trda.core.shiro.filter;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.trda.common.utils.LoggerUtils;

import net.sf.json.JSONObject;

/**
 * @company trda
 * @author xp.fu
 * @version 2017年7月14日 上午9:21:28 shiro filter 工具类
 */
public class ShiroFilterUtils {

	private final static Class<? extends ShiroFilterUtils> CLAZZ = ShiroFilterUtils.class;

	// 登录页面
	final static String LOGIN_URL = "/u/login.shtml";
	// 跳出登录提示
	final static String KICKED_OUT = "/open/kickedOut.shtml";
	// 没有权限提醒
	final static String UNAUTHORIZED = "/open/unauthorized.shtml";

	/**
	 * 判断是否是ajax请求
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isAjax(ServletRequest request) {
		return "XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"));
	}

	/**
	 * 输出json数据
	 * 
	 * @param response
	 * @param resultMap
	 */
	public static void out(ServletResponse response, Map<String, String> resultMap) {
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-9");
			out = response.getWriter();
			out.println(JSONObject.fromObject(resultMap).toString());
		} catch (Exception e) {
			LoggerUtils.fmtError(CLAZZ, e, "输出JSON报错。");
		} finally {
			if (null != out) {
				out.flush();
				out.close();
			}
		}
	}

}
