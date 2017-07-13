package com.trda.core.shiro.session;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.session.Session;

/** 
* @company trda
* @author xp.fu
* @version 2017年6月28日 下午5:30:39
* Session操作
*/
public interface ShiroSessionRepository {

	//存储session
	void saveSession(Session session);
	
	//删除session
	void deleteSession(Serializable sessionId);
	
	//获取session
	Session getSession(Serializable sessionId);
	
	//获取所有session
	Collection<Session> getAllSessions();
}
