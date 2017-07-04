package com.trda.permission.bo;
/** 
* @company trda
* @author xp.fu
* @version 2017年7月3日 上午9:57:12
* 权限选择
*/

import java.io.Serializable;

import com.trda.common.model.UPermission;
import com.trda.common.utils.StringUtils;

public class UPermissionBo extends UPermission implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//是否勾选
	private String marker;
	//role ID
	private String roleId;
	
	public boolean isCheck(){
		return StringUtils.equals(roleId, marker);
	}

	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}
