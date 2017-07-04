package com.trda.common.dao;

import java.util.List;
import java.util.Map;

import com.trda.common.model.URoleBo;
import com.trda.common.model.UUser;

public interface UUserMapper {

	int deleteByPrimaryKey(Long id);

	int insert(UUser record);

	int insertSelective(UUser record);

	UUser selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(UUser record);

	int updateByPrimaryKey(UUser record);

	UUser login(Map<String, Object> map);

	UUser findUserByEmail(String email);

	List<URoleBo> selectRoleByUserId(Long id);

}