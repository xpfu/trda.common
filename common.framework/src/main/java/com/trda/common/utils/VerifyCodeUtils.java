package com.trda.common.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

import com.trda.core.shiro.token.manager.TokenManager;

/** 
* @company trda
* @author xp.fu
* @version 2017年7月6日 上午10:41:32
* 验证码工具类
*/
public class VerifyCodeUtils {

	//使用Algerian字体，系统没有需要安装,字体只显示大写,去掉了1,0，i，o几个容易混淆的字符
	public static final String VERIFY_CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
	//验证码的key
	public static final String V_CODE = "_CODE";
	private static Random random = new Random();
	
	/**
	 * 验证码对象
	 * @author xp.fu
	 *
	 */
	public static class Verify{
		//eg: 1 + 2
		private String code;
		//eg: 3
		private Integer value;
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public Integer getValue() {
			return value;
		}
		public void setValue(Integer value) {
			this.value = value;
		}
	}
	
	/**
	 * 使用系统默认字符源生成验证码
	 * @return
	 */
	public static Verify generateVerify(){
		int num1 = new Random().nextInt(10) + 1;
		int num2 = new Random().nextInt(10) + 1;
		Verify entity = new Verify();
		entity.setCode(num1 + " x " + num2);
		entity.setValue(num1 + num2);
		
		return entity;
	}
	
	/**
	 * 使用系统默认字符源生成验证码
	 * @param verifySize 验证码长度
	 * @return
	 */
	public static String generateVerifyCode(int verifySize){
		return generateVerifyCode(verifySize,VERIFY_CODES);
	}
	
	/**
	 * 清楚验证码
	 */
	public static void clearVerifyCode(){
		TokenManager.getSession().removeAttribute(V_CODE);
	}
	
	/**
	 * 对比验证码
	 * @param code
	 * @return
	 */
	public static boolean verifyCode(String code){
		String v = (String)TokenManager.getValueFromSession(V_CODE);
		return StringUtils.equals(v, StringUtils.lowerCase(code));
	}
	
	/**
	 * 使用指定字符源生成验证码
	 * @param verifySize
	 * @param sources
	 * @return
	 */
	public static String generateVerifyCode(int verifySize,String sources){
		if(sources == null || sources.length() == 0){
			sources = VERIFY_CODES;
		}
		int codesLen = sources.length();
		Random random = new Random(System.currentTimeMillis());
		StringBuilder verifyCode = new StringBuilder(verifySize);
		for(int i = 0;i < verifySize; i++){
			verifyCode.append(sources.charAt(random.nextInt(codesLen - 1)));
		}
		return verifyCode.toString();
	}
	
	/**
	 * 生成随机验证码文件并返回验证码的值
	 * @param w
	 * @param h
	 * @param outputFile
	 * @param verifySize
	 * @return
	 * @throws Exception 
	 */
	public static String generateVerifyCode(int w,int h,File outputFile,int verifySize) throws Exception{
		String verifyCode = generateVerifyCode(verifySize);
		outputImage(w,h,outputFile,verifyCode);
		return verifyCode;
	}
	
	/**
	 * 输出随机验证码图片流并返回验证码的值
	 * @param w
	 * @param h
	 * @param os
	 * @param verifySize
	 * @return
	 * @throws IOException 
	 */
	public static String outputVerifyImage(int w,int h,OutputStream os,int verifySize) throws IOException{
		String verifyCode = generateVerifyCode(verifySize);
		outputImage(w,h,os,verifyCode);
		return verifyCode;
	}
	
	/**
	 * 生成指定验证码图像文件
	 * @param w
	 * @param h
	 * @param outputFile
	 * @param verifySize
	 * @throws Exception 
	 */
	public static void outputImage(int w,int h,File outputFile,String code) throws Exception{
		if(null == outputFile){
			return;
		}
		File dir = outputFile.getParentFile();
		if(!dir.exists()){
			dir.mkdirs();
		}
		try {
			outputFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(outputFile);
			outputImage(w,h,fos,code);
			fos.close();
		} catch (Exception e) {
			throw e;
			
		}
	}
	
	/**
	 * 输出指定验证码图片流
	 * @param w
	 * @param h
	 * @param os
	 * @param code
	 * @throws IOException 
	 */
	
	
	public static void outputImage(int w,int h,OutputStream os,String code) throws IOException{
		int verifySize = code.length();
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_BGR);
		Random random = new Random();
		Graphics2D graphics2d = image.createGraphics();
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		Color[] colors = new Color[5];
		Color[] colorSpaces = new Color[]{Color.WHITE,Color.CYAN,Color.GRAY,Color.LIGHT_GRAY,Color.MAGENTA,Color.ORANGE,Color.PINK,Color.YELLOW};
		//小数部分
		float[] fractions = new float[colors.length];
		for(int i = 0;i < colors.length; i++){
			colors[i] = colorSpaces[random.nextInt(colorSpaces.length)];
			fractions[i] = random.nextFloat();
		}
		Arrays.sort(fractions);
		
