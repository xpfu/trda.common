package com.trda.core.mybatis;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.trda.common.utils.LoggerUtils;
import com.trda.core.mybatis.page.MysqlDialect;
import com.trda.core.mybatis.page.Pagination;

/** 
* @company trda
* @author xp.fu
* @version 2017年6月28日 上午10:06:17
*/
public class BaseMybatisDao<T> extends SqlSessionDaoSupport {
	
	private String NAMESPACE;
	final static Class<? extends Object> SELF = BaseMybatisDao.class;
	//日志
	protected final Log logger = LogFactory.getLog(BaseMybatisDao.class);
	//默认的查询sql id
	final static String DEFAULT_SQL_ID = "findAll";
	//默认的查询count sql id
	final static String DEFAULT_COUNT_SQL_ID = "findCount";
	
	public BaseMybatisDao(){
		try {
			Object genericClz = getClass().getGenericSuperclass();
			if(genericClz instanceof ParameterizedType){
				Class<T> entityClass = (Class<T>)((ParameterizedType)genericClz)
						.getActualTypeArguments()[0];
				NAMESPACE = entityClass.getPackage().getName() + "."
						+ entityClass.getSimpleName();
			}
		} catch (RuntimeException e) {
			LoggerUtils.error(SELF,"初始化数据失败,继承BaseMybatisDao没有泛型!");
		}
	}
	
