package com.trda.user.bo;

import java.io.Serializable;
import java.util.Date;

import com.trda.common.model.UUser;

/** 
* @company trda
* @author xp.fu
* @version 2017年6月28日 下午6:11:55
* session + user Bo
*/
public class UserOnlineBo extends UUser implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//Session Id
	private String sessionId;
	//Session Host
	private String host;
	//Session创建时间
	private Date startTime;
	//Session最后交互时间
	private Date lastAccess;
	//Session timeout
	private long timeOut;
	//Session是否剔出
	private boolean sessionStatus = Boolean.TRUE;
	
	public UserOnlineBo(){
	}
	
	public UserOnlineBo(UUser user){
		super(user);
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	public boolean isSessionStatus() {
		return sessionStatus;
	}

	public void setSessionStatus(boolean sessionStatus) {
		this.sessionStatus = sessionStatus;
	}
	
}
