package com.trda.core.config;
/** 
* @company trda
* @author xp.fu
* @version 2017年7月13日 上午9:07:12
* 读取config.properties配置文件
*/

import java.util.Properties;

import com.trda.common.utils.LoggerUtils;

public class IConfig {

	//同步锁
	private static final Object obj = new Object();
	
	//配置文件
	private static Properties prop = null;
	
	//配置对象单例模式
	private static IConfig config = null;
	
	//配置文件名称
	private static final String FILE_NAME = "/config.properties";
	
	static{
		prop = new Properties();
		
		try {
			prop.load(IConfig.class.getResourceAsStream(FILE_NAME));
		} catch (Exception e) {
			LoggerUtils.fmtError(IConfig.class,e, "加载文件异常，文件路径：%s", FILE_NAME);
		}
	}
	
	/**
	 * 获取单例模式对象实例
	 * @return
	 */
	public static IConfig getInstance(){
		if(null == config){
			synchronized (obj) {
				config = new IConfig();
			}
		}
		return config;
	}
	
	/**
	 * 获取属性值
	 * @param key
	 * @return
	 */
	public static String get(String key){
		return prop.getProperty(key);
	}
}
