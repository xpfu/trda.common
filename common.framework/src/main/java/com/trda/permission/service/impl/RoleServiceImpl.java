package com.trda.permission.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trda.common.dao.URoleMapper;
import com.trda.common.model.URole;
import com.trda.common.utils.LoggerUtils;
import com.trda.core.mybatis.BaseMybatisDao;
import com.trda.core.mybatis.page.Pagination;
import com.trda.permission.bo.RolePermissionAllocationBo;
import com.trda.permission.service.RoleService;

/** 
* @company trda
* @author xp.fu
* @version 2017年6月28日 上午9:59:34
*/
@Service
public class RoleServiceImpl extends BaseMybatisDao<URoleMapper> implements RoleService {

	@Autowired
	URoleMapper uRoleMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return uRoleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(URole record) {
		return uRoleMapper.insert(record);
	}

	@Override
	public int intsertSelective(URole record) {
		return uRoleMapper.insertSelective(record);
	}

	@Override
	public URole selectByPrimaryKey(Long id) {
		return uRoleMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimarykeySelective(URole record) {
		return uRoleMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(URole record) {
		return uRoleMapper.updateByPrimaryKey(record);
	}

	@Override
	public Pagination<URole> findPage(Map<String, Object> resultMap, Integer pageNo, Integer pageSize) {
		return super.findPage(resultMap, pageNo, pageSize);
	}

	@Override
	public Map<String, Object> deleteRoleById(String ids) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			int count = 0;
			String resultMsg = "刪除成功";
			String[] idArray = new String[]{};
			
			if(StringUtils.contains(ids, ",")){
				idArray = ids.split(",");
			}else{
				idArray = new String[]{ids};
			}
			
			for(String idx : idArray){
				Long id = new Long(idx);
				if(new Long(1).equals(id)){
					resultMsg = "操作成功,But '系统管理员不能删除。'";
					continue;
				}else{
					count += this.deleteByPrimaryKey(id);
				}
			}
			resultMap.put("status", 200);
			resultMap.put("count", count);
			resultMap.put("resultMsg", resultMsg);
			
		} catch (Exception e) {
			LoggerUtils.fmtError(getClass(), e, "根据IDS删除用户出现错误,ids[%s]",ids);
			resultMap.put("status", 500);
			resultMap.put("message", "删除出现错误,请刷新后再试");
		}
		
		return resultMap;
	}

	@Override
	public Pagination<RolePermissionAllocationBo> findRoleAndPermissionPage(Map<String, Object> resultMap,
			Integer pageNo,Integer pageSize) {
		return super.findPage("findRoleAndPermission", "findCount", resultMap, pageNo, pageSize);
	}

	@Override
	public Set<String> findRoleByUserId(Long userId) {
		return uRoleMapper.findRoleByUserId(userId);
	}

	@Override
	public List<URole> findNowAllPermission() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", TokenManager.getUserId());
		return uRoleMapper.findNowAllPermission(map);
	}
	
	//20分钟刷新一次
	@Override
	public void initData() {
		uRoleMapper.initData();
	}

}
