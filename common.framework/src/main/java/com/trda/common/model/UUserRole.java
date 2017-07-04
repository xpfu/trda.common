package com.trda.common.model;

import java.io.Serializable;

import net.sf.json.JSONObject;

/**
 * @company trda
 * @author xp.fu
 * @version 2017年7月3日 下午3:13:25 用户 和 角色 关联的中间表
 */
public class UUserRole implements Serializable {

	private static final long serialVersionUID = 1L;

	// @link UUser.id
	private Long uid;
	// @link URole.id
	private Long rid;

	public UUserRole(Long uid, Long rid) {
		this.uid = uid;
		this.rid = rid;
	}

	public UUserRole() {

	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	public String toString() {
		return JSONObject.fromObject(this).toString();
	}

}
