package com.trda.permission.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.trda.common.model.URole;
import com.trda.core.mybatis.page.Pagination;
import com.trda.permission.bo.RolePermissionAllocationBo;


/**
 * @company trda
 * @author xp.fu
 * @version 2017年6月27日 下午3:28:03
 */
public interface RoleService {

	int deleteByPrimaryKey(Long id);

	int insert(URole record);

	int intsertSelective(URole record);

	URole selectByPrimaryKey(Long id);

	int updateByPrimarykeySelective(URole record);

	int updateByPrimaryKey(URole record);

	Pagination<URole> findPage(Map<String, Object> resultMap, Integer pageNo, Integer pageSize);

	Map<String, Object> deleteRoleById(String ids);

	Pagination<RolePermissionAllocationBo> findRoleAndPermissionPage(Map<String, Object> resultMap,Integer pageNo, Integer pageSize);

	// 根据用户ID查询角色(role),放入到Authorization中
	Set<String> findRoleByUserId(Long userId);

	List<URole> findNowAllPermission();

	// 初始化数据
	void initData();
}