	/**
	 * 根据Sql id查询分页对象
	 * @param sqlId    对应mapper.xml中的sql id
	 * @param params   参数<String,Object>
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination findByPageBySqlId(String sqlId,Map<String,Object>params,Integer pageNo,Integer pageSize){
		
		pageNo = (null == pageNo ? 1 : pageNo);
		pageSize = (null == pageSize? 10 : pageSize);
		
		sqlId = String.format("%s.%s", NAMESPACE,sqlId);
		
		Pagination page = new Pagination();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		
		Configuration configuration = this.getSqlSession().getConfiguration();
		int offset = (page.getPageNo() - 1) * page.getPageSize();
		String page_sql = String.format(" limit %s,%s", offset,pageSize);
		params.put("page_sql", page_sql);
		
		BoundSql boundSql = configuration.getMappedStatement(sqlId).getBoundSql(params);
		String sqlCode = boundSql.getSql();
		
		LoggerUtils.fmtDebug(SELF, "findByPageBySqlId sql : %s", sqlCode);
		String countCode = "";
		String countId = "";
		BoundSql countSql = null;
		
		//sql id 和count id用同一个
		if(StringUtils.isBlank(sqlId)){
			countCode = sqlCode;
			countSql = boundSql;
		}else{
			countId = sqlId;
			
			Map<String,Object> countMap = new HashMap<String,Object>();
			countMap.putAll(params);
			//去掉分页的参数
			countMap.remove("page_sql");
			countSql = configuration.getMappedStatement(countId).getBoundSql(countMap);
			countCode = countSql.getSql();
		}
		
		try {
			//获取链接
			Connection conn = this.getSqlSession().getConnection();
			List<?> resultList = this.getSqlSession().selectList(sqlId,params);
			page.setList(resultList);
			PreparedStatement ps = getPreparedStatement(countCode,countSql.getParameterMappings(),params,conn);
			ps.execute();
			ResultSet set = ps.getResultSet();
			
			while(set.next()){
				page.setTotalCount(set.getInt(1));
			}
		} catch (Exception e) {
			LoggerUtils.error(SELF, "jdbc.error.code.findByPageBySqlId",e);
		}
		
		return page;
	}
	
	/**
	 * 根据sql Id查询list，不要查询分页的页码
	 * @param sqlId
	 * @param params
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List findList(String sqlId,Map<String,Object> params,Integer pageNo,Integer pageSize){
		pageNo = (null == pageNo ? 1 : pageNo);
		pageSize = (null == pageSize ? 10 : pageSize);
		
		int offset = (pageNo - 1)*pageSize;
		String page_sql = String.format(" limit %s,%s", offset,pageSize);
		params.put("page_sql", page_sql);
		sqlId = String.format("%s.%s", NAMESPACE,sqlId);
		
		List resultList = this.getSqlSession().selectList(sqlId,params);
		
		return resultList;
	}
	
	/**
	 * 默认的findAll的查询
	 * @param params
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public List findList(Map<String,Object> params,Integer pageNo,Integer pageSize){
		return findList(DEFAULT_COUNT_SQL_ID,params,pageNo,pageSize);
	}
	
	public Pagination findPage(String sqlId,String countId,Map<String,Object> params,Integer pageNo,Integer pageSize){
		
		pageNo = (null == pageNo ? 1 : pageNo);
		pageSize = (null == pageSize ? 10 : pageSize);
		
		Pagination page = new Pagination<>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		
		Configuration configuration = this.getSqlSession().getConfiguration();
		int offset = (page.getPageNo() - 1 ) * page.getPageSize();
		String page_sql = String.format(" limit %s,%s ", offset,pageSize);
		params.put("page_sql", page_sql);
		
		sqlId = String.format("%s.%s", NAMESPACE,sqlId);
		
		BoundSql boundSql = configuration.getMappedStatement(sqlId).getBoundSql(params);
		String sqlCode = boundSql.getSql();
		LoggerUtils.fmtDebug(SELF, "findPage sql : %s ", sqlCode);
		String countCode = "";
		BoundSql countSql = null;
		if(StringUtils.isBlank(countId)){
			countCode = sqlCode;
			countSql = boundSql;
		} else {
			countId = String.format("%s.%s", NAMESPACE,countId);
			countSql = configuration.getMappedStatement(countId).getBoundSql(params);
			countCode = countSql.getSql();
		}
		
		try {
			Connection conn = this.getSqlSession().getConnection();
			List resultList = this.getSqlSession().selectList(sqlId,params);
			
			page.setList(resultList);
			
			PreparedStatement ps = getPreparedStatement4Count(countCode, countSql.getParameterMappings(), params, conn);
			ps.execute();
			
			ResultSet set = ps.getResultSet();
			while(set.next()){
				page.setTotalCount(set.getInt(1));
			}
		} catch (Exception e) {
			LoggerUtils.error(SELF, "jdbc.error.code.findByPageBySqlId");
		}
		return page;
	}
	
	/**
	 * 默认查询数据
	 * @param params
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination findPage(Map<String,Object> params,Integer pageNo,Integer pageSize){
		return findPage(DEFAULT_SQL_ID,DEFAULT_COUNT_SQL_ID,params,pageNo,pageSize);
	}
	
	/**
	 * 组装获得执行sql的语句
	 * @param sql
	 * @param parameterMappingList
	 * @param params
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement getPreparedStatement(String sql,List<ParameterMapping> parameterMappingList,Map<String,Object> params,Connection conn) throws SQLException{
		//根据数据库分页
		MysqlDialect mysqlDialect = new MysqlDialect();
		
		PreparedStatement ps = conn.prepareStatement(mysqlDialect.getCountSqlString(sql));
		int index = 1;
		for(int i = 0;i< parameterMappingList.size();i++){
			ps.setObject(index++, params.get(parameterMappingList.get(i).getProperty()));
		}
		return ps;
	}
	
	/**
	 * 分页查询Count 用自定义的sql语句查询count
	 * @param sql
	 * @param parameterMappingList
	 * @param params
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement getPreparedStatement4Count(String sql,List<ParameterMapping> parameterMappingList,Map<String,Object> params,Connection conn) throws SQLException{
		
		PreparedStatement ps = conn.prepareStatement(StringUtils.trim(sql));
		int index = 1;
		for(int i = 0;i < parameterMappingList.size(); i++){
			ps.setObject(index++, params.get(parameterMappingList.get(i).getProperty()));
		}
		return ps;
	}

}
