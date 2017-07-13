package com.trda.core.config;
/** 
* @company trda
* @author xp.fu
* @version 2017年7月13日 上午9:15:43
* 有序读取INI配置文件
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class INI4j {

	//用linkedHashMap来保证有序的读取数据
	public final LinkedHashMap<String, LinkedHashMap<String, String>> coreMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
	
	//当前section的引用
	public String currentSection = null;
	
	/**
	 * 读取文件
	 * @param file
	 * @throws FileNotFoundException 
	 */
	public INI4j(File file) throws FileNotFoundException{
		this.init(new BufferedReader(new FileReader(file)));
	}
	
	//初始化文件
	private void init(BufferedReader bufferedReader){
		try {
			read(bufferedReader);
		} catch (IOException e) {
			e.printStackTrace();
    		throw new RuntimeException("IO Exception:" + e);
		}
	}
	//读取文件
	private void read(BufferedReader bufferedReader) throws IOException{
		String line = null;
		while(null != (line = bufferedReader.readLine())){
			parseLine(line);
		}
	}
	//转换
	private void parseLine(String line){
		line = line.trim();
		
		//是注释则返回
		if(line.matches("^\\#.*$")){
			return;
		}else if(line.matches("^\\[\\S+\\]$")){
			//section
			String section = line.replaceFirst("^\\[(\\S+)\\]$","$1");
			addSection(section);
		}else if(line.matches("^\\S+=.*$")){
			//key , value
			int i = line.indexOf("=");
			String key = line.substring(0, i).toString();
			String value = line.substring(i + 1).toString();
			addKeyValue(currentSection,key,value);
		}
	}
	//增加新的key和value
	private void addKeyValue(String tempSection,String key,String value){
		if(!coreMap.containsKey(tempSection)){
			return;
		}
		Map<String,String> childMap = coreMap.get(tempSection);
		childMap.put(key, value);
	}
	//增加新的section
	private void addSection(String section){
		if(!coreMap.containsKey(section)){
			currentSection = section;
			LinkedHashMap<String, String> childMap = new LinkedHashMap<String,String>();
			coreMap.put(section, childMap);
		}
	}
	
	/**
	 * 获取配置文件指定section和指定子健的值
	 * @param section
	 * @param key
	 * @return
	 */
	public String get(String section,String key){
		if(coreMap.containsKey(section)){
			return get(section).containsKey(key)?get(section).get(key):null;
		}
		return null;
	}
	
	/**
	 * 获取配置文件指定section的子健和值
	 * @param section
	 * @return
	 */
	public Map<String,String> get(String section){
		return coreMap.containsKey(section)?coreMap.get(section):null;
	}
	
	/**
	 * 获取配置文件的节点和值
	 * @return
	 */
	public LinkedHashMap<String, LinkedHashMap<String, String>> get(){
		return coreMap;
	}
 	
}