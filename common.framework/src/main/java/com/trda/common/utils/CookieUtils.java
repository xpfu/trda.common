
package com.trda.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
* @company trda
* @author xp.fu
* @version 2017年7月5日 上午10:59:42
* Cookie工具类
*/
public class CookieUtils {

	/**
	 * 设置Cookie
	 * @param response
	 * @param name
	 * @param value
	 * @param maxAge
	 */
	public static void addCookie(HttpServletResponse response,String name,String value,int maxAge){
		try {
			Cookie cookie = new Cookie(name,value);
			if(maxAge > 0){
				cookie.setMaxAge(maxAge);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		} catch (Exception e) {
			LoggerUtils.error(CookieUtils.class, "创建Cookies发生异常",e);
		}
	}
	
	/**
	 * 清空Cookie
	 * @param request
	 * @param response
	 * @param name
	 */
	public static boolean clearCookie(HttpServletRequest request,HttpServletResponse response,String name){
		
		boolean returnFlag = false;
		
		Cookie[] cookies = request.getCookies();
		if(null == cookies || cookies.length == 0){
			return returnFlag;
		}
		try {
			for(int i = 0;i < cookies.length; i++){
				Cookie tempCookie = new Cookie(name,null);
				tempCookie.setMaxAge(0);
				tempCookie.setPath("/");
				response.addCookie(tempCookie);
				returnFlag = true;
			}
		} catch (Exception e) {
			LoggerUtils.error(CookieUtils.class, "清空Cookies发生异常",e);
		}
		
		return returnFlag;
	}
	
	/**
	 * 根据节点域
	 * 
	 * 清空Cookie操作
	 * @param request
	 * @param response
	 * @param name
	 * @param domain
	 * @return
	 */
	public static boolean clearCookie(HttpServletRequest request,HttpServletResponse response,String name,String domain){
		boolean returnFlag = false;
		
		Cookie[] cookies = request.getCookies();
		if(null == cookies || cookies.length == 0){
			return returnFlag;
		}
		try {
			for(int i = 0; i < cookies.length; i++){
				Cookie tempCookie = new Cookie(name,null);
				tempCookie.setMaxAge(0);
				tempCookie.setPath("/");
				tempCookie.setDomain(domain);
				response.addCookie(tempCookie);
				returnFlag = true;
			}
		} catch (Exception e) {
			LoggerUtils.error(LoggerUtils.class, "清空Cookies发生异常");
		}
		
		return returnFlag;
	}
	
	/**
	 * 根据指定Cookie名获取相应的Cookie
	 * @param request
	 * @param name
	 * @return
	 */
	public static String findCookieByName(HttpServletRequest request,String name){
		String strResult = "";
		
		Cookie[] cookies = request.getCookies();
		if(null == cookies || cookies.length == 0){
			return strResult;
		}
		try {
			for(int i = 0;i < cookies.length; i++){
				Cookie tempCookie = cookies[i];
				String cookieName = tempCookie.getName();
				
				if(!StringUtils.isBlank(cookieName) && cookieName.equals(name)){
					strResult = cookieName;
					break;
				}
			}
		} catch (Exception e) {
			LoggerUtils.error(CookieUtils.class, "获取Cookie发生异常",e);
		}
		
		return strResult;
	}
	
	
}
