package com.trda.core.shiro.cache;

import org.apache.shiro.cache.Cache;

/** 
* @company trda
* @author xp.fu
* @version 2017年7月13日 上午10:49:12
*/
public interface ShiroCacheManager {
	
	<K,V>Cache<K,V> getCache(String name);
	
	void destroy();
}
