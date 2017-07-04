package com.trda.permission.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.trda.common.dao.UPermissionMapper;
import com.trda.common.dao.URolePermissionMapper;
import com.trda.common.dao.UUserMapper;
import com.trda.common.dao.UUserRoleMapper;
import com.trda.common.model.UPermission;
import com.trda.common.model.URolePermission;
import com.trda.common.utils.LoggerUtils;
import com.trda.common.utils.StringUtils;
import com.trda.core.mybatis.BaseMybatisDao;
import com.trda.core.mybatis.page.Pagination;
import com.trda.core.shiro.token.manager.TokenManager;
import com.trda.permission.bo.UPermissionBo;
import com.trda.permission.service.PermissionService;

/** 
* @company trda
* @author xp.fu
* @version 2017年7月3日 上午10:02:49
*/
public class PermissionServiceImpl extends BaseMybatisDao<UPermissionMapper> implements PermissionService {

	@Autowired
	UPermissionMapper uPermissionMapper;
	@Autowired
	UUserMapper uUserMapper;
	@Autowired
	URolePermissionMapper uRolePermissionMapper;
	@Autowired
	UUserRoleMapper uUserRoleMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return uPermissionMapper.deleteByPrimaryKey(id);
	}

	@Override
	public UPermission insert(UPermission record) {
		uPermissionMapper.insert(record);

		return record;
	}

	@Override
	public UPermission insertSelective(UPermission record) {
		//添加权限
		uPermissionMapper.insertSelective(record);
		//每添加一个权限都往【系统管理员：888888】中添加一次，保证系统管理员有最大的权限
		executePermission(new Long(1),String.valueOf(record.getId()));
		return record;
	}

	@Override
	public UPermission selectByPrimaryKey(Long id) {
		return uPermissionMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(UPermission record) {
		return uPermissionMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(UPermission record) {
		return uPermissionMapper.updateByPrimaryKey(record);
	}

	@Override
	public Map<String, Object> deletePermissionById(String ids) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		try {
			int successCount = 0;
			int errorCount = 0;
			String resultMsg = "删除%s条,失败%s条";
			String[] idArray = new String[]{};
			if(StringUtils.contains(ids, ",")){
				idArray = ids.split(",");
			}else{
				idArray = new String[]{ids};
			}
			for(String tempId : idArray){
				Long id = new Long(tempId);
				
				List<URolePermission> rolePermissions = uRolePermissionMapper.findRolePermissionByPid(id);
				if(null != rolePermissions && rolePermissions.size() > 0){
					errorCount += rolePermissions.size();
				}else{
					successCount += this.deleteByPrimaryKey(id);
				}
			}
			resultMap.put("status", 200);
			//有成功也有失败的记录，详细提示
			if(errorCount > 0){
				resultMsg = String.format(resultMsg, successCount,errorCount);
			}else{
				resultMsg = "操作成功";
			}
			resultMap.put("message", resultMsg);
		} catch (Exception e) {
			LoggerUtils.fmtError(getClass(), e, "根据IDS删除用户出错,IDS[%s]",ids);
			resultMap.put("status", 500);
			resultMap.put("message", "删除出现错误,请刷新后再试");
		}
		return resultMap;
	}

	@Override
	public Pagination<UPermission> findPage(Map<String, Object> resultMap, Integer pageNo, Integer pageSize) {
		return super.findPage(resultMap, pageNo, pageSize);
	}

	@Override
	public List<UPermissionBo> selectPermissionById(Long id) {
		return uPermissionMapper.selectPermissionById(id);
	}

	@Override
	public Map<String, Object> addPermission2Role(Long roleId, String ids) {
		//先删除原有的数据
		uRolePermissionMapper.deleteByRid(roleId);
		return executePermission(roleId,ids);
	}

	@Override
	public Map<String, Object> deleteByRids(String roleIds) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			resultMap.put("roleIds", roleIds);
			uRolePermissionMapper.deleteByRids(resultMap);
			resultMap.put("status", 200);
			resultMap.put("message", "操作成功");
		} catch (Exception e) {
			resultMap.put("status", 200);
			resultMap.put("message", "操作失败,请重试");
		}
		return resultMap;
	}

	@Override
	public Set<String> findPermissionByUserId(Long userId) {
		return uPermissionMapper.findPermissionByUserId(userId);
	}
	
	//处理权限
	private Map<String,Object> executePermission(Long roleId,String ids){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		int count = 0;
		try {
			//如果ids，permission的id有值就添加，没有值就象征着把这个角色(roleId)所有权限取消
			if(StringUtils.isNotBlank(ids));
			String [] idArray = null;
			
			if(StringUtils.contains(ids,",")){
				idArray = ids.split(",");
			}else{
				idArray = new String[]{ids};
			}
			
			//添加新的
			for(String tempPId : idArray){
				if(StringUtils.isNotBlank(tempPId)){
					URolePermission entity = new URolePermission(roleId,new Long(tempPId));
					count += uRolePermissionMapper.insertSelective(entity);
				}
			}
			resultMap.put("status", 200);
			resultMap.put("message", "操作成功");
			
		} catch (Exception e) {
			resultMap.put("status", 200);
			resultMap.put("message", "操作失败");
		}
		
		//清空拥有角色ID为roleID的用户权限已加载数，让权限数据重新加载
		List<Long> userIds = uUserRoleMapper.findUserIdByRoleId(roleId);
		
		TokenManager.clearUserAuthByUserId(userIds);
		resultMap.put("count", count);
		return resultMap;
	}

}
