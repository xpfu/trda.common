package com.trda.common.utils;

import javax.management.RuntimeErrorException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/** 
* @company trda
* @author xp.fu
* @version 2017年6月30日 上午11:21:15
* 静态获取bean工具类
*/
public class SpringContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	public static ApplicationContext getApplicationContext(){
		return applicationContext;
	}
	
	public static Object getBean(String name) throws BeansException{
		try {
			return applicationContext.getBean(name);
			
		} catch (Exception e) {
			throw new RuntimeException("获取的bean不存在");
		}
	}
	
	public static<T> T getBean(String name,Class<T> requiredType){
		return applicationContext.getBean(name,requiredType);
	}
	
	public static boolean containsBean(String name){
		return applicationContext.containsBean(name);
	}
	
	public static boolean isSingleton(String name){
		return applicationContext.isSingleton(name);
	}
	
	public static Class<? extends Object> getType(String name){
		return applicationContext.getType(name);
	}
	
	public static String[] getAliases(String name){
		return applicationContext.getAliases(name);
	}

}
