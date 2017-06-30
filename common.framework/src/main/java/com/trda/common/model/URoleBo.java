package com.trda.common.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/** 
* @company trda
* @author xp.fu
* @version 2017年6月28日 下午5:05:54
*/
public class URoleBo extends URole implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//用户ID(考虑多个ID，用String类型)
	private String userId;
	//是否勾选
	private String marker;
	
	public boolean isCheck(){
		return StringUtils.equals(userId, marker);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}
	

}
