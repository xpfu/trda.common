package com.trda.core.shiro.cache;
/** 
* @company trda
* @author xp.fu
* @version 2017年7月13日 上午10:51:50
* Redis manager工具
*/

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.session.Session;

import com.trda.common.utils.LoggerUtils;
import com.trda.common.utils.SerializeUtil;
import com.trda.common.utils.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class JedisManager {

	private JedisPool jedisPool;

	public JedisPool getJedisPool() {
		return jedisPool;
	}
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
	/**
	 * 获得jedis服务
	 * @return
	 */
	public Jedis getJedis(){
		Jedis jedis = null;
		try {
			jedis = getJedisPool().getResource();
		} catch (JedisConnectionException e) {
			String message = StringUtils.trim(e.getMessage());
			if("Could not get a resource from the pool".equalsIgnoreCase(message)){
				System.out.println("++++++++++请检查你的redis服务++++++++");
				System.out.println("项目退出中...");
				//停止项目
				System.exit(0);
				throw new JedisConnectionException(e);
			}
		}
		return jedis;
	}
	
	public byte[] getValueByKey(int dbIndex, byte[] key) throws Exception {
        Jedis jedis = null;
        byte[] result = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            result = jedis.get(key);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
        return result;
    }
	
	public void returnResource(Jedis jedis, boolean isBroken) {
        if (jedis == null){
        	return;        	
        }
        jedis.close();
    }
	
	public void deleteByKey(int dbIndex, byte[] key) throws Exception {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            Long result = jedis.del(key);
            LoggerUtils.fmtDebug(getClass(), "删除Session结果：%s" , result);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
    }
	
	public void saveValueByKey(int dbIndex, byte[] key, byte[] value, int expireTime)
            throws Exception {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            jedis.set(key, value);
            if (expireTime > 0)
                jedis.expire(key, expireTime);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
    }
	
	/**
	 * 获取所有的session
	 * @param dbIndex
	 * @param redisShiroSession
	 * @return
	 * @throws Exception
	 */
	public Collection<Session> AllSession(int dbIndex, String redisShiroSession) throws Exception {
		Jedis jedis = null;
        boolean isBroken = false;
        Set<Session> sessions = new HashSet<Session>();
		try {
            jedis = getJedis();
            jedis.select(dbIndex);
            
            Set<byte[]> byteKeys = jedis.keys((JedisShiroSessionRepository.REDIS_SHIRO_ALL).getBytes());  
            if (byteKeys != null && byteKeys.size() > 0) {  
                for (byte[] bs : byteKeys) {  
                	Session obj = SerializeUtil.deserialize(jedis.get(bs),  
                    		 Session.class);  
                     if(obj instanceof Session){
                    	 sessions.add(obj);  
                     }
                }  
            }  
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
        return sessions;
	}
	
}
