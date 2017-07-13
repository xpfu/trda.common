package com.trda.core.mybatis.page;

/** 
* @company trda
* @author xp.fu
* @version 2017年6月28日 上午11:07:04
* mysql分页查询帮助类
*/
public class MysqlDialect implements Dialect {
	
	protected static final String SQL_END_DEIMITER = ";";
	@Override
	public boolean supprotsLimit() {
		return true;
	}

	@Override
	public String getLimitSqlString(String sql, int offset, int limit) {
		sql = sql.trim();
		boolean isForUpdate = false;
		if(sql.toLowerCase().endsWith(" for update")){
			sql = sql.substring(0, sql.length() - 11);
			isForUpdate = true;
		}
		if(offset < 0){
			offset = 0;
		}
		
		StringBuffer pagingSelect = new StringBuffer();
		pagingSelect.append(sql + " limit " + offset + "," + limit);
		
		if(isForUpdate){
			pagingSelect.append(" for update");
		}
		
		return pagingSelect.toString();
	}

	@Override
	public String getCountSqlString(String sql) {
		sql = trim(sql);
		
		StringBuffer sb = new StringBuffer(sql.length() + 10);
		sb.append("SELECT COUNT(1) AS " + RS_COLUMN + " FROM ( ");
		sb.append(sql);
		sb.append(") a");
		
		return sb.toString();
	}
	
	private String trim(String sql){
		sql = sql.trim();
		if(sql.endsWith(SQL_END_DEIMITER)){
			sql = sql.substring(0, sql.length() - 1 - SQL_END_DEIMITER.length());
		}
		
		return sql;
	}

}
