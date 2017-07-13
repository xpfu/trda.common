package com.trda.core.mybatis.page;
/** 
* @company trda
* @author xp.fu
* @version 2017年6月28日 上午11:01:49
* 数据库分页助手
*/
public interface Dialect {
	public static final String RS_COLUMN = "totalCount";
	public boolean supprotsLimit();
	
	/**
	 * 以传入sql为基础组装分页查询的sql语句，传递给myBatis调用
	 * @param sql    原始sql语句
	 * @param offset 分页查询的记录偏移量
	 * @param limit  每页限定记录数
	 * @return       拼装好的sql语句
	 */
	public String getLimitSqlString(String sql,int offset,int limit);
	
	/**
	 * 以传入sql为基础组装总记录数查询的sql语句
	 * @param sql  原始sql语句
	 * @return     拼装好的sql语句
	 */
	public String getCountSqlString(String sql);
}
