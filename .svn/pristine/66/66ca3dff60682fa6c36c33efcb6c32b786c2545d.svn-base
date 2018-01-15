package com.mmec.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import org.apache.log4j.Logger;


public class PictureAndBase64 {  
	private static Logger  log = Logger.getLogger(PictureAndBase64.class);
	public static void main(String[] args) {  
		// 测试从图片文件转换为Base64编码  
		String strImg1 = GetImageStr("E:/office/cert/jiangjunjun%40mymaimai.net_sha256_cn.pfx");
//		String strImg2 = GetImageStr("f:/test1.jpg");
//		log.info(strImg1);
//		strImg1+=strImg2;
		// 测试从Base64编码转换为图片文件 
		GenerateImage(strImg1, "d:\\test.pfx");
	}  
	
	
	
	
	public static String GetImageStr(String imgFilePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		if(imgFilePath == null || "".equals(imgFilePath)){
			return "";
		}
		File file = new File(imgFilePath);
		if(!file.exists()){
			return "";
		}
		byte[] data = null;
		// 读取图片字节数组
		try {
			InputStream in = new FileInputStream(imgFilePath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}
	
	// 对字节数组字符串进行Base64解码并生成图片
	public static boolean GenerateImage(String imgStr, String imgFilePath) {
		if (imgStr == null || "".equals(imgStr)) // 图像数据为空
			return false;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] bytes = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {// 调整异常数据
					bytes[i] += 256;
				}
			}
			// 生成jpeg图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(bytes);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	 
}
