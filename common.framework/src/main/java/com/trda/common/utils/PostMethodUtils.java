package com.trda.common.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

/**
 * @company trda
 * @author xp.fu
 * @version 2017年7月12日 下午2:43:28 post请求封装类
 */
public class PostMethodUtils extends PostMethod {

	public final static String JSONObject = "JSONObject";
	public final static String JSONArray = "JSONArray";

	// 自定义请求头信息
	private Map<String, String> head = new LinkedHashMap<String, String>();
	// 默认超时时间
	private int timeOut = 60000;

	/**
	 * 直接返回对应的类型
	 * 
	 * @param requiredType
	 * @return
	 */
	public <T> T executeMethod(Class<T> requiredType) {
		if (null == requiredType) {
			return null;
		}
		if ("net.sf.json.JSONArray".equals(requiredType.getCanonicalName())) {
			return (T) executeMethod(JSONArray);
		}
		if ("net.sf.json.JSONObject".equals(requiredType.getCanonicalName())) {
			return (T) executeMethod(JSONObject);
		} else {
			return (T) executeMethod();
		}
	}

	/**
	 * 执行方法,type可以不传,默认返回String
	 * @param type
	 * @return
	 */
	public Object executeMethod(String... type) {
		try {
			HttpClient client = new HttpClient();
			// 取得当前系统域名,方便被请求方统计来源
			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			client.getParams().setParameter("Connection", "close");

			// 请求超时时间，如果需要更改，可以修改setTimeOut参数

			// 加入额外的请求头信息
			for (String key : head.keySet()) {
				if (StringUtils.isNotBlank(key, head.get(key))) {
					this.setRequestHeader(key, head.get(key));
				}
			}

			client.getHttpConnectionManager().getParams().setConnectionTimeout(getTimeOut());
			int status = 0;
			String result = null;
			String url = getPath();
			try {
				status = client.executeMethod(this);
			} catch (Exception e) {
				LoggerUtils.error(getClass(), "请求[" + url + "]超时或错误,message : " + e.getMessage(), e);
				RuntimeException ve = new RuntimeException("请求[" + url + "]超时或错误,message : " + e.getMessage(), e);
				throw ve;
			}
			
			if(status == HttpStatus.SC_OK){
				try {
					result = this.getResponseBodyAsString();
				} catch (IOException e) {
					LoggerUtils.error(getClass(),
							"Abnormal request returns! Exception information as follows: "
									+ e.getMessage(), e);
					RuntimeException ve = new RuntimeException( "请求[" + url + "]超时或错误,message : "
							+ e.getMessage(),e);
					throw ve;
				}
			}else{
				LoggerUtils.error(getClass(), "HTTP请求错误，请求地址为：[" + url + "],状态为：["
						+ status + "]");
				throw new RuntimeException( "HTTP请求错误，请求地址为：[" + url
						+ "],状态为：[" + status + "]");
			}
			
			if (StringUtils.indexOf(JSONObject, type) > 0) {
				return net.sf.json.JSONObject.fromObject(result);
			}
	
			if (StringUtils.indexOf(JSONArray, type) > 0) {
				return net.sf.json.JSONArray.fromObject(result);
			}
			
			return result;
			
		} finally {
			this.releaseConnection();
		}
	}
	
	/**
	 * 设置参数List
	 * @param parameters
	 */
	public void setParameter(List<Map<String,Object>> parameters){
		for(Map<String,Object> map : parameters){
			setParameter(map);
		}
	}
	
	/**
	 * 设置参数JSONObject
	 * @param parameters
	 */
	public void setJSONParameter(JSONObject parameters){
		for(Iterator<?> iterator = parameters.keys();iterator.hasNext();){
			String key = (String)iterator.next();
			Object str = parameters.get(key);
			String value = (null==str?"":StringUtils.trimToEmpty(str.toString()));
			this.addParameter(key,value);
		}
	}
	
	/**
	 * 设置参数JSONArray
	 * @param parameters
	 */
	public void setJSONArrayParameter(JSONArray parameters){
		for(Object object : parameters){
			JSONObject jsonArray = (JSONObject)object;
			this.setJSONParameter(jsonArray);
		}
	}
	
	/**
	 * 设置参数
	 * @param parameters
	 */
	public void setParameter(Map<String,Object> parameters){
		for(String key : parameters.keySet()){
			Object str = parameters.get(key);
			String value = null == str ? "" : StringUtils.trimToEmpty(str.toString());
			this.addParameter(key, value);
		}
	}
	
	public PostMethodUtils(){
		super();
	}
	public PostMethodUtils(String url){
		super(url);
	}
	public PostMethodUtils(List<Map<String,Object>> parameters){
		super();
		this.setParameter(parameters);
	}
	public PostMethodUtils(String url,List<Map<String,Object>> parameters){
		super(url);
		this.setParameter(parameters);
	}
	public PostMethodUtils(JSONObject parameter){
		super();
		this.setJSONParameter(parameter);
	}
	public PostMethodUtils(Map<String,Object> parameter){
		super();
		this.setParameter(parameter);
	}
	public PostMethodUtils(String url,Map<String,Object> parameter){
		super(url);
		this.setParameter(parameter);
	}
	public PostMethodUtils(String url,JSONObject parameter){
		super(url);
		this.setJSONParameter(parameter);
	}
	public PostMethodUtils(JSONArray parameter){
		super();
		this.setJSONArrayParameter(parameter);
	}
	public PostMethodUtils(String url,JSONArray parameter){
		super(url);
		this.setJSONArrayParameter(parameter);
	}
	
	public Map<String,String> getHead(){
		return head;
	}
	public void setHead(Map<String,String> head){
		this.head = head;
	}
	public int getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}
	
	public JsonConfig getConfig(){
		JsonConfig jsonConfig = new JsonConfig();
		//实现属性过滤器接口并重写apply()方法
		PropertyFilter pf = new PropertyFilter() {
			//返回true则跳过此属性,返回false则正常转换
			@Override
			public boolean apply(Object source, String name, Object value) {
				if(StringUtils.isBlank(value)){
					return true;
				}
				return false;
			}
		};
		//将过滤器放入json-config中
		jsonConfig.setJsonPropertyFilter(pf);
		
		return jsonConfig;
	}

}
