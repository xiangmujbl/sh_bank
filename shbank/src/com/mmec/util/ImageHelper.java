package com.mmec.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.Logger;

import com.mmec.util.ImgAlpha;
  
public class ImageHelper {  
	private static Logger  log = Logger.getLogger(ImageHelper.class);
    /* 
     * 根据尺寸图片居中裁剪 
     */  
     public static void cutCenterImage(String src,String dest,int w,int h) throws IOException{   
         Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");   
         ImageReader reader = (ImageReader)iterator.next();   
         InputStream in=new FileInputStream(src);  
         ImageInputStream iis = ImageIO.createImageInputStream(in);   
         reader.setInput(iis, true);   
         ImageReadParam param = reader.getDefaultReadParam();   
         int imageIndex = 0;   
         Rectangle rect = new Rectangle((reader.getWidth(imageIndex)-w)/2, (reader.getHeight(imageIndex)-h)/2, w, h);    
         param.setSourceRegion(rect);   
         BufferedImage bi = reader.read(0,param);     
         ImageIO.write(bi, "jpg", new File(dest));             
    
     }  
     
    /* 
     * 图片裁剪二分之一 
     */  
     public static void cutHalfImage(String src,String dest) throws IOException{   
         Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");   
         ImageReader reader = (ImageReader)iterator.next();   
         InputStream in=new FileInputStream(src);  
         ImageInputStream iis = ImageIO.createImageInputStream(in);   
         reader.setInput(iis, true);   
         ImageReadParam param = reader.getDefaultReadParam();   
         int imageIndex = 0;   
         int width = reader.getWidth(imageIndex)/2;   
         int height = reader.getHeight(imageIndex)/2;   
         
         System.out.println(width/2);
         System.out.println(height/2);
         System.out.println(width);
         System.out.println(height);
         Rectangle rect = new Rectangle(width/2, height/2, width, height);   
         param.setSourceRegion(rect);   
         BufferedImage bi = reader.read(0,param);     
         ImageIO.write(bi, "jpg", new File(dest));     
     }  
    
     /**
      * 图片裁剪通用接口 
      * @param src   
      * @param dest
      * @param x
      * @param y
      * @param w
      * @param h
      * @throws IOException
      */
    public static void cutImage(String picType,String src,String dest,int x,int y,int w,int h) throws IOException{   
    	 Iterator iterator = ImageIO.getImageReadersByFormatName(picType);   
         ImageReader reader = (ImageReader)iterator.next();   
         InputStream in=new FileInputStream(src);  
         ImageInputStream iis = ImageIO.createImageInputStream(in);   
         reader.setInput(iis, true);   
         ImageReadParam param = reader.getDefaultReadParam();   
           Rectangle rect = new Rectangle(x, y, w,h);    
           param.setSourceRegion(rect);   
           BufferedImage bi = reader.read(0,param);     
           ImageIO.write(bi, "png", new File(dest));             
  
    }   
    
 /*   public static void  clearImgbg(String srcPath, String targetPath) {
    	Image srcImg = ImageIO.read(new File(srcPath));
		byte[] b = ImgAlpha.transferAlpha(srcImg);
		os = new FileOutputStream(targetPath);
		os.write(b);
	}*/
    public static void main(String[] args) throws Exception {
  		try {
//  			ImageHelper.cutHalfImage("D://005.jpg","D://0451.jpg");
  			ImageHelper.cutImage("png","C://LgX3vS6pb2_1479458293163.png","C://test.png",10,10,100,100);
//  			ImageHelper.resizeImage("D://005.jpg","D://045.jpg",5000,4000,true);
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}
    
    /** 
     * 图片缩放
     * @param src  image path
     * @param dest target　Ｐ
     * @param w
     * @param h
     * @throws Exception
     */
    public static void zoomImage(String src,String dest,int w,int h) throws Exception {  
        double wr=0,hr=0;  
        File srcFile = new File(src);  
        File destFile = new File(dest);  
        BufferedImage bufImg = ImageIO.read(srcFile);  
        Image Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_REPLICATE);  
        wr=w*1.0/bufImg.getWidth();  
        hr=h*1.0 / bufImg.getHeight();  
        // 如果是低于400的话 就不操作
        if(wr>1){
        	wr=1;
        }
        if(hr>1){
        	hr=1;
        }
        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);  
        Itemp = ato.filter(bufImg, null);  
        try {  
            ImageIO.write((BufferedImage) Itemp,dest.substring(dest.lastIndexOf(".")+1), destFile);  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
    }  
    
    
    /** 
     * 图片缩放
     * @param src  image path
     * @param dest target　Ｐ
     * @param w
     * @param h
     * @throws Exception
     */
    public static void zoomImage(String src,String dest,double mulriple) throws Exception {  
        int wr=0,hr=0;  
        File srcFile = new File(src);  
        File destFile = new File(dest);  
        BufferedImage bufImg = ImageIO.read(srcFile);  
        wr=bufImg.getWidth();  
        hr= bufImg.getHeight();  
        Image Itemp = bufImg.getScaledInstance(wr, hr, bufImg.SCALE_REPLICATE);  
        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(mulriple, mulriple), null);  
        Itemp = ato.filter(bufImg, null);  
        try {  
            ImageIO.write((BufferedImage) Itemp,dest.substring(dest.lastIndexOf(".")+1), destFile);  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
    }  
    
