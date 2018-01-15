package com.mmec.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.log4j.Logger;


public class PictureAndBase64 {  
	private static Logger  log = Logger.getLogger(PictureAndBase64.class);
	public static void main(String[] args) {  
		/*// 测试从图片文件转换为Base64编码  
		String strImg1 = GetImageStr("f:/test.jpg");
		String strImg2 = GetImageStr("f:/test1.jpg");
		log.info(strImg1);
		strImg1+=strImg2;
		// 测试从Base64编码转换为图片文件 
		GenerateImage(strImg1, "f:\\test11.jpg");*/
		
		
		try {
			String a=getFileSha1("E:/sharefile/mmec_center_3/uploadAttachment/20171015095152495.pdf");
			String b=GetImageStr("E:/sharefile/mmec_center_3/uploadAttachment/20171015095152495.pdf");
			System.out.println(a);
			System.out.println(b);
		} catch (OutOfMemoryError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	  /**
     * 将svg字符串转换为png
     * 
     * @param svgCode svg代码
     * @param pngFilePath 保存的路径
     * @throws TranscoderException svg代码异常
     * @throws IOException io错误
     */
    public static void convertToPng(String svgCode, String pngFilePath) throws IOException,
            TranscoderException {

        File file = new File(pngFilePath);

        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            convertToPng(svgCode, outputStream);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将svgCode转换成png文件，直接输出到流中
     * 
     * @param svgCode svg代码
     * @param outputStream 输出流
     * @throws TranscoderException 异常
     * @throws IOException io异常
     */
    public static void convertToPng(String svgCode, OutputStream outputStream)
            throws TranscoderException, IOException {
        try {
            byte[] bytes = svgCode.getBytes("utf-8");
            PNGTranscoder t = new PNGTranscoder();
            TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(bytes));
            TranscoderOutput output = new TranscoderOutput(outputStream);
            t.transcode(input, output);
            outputStream.flush();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
    /** 
	    * 取文件的hash值
	    */  
	    public static String getFileSha1(String path) throws OutOfMemoryError,IOException {  
	   	File file=new File(path); 
	   	long length=file.length();
	   	FileInputStream in = new FileInputStream(file);  
	       MessageDigest messagedigest;  
	       try {  
		        messagedigest = MessageDigest.getInstance("SHA-1");  
		        byte[] buffer = new byte[(int) length];
		        //byte[] buffer = new byte[1024 * 1024 * 10];  
		        int len = 0;  
	       while ((len = in.read(buffer)) >0) {  
	      //该对象通过使用 update（）方法处理数据  
	        messagedigest.update(buffer, 0, len);  
	       }  
	       //对于给定数量的更新数据，digest 方法只能被调用一次。在调用 digest 之后，MessageDigest 对象被重新设置成其初始状态。  
	       	return byte2HexStr(messagedigest.digest());  
	   	}   catch (NoSuchAlgorithmException e) {  
	           e.printStackTrace(); 
	       }  
	   	catch (OutOfMemoryError e) { 
	           e.printStackTrace();  
	           throw e;  
	       }  
	   	finally{  
	        in.close();  
	   	}  
	       return null;  
	    } 
	    

		/** 
		 * bytes转换成十六进制字符串 
		 */  
		public static String byte2HexStr(byte[] b) {  
		    String hs = "";  
		    String stmp = "";  
		    for (int n = 0; n < b.length; n++) {  
		        stmp = (Integer.toHexString(b[n] & 0XFF));  
		        if (stmp.length() == 1)  
		            hs = hs + "0" + stmp;  
		        else  
		            hs = hs + stmp;  
		        // if (n<b.length-1) hs=hs+":";  
		    }  
		    return hs.toUpperCase();  
		}
	
}
