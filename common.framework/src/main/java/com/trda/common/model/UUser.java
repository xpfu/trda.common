package com.trda.common.model;

import java.io.Serializable;
import java.util.Date;

import net.sf.json.JSONObject;

/**
 * @company trda
 * @author xp.fu
 * @version 2017年6月28日 下午3:37:54 用户信息
 */
public class UUser implements Serializable {

	private static final long serialVersionUID = 1L;

	// 0 : 登录无效(禁止登录)
	public static final Long Long_0 = new Long(0);
	// 1 : 登录有效(可以登录)
	public static final Long Long_1 = new Long(1);

	private Long id;
	// 昵称
	private String nickName;
	// 邮箱||登录账号
	private String eMail;
	// 密码
	private transient String pwd;
	// 创建时间
	private Date createTime;
	// 最后登录时间
	private Date lastLoginTime;
	// 1:有效 2:禁止登录
	private Long status;

	public UUser() {
	}

	public UUser(UUser user) {
		this.id = user.getId();
		this.nickName = user.getNickName();
		this.eMail = user.geteMail();
		this.pwd = user.getPwd();
		this.createTime = user.getCreateTime();
		this.lastLoginTime = user.getLastLoginTime();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String toString() {
		return JSONObject.fromObject(this).toString();
	}

}