    /**
     * 图片缩放
     * @param fromFile
     * @param toFile
     * @param outputWidth
     * @param outputHeight
     * @param proportion
     */
    public static void resizeImage(String fromFile, String toFile, int outputWidth, int outputHeight,boolean proportion) {
        try {  
           File f2 = new File(fromFile);  
           BufferedImage bi2 = ImageIO.read(f2);  
	             int newWidth;
	             int newHeight;
           // 判断是否是等比缩放
		       if (proportion == true) {
			        // 为等比缩放计算输出的图片宽度及高度
			        double rate1 = ((double) bi2.getWidth(null)) / (double) outputWidth + 0.1;
			        double rate2 = ((double) bi2.getHeight(null)) / (double) outputHeight + 0.1;
			        // 根据缩放比率大的进行缩放控制
			        double rate = rate1 < rate2 ? rate1 : rate2;
			        newWidth = (int) (((double) bi2.getWidth(null)) / rate);
			        newHeight = (int) (((double) bi2.getHeight(null)) / rate);
		       } else {
		        newWidth = outputWidth; // 输出的图片宽度
		        newHeight = outputHeight; // 输出的图片高度
		       }
            BufferedImage to = new BufferedImage(newWidth, newHeight,BufferedImage.TYPE_INT_RGB);  
            Graphics2D g2d = to.createGraphics();  
            to = g2d.getDeviceConfiguration().createCompatibleImage(newWidth,newHeight,Transparency.TRANSLUCENT);  
            g2d.dispose();  
            g2d = to.createGraphics();  
            Image from = bi2.getScaledInstance(newWidth, newHeight, bi2.SCALE_AREA_AVERAGING);  
            g2d.drawImage(from, 0, 0, null);
            g2d.dispose();  
            ImageIO.write(to, "png", new File(toFile));  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  

    }  
    
    public static void setImgBgClear() throws Exception{
    	BufferedImage temp =ImageIO.read(
                new File("c:/2014.jpg"));//取得图片
        int imgHeight = temp.getHeight();//取得图片的长和宽
        int imgWidth = temp.getWidth();
        int c = temp.getRGB(9, 9);
        BufferedImage bi = new BufferedImage(imgWidth, imgHeight,
                BufferedImage.TYPE_4BYTE_ABGR);//新建一个类型支持透明的BufferedImage
        for(int i = 0; i < imgWidth; ++i)//把原图片的内容复制到新的图片，同时把背景设为透明
        {
            for(int j = 0; j < imgHeight; ++j)
            {
                if(temp.getRGB(i, j) == c)
                    bi.setRGB(i, j, c & 0xff000000);//这里把背景设为透明
                else
                    bi.setRGB(i, j, temp.getRGB(i, j));
            }
        }
        ImageIO.write(bi, "jpg", new File("c:/1051.jpg")); 
    }
    
    /**
     * 加重
     * @param srcPath
     * @param targetPath
     */
    public static void sharpenImg(String srcPath, String targetPath ) throws Exception{
    	FileOutputStream os = null ; 
			Image srcImg = ImageIO.read(new File(srcPath));
			byte[] b = ImgAlpha.aggravateAlpha(srcImg);
			os = new FileOutputStream(targetPath);
			os.write(b);
            os.close();
    }
    
     /**
      * 去背景
      * @param srcPath
      * @param targetPath
      */
    public static void clearImgbg(String srcPath, String targetPath){
    	
    	FileOutputStream os = null ; 
    	try {
			Image srcImg = ImageIO.read(new File(srcPath));
			byte[] b = ImgAlpha.transferAlpha(srcImg);
			os = new FileOutputStream(targetPath);
			os.write(b);
			
		} catch (Exception e) {
			log.info("图片去背景方法出现异常!参数[srcPath:"+srcPath+",targetPath:"+targetPath+"]");
		}finally{
			 if(null!=os){
				 try {
					os.close();
				} catch (Exception e2) {
				}
			 }
		}
    }
} 