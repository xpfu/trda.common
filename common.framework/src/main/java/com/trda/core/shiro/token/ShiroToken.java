package com.trda.core.shiro.token;

import java.io.Serializable;

import org.apache.shiro.authc.UsernamePasswordToken;

/** 
* @company trda
* @author xp.fu
* @version 2017年7月3日 上午9:10:42
* shiro token
*/
public class ShiroToken extends UsernamePasswordToken implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//登录密码
	private String pwd;

	public ShiroToken(String userName,String pwd){
		super(userName,pwd);
		this.pwd = pwd;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
}
