package com.mmec.util;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.mmec.util.lucency.ImageHelper;







public class ImageSealUtil {
	 public static String drawSeal(String message,String path){
		 DrawFrame frame = new DrawFrame(message);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setVisible(true);
	        BufferedImage  bi = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
	        Graphics2D  g2d = bi.createGraphics();
	        frame.paint(g2d);
	        String path_qbj="";
	        String path_ok="";
	        try {
				ImageIO.write(bi, "PNG", new File(path));
				path_qbj=path.substring(0, path.length()-4)+"_qbj.png";
				path_ok=path.substring(0, path.length()-4)+"_ok.png";
				ImageHelper.clearImgbg(path,path_qbj);
				ImageHelper.cutImage(path_qbj, path_ok, 8, 10,180,190);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				frame.dispose();
			}
	        return path_ok;
	 }
	 
	 public static void main(String[] args) {
		 drawSeal("上海公司用户","E://sharefile//yunsign//image//1508918845700.PNG");
	}
}




