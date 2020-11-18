/*
 * Copyright © 2019-2020  Whale Cloud, Inc. All Rights Reserved.
 *
 * Notice: Whale Cloud Inc copyrights this specification.
 * No part of this specification may be reproduced in any form or means,
 *  without the prior written consent of Whale Cloud Inc.
 *
 */

package com.wteam.modules.system.utils;

import cn.hutool.core.codec.Base64Encoder;
import org.apache.commons.lang3.RandomUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

/**
 * @author mission
 * @since 2020/08/17 19:32
 */
public class CaptchaUtil {
	private static Validate validate = null;                  //验证码类，用于最后返回此对象，包含验证码图片base64和真值
	private static final Random random = new Random();              //随机类，用于生成随机参数

	private static final int width = 80;     //图片宽度
	private static final int height = 34;    //图片高度

	//将构造函数私有化 禁止new创建
	private CaptchaUtil() {
		super();
	}


	/**
	 * 获取随机指定区间的随机数
	 * @param min (指定最小数)
	 * @param max (指定最大数)
	 * @return /
	 */
	private static int getRandomNum(int min,int max) {
		return RandomUtils.nextInt(min, max);
	}

	/**
	 * 获得字体
	 * @return /
	 */
	private static Font getFont() {
		return new Font("Fixedsys", Font.BOLD, 25);  //名称、样式、磅值
	}

	/**
	 * 获得颜色
	 * @param frontColor /
	 * @param backColor /
	 * @return /
	 */
	private static Color getRandColor(int frontColor, int backColor) {
		if(frontColor > 255)
			frontColor = 255;
		if(backColor > 255)
			backColor = 255;

		int red = frontColor + random.nextInt(backColor - frontColor - 16);
		int green = frontColor + random.nextInt(backColor - frontColor -14);
		int blue = frontColor + random.nextInt(backColor - frontColor -18);
		return new Color(red, green, blue);
	}

	/**
	 * 绘制字符串,返回绘制的字符串
	 * @param g /
	 * @param randomString /
	 * @param i /
	 */
	private static void drawString(Graphics g, String randomString, int i) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(getFont());   //设置字体
		g2d.setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));//设置颜色
		int rot = getRandomNum(4,10);
		g2d.translate(random.nextInt(3), random.nextInt(3));
		g2d.rotate(rot * Math.PI / 180);
		g2d.drawString(randomString, 12*i, 20);
		g2d.rotate(-rot * Math.PI / 180);
	}

	/**
	 * 绘制干扰线
	 * @param g /
	 */
	private static void drawLine(Graphics g) {
		//起点(x,y)  偏移量x1、y1
		int x = random.nextInt(width);
		int y = random.nextInt(height);
		int xl = random.nextInt(13);
		int yl = random.nextInt(15);
		g.setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
		g.drawLine(x, y, x + xl, y + yl);
	}

	/**
	 * 生成Base64图片验证码
	 * @return String 返回base64
	 */
	public static Validate getRandomCode() {
		validate = validate==null?new Validate():validate;

	// BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Graphics g = image.getGraphics();// 获得BufferedImage对象的Graphics对象
		g.fillRect(0, 0, width, height);//填充矩形
		g.setFont(new Font("Times New Roman", Font.PLAIN, 18));//设置字体
		g.setColor(getRandColor(110, 133));//设置颜色
		//绘制干扰线
		//干扰线数量
		int lineSize = 20;
		for(int i = 0; i <= lineSize; i++) {
			drawLine(g);
		}
		//绘制字符


		int leftNum=getRandomNum(0,10);
		int rightNum=getRandomNum(0,10);
		String op = computeNum(leftNum, rightNum);

		drawString(g, String.valueOf(leftNum), 1);
		drawString(g, op, 2);
		drawString(g, String.valueOf(rightNum), 3);
		drawString(g, "=", 4);
		drawString(g, "?", 5);
		g.dispose();//释放绘图资源
		ByteArrayOutputStream bs = null;
		try {
			bs = new ByteArrayOutputStream();
			ImageIO.write(image, "png", bs);//将绘制得图片输出到流
			String imgsrc = Base64Encoder.encode(bs.toByteArray());
			validate.setBase64Str("data:image/png;base64,"+imgsrc);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bs != null) {
					bs.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				bs = null;
			}
		}
		return validate;
	}

	private static String computeNum(int leftNum, int rightNum) {
		int result ;
		switch (getRandomNum(0,3)) {
			case 1:
				result = leftNum - rightNum;
				validate.setValue(String.valueOf(result));
				return "-";
			case 2:
				result = leftNum * rightNum;
				validate.setValue(String.valueOf(result));
				return "×";
			default:
			case 0:
				result = leftNum + rightNum;
				validate.setValue(String.valueOf(result));
				return "+";
		}
	}


	/**
	 * 验证码类
	 * @author mission
	 * @since 2020/09/03 10:10
	 */
	public static class Validate implements Serializable {
		private static final long serialVersionUID = 1L;
		private String Base64Str;       //Base64 值
		private String value;           //验证码值

		public String getBase64Str() {
			return Base64Str;
		}
		public void setBase64Str(String base64Str) {
			Base64Str = base64Str;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
}
