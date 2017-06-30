package com.trda.common.utils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @company trda
 * @author xp.fu
 * @version 2017年6月29日 上午10:36:49 String的工具类 对StringUtils的方法进行重写，方便使用
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {

	/**
	 * 判断多个或单个对象是否有空对象
	 * 
	 * @param objects
	 * @return 只要有一个对象元素为空则返回true
	 */
	public static boolean isBlank(Object... objects) {
		Boolean result = false;
		for (Object obj : objects) {
			if (null == obj || "".equals(obj.toString().trim()) || "null".equals(obj.toString().trim())) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * 获取随机数
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandom(int length) {
		String val = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			// 输出字母还是数字的判断
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 字符串
			if ("char".equals(charOrNum)) {
				// 大小写判断
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (choice + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				// 数字
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val.toLowerCase();
	}

	/**
	 * 判断单个或多个对象是否不为空
	 * 
	 * @param objects
	 * @return 只要有一个元素不为空则返回true
	 */
	public static boolean isNotBlank(Object... objects) {
		return !isBlank(objects);
	}

	// 重载字符串对象的判断
	public static boolean isBlank(String... objects) {
		Object[] obj = objects;
		return isBlank(obj);
	}

	public static boolean isNotBlank(String... objects) {
		Object[] obj = objects;
		return isNotBlank(obj);
	}

	public static boolean isBlank(String str) {
		Object obj = str;
		return isBlank(obj);
	}

	public static boolean isNotBlank(String str) {
		Object obj = str;
		return isNotBlank(obj);
	}

	/**
	 * 判断自付字符串在数组中存在的个数
	 * 
	 * @param baseStr
	 * @param strings
	 * @return
	 */
	public static int indexOf(String baseStr, String[] strings) {
		if (null == baseStr || baseStr.length() == 0 || null == strings) {
			return 0;
		}

		int i = 0;
		for (String str : strings) {
			boolean result = baseStr.equals(str);
			i = result ? ++i : i;
		}
		return i;
	}

	/**
	 * 判断传入的字符串是否是JSONObject类型的，是则返回JSONObject，否则返回null
	 * 
	 * @param args
	 * @return
	 */
	public static JSONObject isJSONObject(String args) {
		JSONObject result = null;
		if (isBlank(args)) {
			return result;
		}
		try {
			return JSONObject.fromObject(args.trim());
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
	}

	/**
	 * 判断传入的字符串是否为JSONArray,是则返回JSONArray，否则返回null
	 * 
	 * @param args
	 * @return
	 */
	public static JSONArray isJSONArray(Object args) {
		JSONArray result = new JSONArray();
		if (isBlank(args)) {
			return null;
		}
		if (args instanceof JSONArray) {
			JSONArray arr = (JSONArray) args;
			for (Object json : arr) {
				if (json != null && json instanceof JSONObject) {
					result.add(json);
					continue;
				} else {
					result.add(JSONObject.fromObject(json));
				}
			}
			return result;
		} else {
			return null;
		}
	}

	public static String trimToEmpty(Object str) {
		return (isBlank(str) ? "" : str.toString().trim());
	}

	/**
	 * 将string字符串进行BASE64编码
	 * 
	 * @param str
	 *            要进行编码的字符串
	 * @param bf
	 *            true:去掉末尾补充的'=' false:不做特殊处理
	 * @return
	 */
	public static String getBASE64(String str, boolean... bf) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		String base64 = new BASE64Encoder().encode(str.getBytes());
		// 去掉 =
		if (isBlank(bf) && bf[0]) {
			base64 = base64.replaceAll("=", "");
		}
		return base64;
	}

	/**
	 * 将BASE64编码后的字符串str进行解码
	 * 
	 * @param str
	 * @return
	 */
	public static String getStrByBASE64(String str) {
		if (isBlank(str)) {
			return "";
		}
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(str);
			return new String(b);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 将map的值转换成get请求的类型 eg:{"name"=20,"age"=30} 转换后 name=20&&age=30
	 * 
	 * @param map
	 * @return
	 */
	public static String mapToGet(Map<? extends Object, ? extends Object> map) {
		String result = "";
		if (map == null || map.size() == 0) {
			return result;
		}
		Set<? extends Object> keys = map.keySet();
		for (Object key : keys) {
			result += ((String) key + "=" + (String) map.get(key) + "&");
		}
		return isBlank(result) ? result : result.substring(0, result.length() - 1);
	}

	/**
	 * 将一串字符串转换成map类型 eg:"?a=3&b=4" 转换后 Map{a=3,b=4}
	 */
	public static Map<String, ? extends Object> getToMap(String args) {
		if (isBlank(args)) {
			return null;
		}
		args = args.trim();

		// 如果开头是'?'则去掉
		if (args.startsWith("?")) {
			args = args.substring(1, args.length());
		}
		String[] argsArray = args.split("&");
		Map<String, Object> result = new HashMap<String, Object>();
		for (String ag : argsArray) {
			if (!isBlank(ag) && ag.indexOf("=") > 0) {
				String[] keyValue = ag.split("=");

				// 如果value或者key值里边包含'='号，以第一个'='号为主
				// eg: name=0=3 转换后{"name","0=3"}，如果有问题自行修改
				String key = keyValue[0];
				String value = "";
				for (int i = 1; i < keyValue.length; i++) {
					value += keyValue[i] + "=";
				}
				value = value.length() > 0 ? value.substring(0, value.length() - 1) : value;
				result.put(key, value);
			}
		}
		return result;
	}

	/**
	 * 将字符串转换成Unicode编码
	 */
	public static String toUnicode(String str) {
		String as[] = new String[str.length()];
		String s1 = "";
		for (int i = 0; i < str.length(); i++) {
			int v = str.charAt(i);
			if (v >= 19968 && v <= 171941) {
				as[i] = Integer.toHexString(str.charAt(i) & 0xffff);
				s1 = s1 + "\\u" + as[i];
			} else {
				s1 = s1 + str.charAt(i);
			}
		}
		return s1;
	}

	/**
	 * 合并数据
	 */
	public static String merge(Object... v) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < v.length; i++) {
			sb.append(v[i]);
		}
		return sb.toString();
	}

	/**
	 * 字符串转URL code
	 */
	public static String strToUrlCode(String value) {
		try {
			value = URLEncoder.encode(value, "utf-8");
			return value;
		} catch (Exception e) {
			LoggerUtils.error(StringUtils.class, "字符串转换为URLCode失败,value：" + value, e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * urlCode转字符串
	 */
	public static String urlCode2String(String value) {
		try {
			value = URLDecoder.decode(value, "utf-8");
			return value;
		} catch (UnsupportedEncodingException e) {
			LoggerUtils.error(StringUtils.class, "URLCode转换成字符串你失败,value:" + value, e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 判断字符串是否包含汉子
	 */
	public static boolean containsCN(String strText) {
		if (isBlank(strText)) {
			return false;
		}
		for (int i = 0; i < strText.length(); i++) {
			String tempA = strText.substring(i, i + 1);
			boolean tempFlag = Pattern.matches("[\u4E00-\u9FA5]", tempA);
			if (tempFlag) {
				return tempFlag;
			}
		}
		return false;
	}

	/**
	 * 去掉HTML代码
	 */
	public static String removeHtml(String news) {
		String tempStr = news.replaceAll("amp;", "").replaceAll("<", "<").replaceAll(">", ">");

		Pattern pattern = Pattern.compile("<(span)?\\sstyle.*?style>|(span)?\\sstyle=.*?>", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(tempStr);
		String str = matcher.replaceAll("");

		Pattern pattern2 = Pattern.compile("(<[^>]+>)", Pattern.DOTALL);
		Matcher matcher2 = pattern2.matcher(str);
		String strHttp = matcher2.replaceAll(" ");

		String regEx = "(((http|https|ftp)(\\s)*((\\:)|：))(\\s)*(//|//)(\\s)*)?"
				+ "([\\sa-zA-Z0-9(\\.|．)(\\s)*\\-]+((\\:)|(:)[\\sa-zA-Z0-9(\\.|．)&%\\$\\-]+)*@(\\s)*)?" + "("
				+ "(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])"
				+ "(\\.|．)(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)"
				+ "(\\.|．)(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)"
				+ "(\\.|．)(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])"
				+ "|([\\sa-zA-Z0-9\\-]+(\\.|．)(\\s)*)*[\\sa-zA-Z0-9\\-]+(\\.|．)(\\s)*[\\sa-zA-Z]*" + ")"
				+ "((\\s)*(\\:)|(：)(\\s)*[0-9]+)?" + "(/(\\s)*[^/][\\sa-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*";
		
		Pattern p1 = Pattern.compile(regEx, Pattern.DOTALL);
		Matcher matchhttp = p1.matcher(strHttp);
		String strnew = matchhttp.replaceAll("").replaceAll("(if[\\s]*\\(|else|elseif[\\s]*\\().*?;", " ");

		Pattern patterncomma = Pattern.compile("(&[^;]+;)", Pattern.DOTALL);
		Matcher matchercomma = patterncomma.matcher(strnew);
		String strout = matchercomma.replaceAll(" ");
		String answer = strout.replaceAll("[\\pP‘’“”]", " ").replaceAll("\r", " ").replaceAll("\n", " ")
				.replaceAll("\\s", " ").replaceAll("　", "");

		return answer;
	}
	
	/**
	 * 把数组中的空数据去掉
	 */
	public static List<String> array2Empty(String[] array){
		List<String> list = new ArrayList<>();
		for(String str : array){
			if(StringUtils.isNotBlank(str)){
				list.add(str);
			}
		}
		return list;
	}
	
	/**
	 * 把数组转换成set
	 */
	public static Set<?> array2Set(Object[] array){
		Set<Object> set = new TreeSet<Object>();
		for(Object tempSetValue : array){
			if(null != tempSetValue){
				set.add(tempSetValue);
			}
		}
		return set;
	}
	
	/**
	 * 序列化后转化成字符串
	 */
	public static String toStringSeriablizable(Serializable serializable){
		if(null == serializable){
			return null;
		}
		try {
			return (String)serializable;
		} catch (Exception e) {
			e.printStackTrace();
			return serializable.toString();
		}
	}

}
