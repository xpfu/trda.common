package com.trda.user.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import com.trda.common.dao.UUserMapper;
import com.trda.common.dao.UUserRoleMapper;
import com.trda.common.model.UUser;
import com.trda.core.mybatis.BaseMybatisDao;
import com.trda.core.mybatis.page.Pagination;
import com.trda.core.shiro.session.CustomSessionManager;
import com.trda.permission.bo.URoleBo;
import com.trda.permission.bo.UserRoleAllocationBo;
import com.trda.user.service.UUserService;

/** 
* @company trda
* @author xp.fu
* @version 2017年6月28日 下午5:11:03
*/
public class UUserServiceImpl extends BaseMybatisDao<UUserMapper> implements UUserService {

	@Autowired
	CustomSessionManager customSessionManager;
	@Autowired
	UUserMapper uUserMapper;
	@Autowired
	UUserRoleMapper uUserRoleMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return 0;
	}

	@Override
	public UUser insert(UUser record) {
		return null;
	}

	@Override
	public UUser insertSelective(UUser record) {
		return null;
	}

	@Override
	public UUser selectByPrimaryKey(Long id) {
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(UUser record) {
		return 0;
	}

	@Override
	public int updateByPrimaryKey(UUser record) {
		return 0;
	}

	@Override
	public UUser login(String email, String pswd) {
		return null;
	}

	@Override
	public UUser findUserByEmail(String email) {
		return null;
	}

	@Override
	public Pagination<UUser> findByPage(Map<String, Object> resultMap, Integer pageNo, Integer pageSize) {
		return null;
	}

	@Override
	public Map<String, Object> deleteUserById(String ids) {
		return null;
	}

	@Override
	public Map<String, Object> updateForbidUserById(Long id, Long status) {
		return null;
	}

	@Override
	public Pagination<UserRoleAllocationBo> findUserAndRole(ModelMap modelMap, Integer pageNo, Integer pageSize) {
		return null;
	}

	@Override
	public List<URoleBo> selectRoleByUserId(Long id) {
		return null;
	}

	@Override
	public Map<String, Object> addRole2User(Long userId, String ids) {
		return null;
	}

	@Override
	public Map<String, Object> deleteRoleByUserIds(String userIds) {
		return null;
	}

}
