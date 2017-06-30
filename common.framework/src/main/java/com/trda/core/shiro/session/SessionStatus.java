package com.trda.core.shiro.session;

import java.io.Serializable;

/** 
* @company trda
* @author xp.fu
* @version 2017年6月29日 上午10:31:00
* session 状态VO
*/
public class SessionStatus implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//是否踢出 true:有效  false:无效(踢出)
	private Boolean onlineStatus = Boolean.TRUE;

	public Boolean getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(Boolean onlineStatus) {
		this.onlineStatus = onlineStatus;
	}
	
}
