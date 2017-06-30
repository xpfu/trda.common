package com.trda.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.ui.ModelMap;

import com.trda.common.model.URoleBo;
import com.trda.common.model.UUser;
import com.trda.core.mybatis.page.Pagination;
import com.trda.permission.bo.UserRoleAllocationBo;

/**
 * @company trda
 * @author xp.fu
 * @version 2017年6月28日 下午3:36:01
 */
public interface UUserService {

	int deleteByPrimaryKey(Long id);

	UUser insert(UUser record);

	UUser insertSelective(UUser record);

	UUser selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(UUser record);

	int updateByPrimaryKey(UUser record);

	UUser login(String email, String pswd);

	UUser findUserByEmail(String email);

	Pagination<UUser> findByPage(Map<String, Object> resultMap, Integer pageNo, Integer pageSize);

	Map<String, Object> deleteUserById(String ids);

	Map<String, Object> updateForbidUserById(Long id, Long status);

	Pagination<UserRoleAllocationBo> findUserAndRole(ModelMap modelMap, Integer pageNo, Integer pageSize);

	List<URoleBo> selectRoleByUserId(Long id);

	Map<String, Object> addRole2User(Long userId, String ids);

	Map<String, Object> deleteRoleByUserIds(String userIds);
}
