package com.trda.common.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/** 
* @company trda
* @author xp.fu
* @version 2017年7月5日 下午4:33:36
* 静态化路径工具包
*/
public class PathUtils {
	
	/**
	 * 获得classes的目录
	 * @return
	 */
	public static String getClassPath(){
		String systemName = System.getProperty("os.name");
		
		//判断当前环境，如果是windows则截取路径的第一个"/"
		if(!StringUtils.isBlank(systemName) && systemName.indexOf("Windows") != -1){
			return PathUtils.class.getResource("/").getFile().toString().substring(1);
		}else{
			return PathUtils.class.getResource("/").getFile().toString();
		}
	}
	
	/**
	 * 获取当前对象的路径
	 * @param object
	 * @return
	 */
	public static String getObjectPath(Object object){
		return object.getClass().getResource(".").getFile().toString();
	}
	
	/**
	 * 获取当前项目的路径
	 * @return
	 */
	public static String getProjectPath(){
		return System.getProperty("user.dir");
	}
	
	/**
	 * 获取root路径
	 */
	public static String getRootPath(){
		return getWEB_INF().replace("WEB-INF/", "");
	}

	/**
	 * 获取web-inf目录
	 * @return
	 */
	public static String getWEB_INF() {
		return getClassPath().replace("classes/", "");
	}
	
	/**
	 * 获取输出的HTML目录
	 * @return
	 */
	public static String getHTMLPath(){
		return getFreePath() + "html/html/";
	}
	
	/**
	 * 获取模板文件夹目录
	 * @return
	 */
	public static String getFreePath() {
		return getWEB_INF() + "ftl/";
	}
	
	/**
	 * 获取输出FTL目录
	 * @return
	 */
	public static String getFTLPath(){
		return getFreePath() + "html/ftl/";
	}
	
	/**
	 * 获取一个目录下所有的文件
	 * @param path
	 * @return
	 */
	public static File[] getFiles(String path){
		File file = new File(path);
		File[] files = file.listFiles();
		return files;
	}
	
	/**
	 * 获取当前时间 + 中国时区
	 * @return
	 */
	public static String getDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String result = sdf.format(new Date());
		result = result.replace("_", "T");
		result += "+08:00";
		return result;
	}
	
	/**
	 * 不带结尾的XmlSitemap头部
	 * @return
	 */
	public static String getXmlSiteMap(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + nextLine())
		.append("<?xml-stylesheet type=\"text/xsl\" href=\"sitemap.xsl\"?>"+ nextLine())
		.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">"+ nextLine());
		
		return stringBuffer.toString();
	}
	
	/**
	 * 文本换行
	 * @return
	 */
	private static String nextLine() {
		String nextLine = System.getProperty("line.separator");
		return nextLine;
	}
	
}
