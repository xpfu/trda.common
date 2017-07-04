package com.trda.common.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.trda.common.model.URole;

/**
 * @company trda
 * @author xp.fu
 * @version 2017年6月28日 上午11:26:49
 */
public interface URoleMapper {

	int deleteByPrimaryKey(Long id);

	int insert(URole record);

	int insertSelective(URole record);

	URole selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(URole record);

	int updateByPrimaryKey(URole record);

	Set<String> findRoleByUserId(Long id);

	List<URole> findNowAllPermission(Map<String, Object> map);

	void initData();
}
