package com.trda.core.shiro.token.manager;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;

import com.trda.common.model.UUser;
import com.trda.common.utils.SpringContextUtil;
import com.trda.core.shiro.session.CustomSessionManager;
import com.trda.core.shiro.token.SampleRealm;
import com.trda.core.shiro.token.ShiroToken;

/**
 * @company trda
 * @author xp.fu
 * @version 2017年6月28日 下午3:24:41 shiro管理的token工具类
 */
public class TokenManager {

	// 用户登录管理
	public static final SampleRealm realm = SpringContextUtil.getBean("sampleRealm", SampleRealm.class);
	// 用户session管理
	public static final CustomSessionManager customSessionManager = SpringContextUtil.getBean("customSessionManager",
			CustomSessionManager.class);

	/**
	 * 获取当前登录用户的User对象
	 */
	public static UUser getToken() {

		UUser token = (UUser) SecurityUtils.getSubject().getPrincipal();

		return new UUser();
	}

	/**
	 * 获取当前用户的Session
	 */
	public static Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}

	/**
	 * 获取当前用户的昵称
	 */
	public static String getNickName() {
		return getToken().getNickName();
	}

	/**
	 * 获取当前用户的ID
	 */
	public static Long getUserId() {
		return getToken() == null ? null : getToken().getId();
	}

	/**
	 * 把结果放入到当前登录用户的Sesison中
	 */
	public static void setValue2Session(Object key, Object value) {
		getSession().setAttribute(key, value);
	}

	/**
	 * 从当前登录用户的Session中取值
	 */
	public static Object getValueFromSession(Object key) {
		return getSession().getAttribute(key);
	}

	/**
	 * 获取验证码,获取后删除
	 */
	public static String getYZM() {
		String code = (String) getSession().getAttribute("CODE");
		getSession().removeAttribute("CODE");

		return code;
	}

	/**
	 * 登录
	 */
	public static UUser login(UUser user, boolean rememberMe) {
		ShiroToken token = new ShiroToken(user.geteMail(), user.getPwd());
		token.setRememberMe(rememberMe);
		SecurityUtils.getSubject().login(token);

		return getToken();
	}

	// 判断你是否登录
	public static boolean isLogin() {
		return null != SecurityUtils.getSubject().getPrincipal();
	}

	// 退出登录
	public static void logout() {
		SecurityUtils.getSubject().logout();
	}

	/**
	 * 清空当前用户的权限信息 目的：判断权限的时候，会再次调用 doGetAuthorizationInfo的方法 PS:
	 * 也可手动调用doGetAuthorizationInfo的方法 清空了权限后，doGetAuthorizationInfo的方法就会被再次调用
	 */
	public static void clearNowUserAuth() {
		realm.clearCachedAuthorZationInfo();
	}

	/**
	 * 根据用户 UserIds 清空权限信息
	 * 
	 * @param userIDs
	 */
	public static void clearUserAuthByUserId(Long... userIDs) {

		List<SimplePrincipalCollection> result;
		if (null == userIDs || userIDs.length == 0) {
			return;
		} else {
			result = customSessionManager.getSimplePrincipalCollectionByUserId(userIDs);
		}

		for (SimplePrincipalCollection simplePrincipalCollection : result) {
			realm.clearCachedAuthorizationInfo(simplePrincipalCollection);
		}
	}

	public static void clearUserAuthByUserId(List<Long> userIDs) {
		if (null == userIDs || userIDs.size() == 0) {
			return;
		} else {
			clearUserAuthByUserId(userIDs.toArray(new Long[0]));
		}
	}

}
