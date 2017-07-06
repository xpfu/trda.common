package com.trda.common.utils;

import java.security.MessageDigest;
import java.util.Random;

/** 
* @company trda
* @author xp.fu
* @version 2017年7月5日 下午3:07:08
*/
public class MathUtils {

	
	/**
	 * 获取随机数(10个数位上数字不重复)
	 * @param length
	 * @return
	 */
	public static String getRandom620(Integer length){
		String result = "";
		
		Random random = new Random();
		int tempInt = 20;
		if(null != length && length > 0){
			tempInt = length;
		}
		boolean[] tempB = new boolean[tempInt];
		int randomInt = 0;
		for(int i = 0; i < length; i++){
			do{
				randomInt = random.nextInt(tempInt);
			}while(tempB[randomInt]);
			
			tempB[randomInt] = true;
			result += randomInt;
		}
		return result;
	}
	
	/**
	 * MD5加密
	 * @param str
	 * @return
	 */
	public static String getMD5(String str){
		StringBuffer md5StrBuffer = new StringBuffer();
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (Exception e) {
			LoggerUtils.fmtError(MathUtils.class, e, "MD5转换异常,message:%s",e.getMessage());
		}
		
		byte[] byteArray = messageDigest.digest();
		for(int i = 0; i < byteArray.length; i++){
			if(Integer.toHexString(0xFF & byteArray[i]).length() == 1){
				md5StrBuffer.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			}else{
				md5StrBuffer.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}
		
		return md5StrBuffer.toString();
	}
	
}
