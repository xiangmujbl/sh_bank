package com.mmec.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Random;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import com.google.zxing.Result;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.swetake.util.Qrcode;

public class EncoderHandler {
	 
    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 600;
    // LOGO宽度
    private static final int WIDTH = 60;
    // LOGO高度
    private static final int HEIGHT = 60;

    private static BufferedImage createImage(String content, String imgPath,
            boolean needCompress) throws Exception {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000
                        : 0xFFFFFFFF);
            }
        }
        if (imgPath == null || "".equals(imgPath)) {
            return image;
        }
        // 插入图片
        EncoderHandler .insertImage(image, imgPath, needCompress);
        return image;
    }

   
    private static void insertImage(BufferedImage source, String imgPath,
            boolean needCompress) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println(""+imgPath+"   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

   
    public static void encode(String content, String imgPath, String destPath,
            boolean needCompress) throws Exception {
        BufferedImage image = EncoderHandler .createImage(content, imgPath,
                needCompress);
        mkdirs(destPath);
        String file = new Random().nextInt(99999999)+".jpg";
        ImageIO.write(image, FORMAT_NAME, new File(destPath+"/"+file));
    }

   
    public static void mkdirs(String destPath) {
        File file =new File(destPath);   
        //当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

   
    public static void encode(String content, String imgPath, String destPath)
            throws Exception {
    	EncoderHandler .encode(content, imgPath, destPath, false);
    }

   
    public static void encode(String content, String destPath,
            boolean needCompress) throws Exception {
    	EncoderHandler .encode(content, null, destPath, needCompress);
    }

   
    public static void encode(String content, String destPath) throws Exception {
    	EncoderHandler .encode(content, null, destPath, false);
    }

   
    public static void encode(String content, String imgPath,
            OutputStream output, boolean needCompress) throws Exception {
        BufferedImage image = EncoderHandler .createImage(content, imgPath,
                needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

   
    public static void encode(String content, HttpServletResponse response)
            throws Exception {
    	EncoderHandler .encode(content, null, response.getOutputStream(), false);
    }

   
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(
                image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

   
    public static String decode(String path) throws Exception {
        return EncoderHandler .decode(new File(path));
    }
   
   
    public static void main(String[] args) throws Exception {
        String text = "http://www.baidu.com";
        EncoderHandler.encode(text, "", "F:/22/aa.jpg", true);
    }
    
    public void encoderQRCoder(String content, HttpServletResponse response) {  
        try {  
            Qrcode handler = new Qrcode();  
            handler.setQrcodeErrorCorrect('M');  
            handler.setQrcodeEncodeMode('B');  
            handler.setQrcodeVersion(7);  
              
            System.out.println(content);  
            byte[] contentBytes = content.getBytes("UTF-8");  
              
            BufferedImage bufImg = new BufferedImage(80, 80, BufferedImage.TYPE_INT_RGB);  
              
            Graphics2D gs = bufImg.createGraphics();  
              
            gs.setBackground(Color.WHITE);  
            gs.clearRect(0, 0, 140, 140);  
              
            //设定图像颜色：BLACK  
            gs.setColor(Color.BLACK);  
              
            //设置偏移量  不设置肯能导致解析出错  
            int pixoff = 2;  
            //输出内容：二维码  
            if(contentBytes.length > 0 && contentBytes.length < 124) {  
                boolean[][] codeOut = handler.calQrcode(contentBytes);  
                for(int i = 0; i < codeOut.length; i++) {  
                    for(int j = 0; j < codeOut.length; j++) {  
                        if(codeOut[j][i]) {  
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff,3, 3);  
                        }  
                    }  
                }  
            } else {  
                System.err.println("QRCode content bytes length = " + contentBytes.length + " not in [ 0,120 ]. ");  
            }  
              
            gs.dispose();  
            bufImg.flush();  
              
              
              
            //生成二维码QRCode图片  
            ImageIO.write(bufImg, "jpg", response.getOutputStream());  
		              
		              
		        } catch (Exception e) {  
		            e.printStackTrace();  
		        }  
		    }  
}
