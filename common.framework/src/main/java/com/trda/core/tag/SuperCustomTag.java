package com.trda.core.tag;

import java.util.Map;

import com.trda.common.utils.StringUtils;

/**
 * @company trda
 * @author xp.fu
 * @version 2017年8月1日 上午11:01:17
 */
public abstract class SuperCustomTag {

	/**
	 * 本方法采用多态集成的方式，然后用父类接收，用父类调用子类的 {@link result(...)} 方法。
	 */
	protected abstract Object result(Map params);

	/**
	 * 直接强转报错，需要用Object过度一下
	 */
	protected Long getLong(Map params, String key) {
		Object i = params.get(key);
		return StringUtils.isBlank(i) ? null : new Long(i.toString());
	}

	protected String getString(Map params, String key) {
		Object i = params.get(key);
		return StringUtils.isBlank(i) ? null : i.toString();
	}

	protected Integer getInt(Map params, String key) {
		Object i = params.get(key);
		return StringUtils.isBlank(i) ? null : Integer.parseInt(i.toString());
	}
}