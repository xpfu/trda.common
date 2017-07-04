package com.trda.core.shiro.token;

import java.util.Date;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.trda.common.model.UUser;
import com.trda.core.shiro.token.manager.TokenManager;
import com.trda.permission.service.PermissionService;
import com.trda.permission.service.RoleService;
import com.trda.user.service.UUserService;

/**
 * @company trda
 * @author xp.fu
 * @version 2017年6月28日 下午3:28:56 shiro 认证和授权重写工具类
 */
public class SampleRealm extends AuthorizingRealm {

	@Autowired
	UUserService uUserService;
	@Autowired
	PermissionService permissionService;
	@Autowired
	RoleService roleService;

	public SampleRealm() {

		
	}

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

		Long userId = TokenManager.getUserId();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// 根据用户ID查询角色（role），放入到Authorization里。
		Set<String> roles = roleService.findRoleByUserId(userId);
		info.setRoles(roles);
		// 根据用户ID查询权限（permission），放入到Authorization里。
		Set<String> permissions = permissionService.findPermissionByUserId(userId);
		info.setStringPermissions(permissions);
		return info;
	}

	/**
	 * 认证信息，主要针对用户登录
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {

		ShiroToken token = (ShiroToken) authcToken;
		UUser user = uUserService.login(token.getUsername(), token.getPwd());
		if (null == user) {
			throw new AccountException("帐号或密码不正确！");
			 
		//如果用户的status为禁用。那么就抛出DisabledAccountException
		} else if (UUser.Long_0.equals(user.getStatus())) {
			throw new DisabledAccountException("帐号已经禁止登录！");
		} else {
			// 更新登录时间 last login time
			user.setLastLoginTime(new Date());
			uUserService.updateByPrimaryKeySelective(user);
		}
		return new SimpleAuthenticationInfo(user, user.getPwd(), getName());
	}
	
	/**
	 * 清空当前用户权限信息
	 */
	public void clearCachedAuthorZationInfo(){
		PrincipalCollection principalCollection = SecurityUtils.getSubject().getPrincipals();
//		SimplePrincipalCollection principals = new SimplePrincipalCollection(principalCollection,getName());
//		super.clearCachedAuthorizationInfo(principals);
		this.clearCachedAuthorizationInfo(principalCollection);
	}
	
	/**
	 * 指定pringcipalcollection 清除用户权限
	 */
	public void clearCachedAuthorizationInfo(PrincipalCollection principalCollection){
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principalCollection,getName());
		super.clearCachedAuthorizationInfo(principals);
	}
	
}
