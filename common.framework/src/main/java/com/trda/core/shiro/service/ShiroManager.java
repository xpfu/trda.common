package com.trda.core.shiro.service;

/**
 * @company trda
 * @author xp.fu
 * @version 2017年7月28日 上午10:46:32
 * 
 *          动态加载权限
 */
public interface ShiroManager {

	/**
	 * 加载过滤配置信息
	 * @return
	 */
	public String loadFilterChainDefinitions();

	/**
	 * 重新构建权限过滤器 一般在修改了用户角色、用户等信息时，需要再次调用该方法
	 */
	public void reCreateFilterChains();
}
