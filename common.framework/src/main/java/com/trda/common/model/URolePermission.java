package com.trda.common.model;

import java.io.Serializable;

import net.sf.json.JSONObject;

/**
 * @company trda
 * @author xp.fu
 * @version 2017年7月3日 下午12:01:20 角色和权限中间链接表
 */
public class URolePermission implements Serializable {

	private static final long serialVersionUID = 1L;

	// @link URloe.id
	private Long rid;
	// @link UPermission.id
	private Long pid;

	public URolePermission() {

	}

	public URolePermission(Long rid, Long pid) {
		this.rid = rid;
		this.pid = pid;
	}

	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String toString() {
		return JSONObject.fromObject(this).toString();
	}

}
