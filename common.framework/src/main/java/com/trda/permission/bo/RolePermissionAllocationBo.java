package com.trda.permission.bo;

import java.io.Serializable;

/** 
* @company trda
* @author xp.fu
* @version 2017年6月28日 上午9:53:21
* 权限分配查询列表BO
*/
public class RolePermissionAllocationBo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//角色
	private Long id;
	//角色type
	private String type;
	//角色name
	private String name;
	//权限name列转行，用','分割
	private String permissionNames;
	//权限ID列转行，用','分割
	private String permissionIds;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPermissionNames() {
		return permissionNames;
	}
	public void setPermissionNames(String permissionNames) {
		this.permissionNames = permissionNames;
	}
	public String getPermissionIds() {
		return permissionIds;
	}
	public void setPermissionIds(String permissionIds) {
		this.permissionIds = permissionIds;
	}
	
}
