package com.mmec.util.pdf;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.itextpdf.awt.geom.Rectangle2D.Float;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class PdfUtil {
   //final static String keyWord = "）";
    private static int i = 0;
    static List  arrays = new ArrayList ();
    static String sb ;
    static String a ="";
    private static Logger  log = Logger.getLogger(PdfUtil.class);
    /**
     * 数组静态变量清0
     */
    public static void clearArrays(){
    	arrays.clear();
    }
    
    /*
     * @param filePath pdf文件
     * @param keyWord 在模板里做的关键字
     * 返回值为一个数组 分别为x轴,y轴,坐标在文档中的页数(x,y和创建签名域的一样)  57.832397----283.78784-----3.0  
     */
    public static List<float[]> getKeyWords(String filePath, final String keyWord){
        try {
            PdfReader pdfReader = new PdfReader(filePath);
            int pageNum = pdfReader.getNumberOfPages();
            PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(
                    pdfReader);
            for (i = 1; i < pageNum+1; i++) {
                pdfReaderContentParser.processContent(i, new RenderListener() {
                	 @Override
                    public void renderText(TextRenderInfo textRenderInfo) {
                        String text = textRenderInfo.getText(); // 整页内容
                        
                        if (null != text && text.contains(keyWord)) {
                            Float boundingRectange = textRenderInfo.getBaseline().getBoundingRectange();
                            sb = boundingRectange.x+"--"+boundingRectange.y+"---";
                            a =keyWord;
                            float[] resu = new float[4];
                            resu[0] = boundingRectange.x;
                            resu[1] = boundingRectange.y;
                            resu[2] = i;
                            arrays.add(resu);
                        }
                    }

                    @Override
                    public void renderImage(ImageRenderInfo arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void endTextBlock() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void beginTextBlock() {
                        // TODO Auto-generated method stub

                    }

					
                });
            }
           // arrays.add(a);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info(keyWord);
        return arrays;
    }
    
    
    /**
     * 在签名域上面签名
     */
    

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
    
    
    
    public static void main(String[] args) {

        List<float[]> ff = getKeyWords("F:\\office\\1.pdf","*");
        for(float[] f : ff){
            log.info(f[0]+"----"+f[1]+"-----"+f[2]);
        }
//        log.info(sb);

     }

}
