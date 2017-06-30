package com.trda.core.shiro.token;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.trda.permission.service.RoleService;

/** 
* @company trda
* @author xp.fu
* @version 2017年6月28日 下午3:28:56
* shiro 认证和授权重写工具类
*/
public class SampleRealm extends AuthorizingRealm {
	
	@Autowired
	UUserService uUserService;
	@Autowired
	PermissionService permissionService;
	@Autowired
	RoleService roleService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}
}
