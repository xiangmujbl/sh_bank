package com.mmec.util.lucency;

import com.mmec.util.lucency.Color;

/**
 * 实现对颜色的定义
 * @author liuy
 *
 */
public class ColorTable {
	
	/**
	 * 获取颜色
	 * @param rgb
	 * @return
	 */
	private static Color getRGB(int rgb)
	{
		int R =(rgb & 0xff0000 ) >> 16 ;
     	int G= (rgb & 0xff00 ) >> 8 ;
     	int B= (rgb & 0xff );
     	Color c=new Color();
     	c.setR(R);
     	c.setG(G);
     	c.setB(B);
     	return c;
	}
	
	/**
	 * 判定是否为公章的颜色，公章目前主要分2种颜色（纯红色为（255,0,0））：
	 * 1 红色： 200<=r<=255&&0<=g<=50&&0<=b<=50
	 * 2 蓝色：
	 * @param rgb  颜色的值
	 */
	public static boolean isSeal(int rgb)
	{
		Color c=getRGB(rgb);
		int r=c.getR();
		int g=c.getG();
		int b=c.getB();
		if(200<=r&&r<=255&&0<=g&&g<=50&&0<=b&&b<=50)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 判定是否为白色的图片背景色（纯白色为（255,255,255））
	 * 取值范围：
	 * 220<=r<=255&&220<=r<=255&&220<=b<=255  
	 * @param rgb
	 * @return
	 */
	public static boolean isGround(int rgb)
	{
		Color c=getRGB(rgb);
		int r=c.getR();
		int g=c.getG();
		int b=c.getB();
		if(220<=r&&r<=255&&
				220<=g&&g<=255&&
						220<=b&&b<=255)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	/**
	 * 判定是否为黑色
	 * 取值范围：
	 * 0<=r<=95&&0<=r<=95&&0<=b<=95
	 * @param rgb
	 * @return
	 */
	public static boolean isBack(int rgb)
	{
		Color c=getRGB(rgb);
		int r=c.getR();
		int g=c.getG();
		int b=c.getB();
		if(0<=r&&r<=95&&0<=g&&g<=95&&0<=b&&b<=95)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