		//设置边框颜色
		graphics2d.setColor(Color.GRAY);
		graphics2d.fillRect(0, 0, w, h);
		//设置背景色
		Color backGrandColor = getRandomColor(200,250);
		graphics2d.setBackground(backGrandColor);
		graphics2d.fillRect(0, 2, w, h - 4);
		
		//绘制干扰线
		Random randomLine = new Random();
		//设置线条的颜色
		graphics2d.setColor(getRandomColor(160,200));
		for(int i = 0; i < 20; i++){
			int x = randomLine.nextInt(w - 1);
			int y = randomLine.nextInt(h - 1);
			int x1 = randomLine.nextInt(6) + 1;
			int y1 = randomLine.nextInt(12) + 1;
			graphics2d.drawLine(x, y,x + x1 + 40,y + y1 + 20);
		}
		
		//添加噪点
		//噪声率
		float yawpRate = 0.05f;
		int area = (int)(yawpRate*w*h);
		for(int i = 0; i < area; i++){
			int x = randomLine.nextInt(w);
			int y = randomLine.nextInt(h);
			int rgb = getRandomIntColor();
			image.setRGB(x, y, rgb);
		}
		
		//使图片扭曲
		shear(graphics2d,w,h,backGrandColor);
		
		graphics2d.setColor(getRandomColor(100,160));
		int fontSize = h - 4;
		Font font = new Font("Algerian",Font.ITALIC,fontSize);
		graphics2d.setFont(font);
		
		char[] chars = code.toCharArray();
		for(int i = 0; i < verifySize; i ++){
			AffineTransform affine = new AffineTransform();
			affine.setToRotation(Math.PI / 4 * random.nextDouble() * (random.nextBoolean() ? 1 : -1), (w / verifySize) * i + fontSize / 2, h / 2);
			graphics2d.setTransform(affine);
			graphics2d.drawChars(chars, i, 1, ((w - 10) / verifySize) * i + 5, h / 2 + fontSize / 2 - 10);
		}
		graphics2d.dispose();
		ImageIO.write(image, "jpg", os);
	}
	
	/**
	 * 获得随机数组成的RGB颜色
	 * @param fc
	 * @param bc
	 * @return
	 */
	private static Color getRandomColor(int fc,int bc){
		if(fc > 255){
			fc = 255;
		}
		if(bc > 255){
			bc = 255;
		}
		
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		
		return new Color(r,g,b);
	}
	
	/**
	 * 获得随机整数的RGB颜色
	 * @return
	 */
	private static int getRandomIntColor(){
		int[] rgb = getRandomRgb();
		int color = 0;
		for(int c : rgb){
			color = color << 8;
			color = color | c;
		}
		return color;
	}
	
	/**
	 * 获得随机数组成的Rgb颜色数组
	 * @return
	 */
	private static int[] getRandomRgb(){
		int[] rgb = new int[3];
		for(int i = 0; i < 3; i++){
			rgb[i] = random.nextInt(255);
		}
		return rgb;
	}
	
	/**
	 * 使图片扭曲
	 * @param g
	 * @param w1
	 * @param h1
	 * @param color
	 */
	private static void shear(Graphics g, int w, int h, Color color){
		//使X方向扭曲
		shearX(g,w,h,color);
		//使Y方向扭曲
		shearY(g,w,h,color);
	}
	
	/**
	 * 使X方向扭曲
	 * @param g
	 * @param w
	 * @param h
	 * @param color
	 */
	private static void shearX(Graphics g, int w, int h, Color color){
		int period = random.nextInt(2);
		
		boolean borderGap = true;
		int frames = 1;
		int phase = random.nextInt(2);
		for(int i = 0; i < h; i++){
			double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea(0, i, w, 1, (int) d, 0);
            if (borderGap) {
                g.setColor(color);
                g.drawLine((int) d, i, 0, i);
                g.drawLine((int) d + w, i, w, i);
            }
		}
	}
	
	
	private static void shearY(Graphics g, int w, int h, Color color){
		int period = random.nextInt(40) + 10;
		boolean borderGap = true;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea(i, 0, 1, h, 0, (int) d);
            if (borderGap) {
                g.setColor(color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, (int) d + h, i, h);
            }

        }
	}
	
}
