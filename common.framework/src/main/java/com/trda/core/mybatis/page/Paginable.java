package com.trda.core.mybatis.page;
/** 
* @company trda
* @author xp.fu
* @version 2017年6月27日 下午3:53:56
* 
* 分页实体接口
*/
public interface Paginable {

	//获得总记录数
	public int getTotalCount();
	
	//获得总页数
	public int getTotalPage();
	
	//每页记录数
	public int getPageSize();
	
	//当前页号
	public int getPageNo();
	
	//是否是第一页
	public boolean isFirstPage();
	
	//是否是最后一页
	public boolean isLastPage();
	
	//返回下一页的页号
	public int getNextPage();
	
	//返回上一页的页号
	public int getPrePage();
}
