package com.mmec.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.mmec.util.ColorTable;

/**
 * 实现去除白色或者黑色背景
 * 
 *
 */
public class ImgAlpha {
 
	/**
	 * 去除图片白色元素
	 * @param image  待处理的图片
	 * @return
	 * @throws Exception 
	 */
    public static byte[] transferAlpha(Image image) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIcon imageIcon = new ImageIcon(image);
            BufferedImage bufferedImage = new BufferedImage(imageIcon
                    .getIconWidth(), imageIcon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon
                    .getImageObserver());
            int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) 
            {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) 
                {
                    int rgb = bufferedImage.getRGB(j2, j1);
                    if(ColorTable.isGround(rgb))
                    {
                    	rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                    	bufferedImage.setRGB(j2, j1, rgb);
                    }
                   /* else
                    {
                    	rgb =0xffff0000;
                    	bufferedImage.setRGB(j2, j1, rgb);
                    }*/
                }
            }
            g2D.drawImage(bufferedImage,0, 0, imageIcon.getImageObserver());
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byte[] by=byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return by;
        } 
        catch (Exception e) 
        {
        	byteArrayOutputStream.close();
        	throw new Exception();
        }
        finally
        {
        	if(byteArrayOutputStream!=null)
        	{
        		byteArrayOutputStream.close();
        	}
        }
    }
 
    /**
	 * 图片加重
	 * @param image  待处理的图片
	 * @return
	 * @throws Exception 
	 */
    public static byte[] aggravateAlpha(Image image) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIcon imageIcon = new ImageIcon(image);
            BufferedImage bufferedImage = new BufferedImage(imageIcon
                    .getIconWidth(), imageIcon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon
                    .getImageObserver());
            int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) 
            {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) 
                {
                    int rgb = bufferedImage.getRGB(j2, j1);
                    if(ColorTable.isGround(rgb))
                    {
                    	rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                    	bufferedImage.setRGB(j2, j1, rgb);
                    }
                   else
                    {
                    	rgb =0xffff0000;
                    	bufferedImage.setRGB(j2, j1, rgb);
                    }
                }
            }
            g2D.drawImage(bufferedImage,0, 0, imageIcon.getImageObserver());
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byte[] by=byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return by;
        } 
        catch (Exception e) 
        {
        	byteArrayOutputStream.close();
        	throw new Exception();
        }
        finally
        {
        	if(byteArrayOutputStream!=null)
        	{
        		byteArrayOutputStream.close();
        	}
        }
    }
 
    //byte[] ------>BufferedImage
    public static BufferedImage ByteToBufferedImage(byte[] byteImage) throws IOException{
        ByteArrayInputStream in = new ByteArrayInputStream(byteImage);
        BufferedImage buffImage = ImageIO.read(in);    
        return buffImage;
    }
 
    //Image转换为BufferedImage；
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        image = new ImageIcon(image).getImage();
        boolean hasAlpha = false;
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null),
                    image.getHeight(null), transparency);
        } catch (HeadlessException e) {
        }
        if (bimage == null) {
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null),
                    image.getHeight(null), type);
        }
        Graphics g = bimage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }
    
    /**
	 * @param args
     * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		    String srcImgPath = "E:\\日常工作\\IT运维\\图章\\test_1.jpg";    
	        String targerPath = "E:\\日常工作\\IT运维\\图章\\test_2.jpg"; 
	        Image srcImg = ImageIO.read(new File(srcImgPath));   
//	        byte[] b=transferAlpha(srcImg); 
	        byte[] b=aggravateAlpha(srcImg); 
	        FileOutputStream os = new FileOutputStream(targerPath);
            // 生成图片  
	        os.write(b);
	        os.close();
	}
}