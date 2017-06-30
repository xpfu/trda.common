package com.trda.common.model;

import java.io.Serializable;

import net.sf.json.JSONObject;

/** 
* @company trda
* @author xp.fu
* @version 2017年6月27日 下午3:49:08
* 
* 权限实体
*/
public class UPermission implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	//操作的URL
	private String url;
	
	//操作的名称
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		
		return JSONObject.fromObject(this).toString();
	}
}
