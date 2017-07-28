package com.trda.core.shiro.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import com.trda.core.shiro.session.ShiroSessionRepository;

/**
 * @company trda
 * @author xp.fu
 * @version 2017年7月28日 上午10:28:38
 * 
 *          shiro 会话 监听
 */
public class CustomSessionListener implements SessionListener {

	private ShiroSessionRepository shiroSessionRepository;

	// 生命周期的开始
	@Override
	public void onStart(Session session) {
		System.out.println("begin shiro session onStart");
	}

	// 生命周期的技术
	@Override
	public void onStop(Session session) {
		System.out.println("begin shiro session onStop");
	}

	@Override
	public void onExpiration(Session session) {
		shiroSessionRepository.deleteSession(session.getId());
	}

	public ShiroSessionRepository getShiroSessionRepository() {
		return shiroSessionRepository;
	}

	public void setShiroSessionRepository(ShiroSessionRepository shiroSessionRepository) {
		this.shiroSessionRepository = shiroSessionRepository;
	}

}
