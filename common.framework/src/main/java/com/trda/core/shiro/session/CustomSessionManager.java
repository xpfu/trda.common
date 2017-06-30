package com.trda.core.shiro.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.print.attribute.standard.RequestingUserName;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;

import com.trda.common.model.UUser;
import com.trda.common.utils.LoggerUtils;
import com.trda.common.utils.StringUtils;
import com.trda.core.shiro.dao.CustomShiroSessionDAO;
import com.trda.user.bo.UserOnlineBo;

/** 
* @company trda
* @author xp.fu
* @version 2017年6月28日 下午5:26:49
* 用户session 手动管理
*/
public class CustomSessionManager {

	//session status
	public static final String SESSION_STATUS = "trda-online-status";
	ShiroSessionRepository shiroSessionRepository;
	
	CustomShiroSessionDAO customShiroSessionDAO;
	
	/**
	 * 获取所有的有效Session用户
	 * @return
	 */
	public List<UserOnlineBo> getAllUser(){
		//获取所有的session
		Collection<Session> sessions = customShiroSessionDAO.getActiveSessions();
		List<UserOnlineBo> list = new ArrayList<UserOnlineBo>();
		
		for(Session session : sessions){
			UserOnlineBo bo = getSessionBo(session);
			if(null != bo){
				list.add(bo);
			}
		}
		return list;
	}
	
	/**
	 * 根据用户ID查询用户信息 SimplePrincipalCollection
	 * @param userIds
	 * @return
	 */
	public List<SimplePrincipalCollection> getSimplePrincipalCollectionByUserId(Long...userIds){
		//把userIds 转成set便于判断
		Set<Long> idSet = (Set<Long>)StringUtils.array2Set(userIds);
		//获取所有的session
		Collection<Session> sessions = customShiroSessionDAO.getActiveSessions();
		
		//定义返回的结果
		List<SimplePrincipalCollection> resultList = new ArrayList<SimplePrincipalCollection>();
		for(Session tempSession : sessions){
			//获取SimplePrincipalCollection
			Object obj = tempSession.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
			if(null != obj && obj instanceof SimplePrincipalCollection){
				//类型转换
				SimplePrincipalCollection spc = (SimplePrincipalCollection)obj;
				//判断用户匹配用户ID
				obj = spc.getPrimaryPrincipal();
				if(null != obj && obj instanceof UUser){
					UUser user = (UUser)obj;
					//比较用户ID，符合条件加入集合
					if(null != user && idSet.contains(user.getId())){
						resultList.add(spc);
					}
				}
			}
		}
		return resultList;
	}
	
	/**
	 * 根据用户sessionId获得耽搁用户信息
	 * @param sessionId
	 * @return
	 */
	public UserOnlineBo getSession(String sessionId){
		Session session = shiroSessionRepository.getSession(sessionId);
		UserOnlineBo userOnlineBo = getSessionBo(session);
		
		return userOnlineBo;
	}
	
	
	private UserOnlineBo getSessionBo(Session session){
		//获取session登录信息
		Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
		if(null == obj){
			return null;
		}
		
		//确保是SimplePrincipalCollection对象
		if(obj instanceof SimplePrincipalCollection){
			SimplePrincipalCollection spc = (SimplePrincipalCollection)obj;
			//获取用户得了的信息
			obj = spc.getPrimaryPrincipal();
			if(null != obj && obj instanceof UUser){
				//存储session 和 user的信息
				UserOnlineBo userBo = new UserOnlineBo((UUser)obj);
				//最后一次和用户交互的时间
				userBo.setLastAccess(session.getLastAccessTime());
				//主机的ip地址
				userBo.setHost(session.getHost());
				//session id
				userBo.setSessionId(session.getId().toString());
				//session最后一次和系统交互的时间
				userBo.setLastLoginTime(session.getLastAccessTime());
				//会话到期时间 ttl(ms)
				userBo.setTimeOut(session.getTimeout());
				//session 创建时间
				userBo.setStartTime(session.getStartTimestamp());
				//是否踢出
				SessionStatus sessionStatus = (SessionStatus)session.getAttribute(SESSION_STATUS);
				boolean status = Boolean.TRUE;
				if(null != sessionStatus){
					status = sessionStatus.getOnlineStatus();
				}
				userBo.setSessionStatus(status);
			}
		}
		return null;
	}
	
	/**
	 * 改变session的状态
	 * @param status
	 * @param sessionIds
	 * @return
	 */
	public Map<String,Object> changeSessionStatus(Boolean status,String sessionIds){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			String[] sessionIdArray = null;
			if(sessionIds.indexOf(",") == -1){
				sessionIdArray = new String[]{sessionIds};
			}else{
				sessionIdArray = sessionIds.split(",");
			}
			
			for(String id : sessionIdArray){
				Session session = shiroSessionRepository.getSession(id);
				SessionStatus sessionStatus = new SessionStatus();
				sessionStatus.setOnlineStatus(status);
				session.setAttribute(SESSION_STATUS, sessionStatus);
				customShiroSessionDAO.update(session);
			}
			
			resultMap.put("status", 200);
			resultMap.put("sessionStatus", status?1:0);
			resultMap.put("sessionStatusText", status?"踢出":"激活");
			resultMap.put("sessionStatusTextTd", status?"有效":"已踢出");
			
		} catch (Exception e) {
			LoggerUtils.fmtError(getClass(), e, "改变Session状态信息错误,sessionId[%s]",sessionIds);
			resultMap.put("status", 500);
			resultMap.put("message", "改变Session状态信息失败,有可能Session不存在，请刷新再试!");
		}
		
		return resultMap;
	}
	
	/**
	 * 查询要禁用的用户是否在线
	 * @param id
	 * @param status
	 */
	public void forbidUserById(Long id,Long status){
		//获取所有在线用户
		for(UserOnlineBo tempUser : getAllUser()){
			Long userId = tempUser.getId();
			//匹配用户ID
			if(userId.equals(id)){
				//获取用户session
				Session session = shiroSessionRepository.getSession(tempUser.getSessionId());
				//标记用户session状态
				SessionStatus sessionStatus = (SessionStatus)session.getAttribute(SESSION_STATUS);
				//是否踢出 true:有效  false:踢出
				sessionStatus.setOnlineStatus(status.intValue() == 1);
				//更新session信息
				customShiroSessionDAO.update(session);
			}
		}
	}

	public void setShiroSessionRepository(ShiroSessionRepository shiroSessionRepository) {
		this.shiroSessionRepository = shiroSessionRepository;
	}

	public void setCustomShiroSessionDAO(CustomShiroSessionDAO customShiroSessionDAO) {
		this.customShiroSessionDAO = customShiroSessionDAO;
	}

}
