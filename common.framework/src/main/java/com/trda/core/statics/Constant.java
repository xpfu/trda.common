package com.trda.core.statics;

import java.util.Calendar;

import com.trda.core.config.IConfig;

/**
 * @company trda
 * @author xp.fu
 * @version 2017年7月28日 下午3:34:09
 * 
 *          静态变量
 */
public interface Constant {
	// 项目根路径 
	static final String CONTEXT_PATH = "contextPath";
	
	/** freemarker 使用的变量 begin **/
	// 标签使用目标
	static final String TARGET = "target";
	// 输出标签Name
	static final String OUT_TAG_NAME = "outTagName";


	/** 其他常用变量 begin **/
	static final String NAME = "name";
	static final String ID = "id";
	static final String TOKEN = "token";
	static final String LOING_USER = "loing_user";
	
	/** Long */
	static final Long ZERO = new Long(0);
	static final Long ONE = new Long(1);
	static final Long TWO = new Long(2);
	static final Long THREE = new Long(3);
	static final Long EIGHT = new Long(8);

	/** String */
	static final String S_ZERO = "0";
	static final String S_ONE = "1";
	static final String S_TOW = "2";
	static final String S_THREE = "3";

	/** Integer */
	static final Integer I_ZERO = 0;
	static final Integer I_ONE = 1;
	static final Integer I_TOW = 2;
	static final Integer I_THREE = 3;

	/** cache常用变量 begin **/
	static final String CACHE_NAME = "shiro_cache";
	// cacheManager bean name
	static final String CACHE_MANAGER = "cacheManager";

	/** 当前年份 **/
	static final int NOW_YEAY = Calendar.getInstance().get(Calendar.YEAR);

	/** 地址 **/
	// 前端域名
	static final String DOMAIN_WWW = IConfig.get("domain.www");
	// 静态资源域名
	static final String DOMAIN_CDN = IConfig.get("domain.cdn");
	// 版本号，重启的时间
	static String VERSION = String.valueOf(System.currentTimeMillis());

	// 存储到缓存，标识用户的禁止状态，解决在线用户踢出的问题
	final static String EXECUTE_CHANGE_USER = "TRDA_EXECUTE_CHANGE_USER";
}
