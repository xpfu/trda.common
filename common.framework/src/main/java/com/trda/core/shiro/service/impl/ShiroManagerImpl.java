package com.trda.core.shiro.service.impl;

import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.trda.common.utils.LoggerUtils;
import com.trda.core.config.INI4j;
import com.trda.core.shiro.service.ShiroManager;

/**
 * @company trda
 * @author xp.fu
 * @version 2017年7月28日 上午10:50:27
 * 
 *          动态加载权限
 */
public class ShiroManagerImpl implements ShiroManager {

	// 注意/r/n前不能有空格
	private static final String CRLF = "\r\n";

	@Resource
	@Autowired
	private ShiroFilterFactoryBean shiroFilterFactoryBean;

	/**
	 * 加载过滤配置信息
	 */
	@Override
	public String loadFilterChainDefinitions() {
		StringBuffer sb = new StringBuffer();
		// 从配置文件读取固定权限
		sb.append(getFixedAuthRule());

		return sb.toString();
	}

	/**
	 * 从配置文件读取固定权限规则
	 */
	private String getFixedAuthRule() {
		StringBuffer strResult = new StringBuffer();
		String section = "base_auth";

		String fileName = "shiro_base_auth.ini";
		ClassPathResource cpr = new ClassPathResource(fileName);
		INI4j ini4j = null;
		try {
			ini4j = new INI4j(cpr.getFile());
		} catch (Exception e) {
			LoggerUtils.fmtError(getClass(), e, "加载文件出错。file:[%s]", fileName);
		}

		Set<String> keys = ini4j.get(section).keySet();
		for (String key : keys) {
			String value = ini4j.get(section, key);
			strResult.append(key).append(" = ").append(value).append(CRLF);
		}

		return strResult.toString();
	}

	/**
	 * 重新构建权限过滤器 一般在修改了用户角色、用户等信息时，需要再次调用该方法 加上同步锁
	 */
	@Override
	public synchronized void reCreateFilterChains() {

		AbstractShiroFilter abstractShiroFilter = null;
		try {
			abstractShiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
		} catch (Exception e) {
			LoggerUtils.error(getClass(), "getShiroFilter from shiroFilterFactoryBean error!", e);
			throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
		}

		PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) abstractShiroFilter
				.getFilterChainResolver();
		DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
		
		//清空老的权限控制
		manager.getFilterChains().clear();
		shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
		shiroFilterFactoryBean.setFilterChainDefinitions(loadFilterChainDefinitions());
		
		//重新构建生成
		Map<String,String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
		for(Map.Entry<String, String> entry : chains.entrySet()){
			String url = entry.getKey();
			String chainDefinition = entry.getValue().trim().replace(" ", "");
			manager.createChain(url, chainDefinition);
		}
	}

	public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
		return shiroFilterFactoryBean;
	}

	public void setShiroFilterFactoryBean(ShiroFilterFactoryBean shiroFilterFactoryBean) {
		this.shiroFilterFactoryBean = shiroFilterFactoryBean;
	}
	
}
