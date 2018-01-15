package com.mmec.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

public class PdfToImgUtil {
	public static final String FILETYPE_JPG = "jpg";

	public static final String SUFF_IMAGE = "." + FILETYPE_JPG;
	
	/**
	 * 
	 * 将指定pdf文件转换为指定路径的缩略图
	 * 
	 * @param filepath
	 *            原文件路径，例如d:/test.pdf
	 * 
	 * @param filename
	 *            文件名
	 * 
	 * @param zoom
	 *            缩略图显示倍数，1表示不缩放，0.3则缩小到30%
	 */

	public static Map<Integer,String> tranfer(File file, float zoom) throws PDFException, PDFSecurityException, IOException {
		// 图片存放路径
		Map<Integer,String> map = new HashMap<Integer,String>();
		
		// 生成图片目录
		String temppath = file.getParent()  + "/img/" + file.getName();
		temppath = temppath.substring(0, temppath.lastIndexOf("."))+"/";
	
		File image = new File(temppath);
		if(!image.exists()){
			image.mkdirs();
		}
		// ICEpdf document class

		Document document = null;

		float rotation = 0f;

		document = new Document();

		// 设置PDF文件路径
		document.setFile(file.getCanonicalPath());

		int pages = document.getNumberOfPages();

		for (int i = 0; i < pages; i++) {
			String imagepath = temppath;
			
			BufferedImage img = (BufferedImage) document.getPageImage(i,

			GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation,

			zoom);

			Iterator iter = ImageIO.getImageWritersBySuffix(FILETYPE_JPG);

			ImageWriter writer = (ImageWriter) iter.next();
			
			// 图片路径
			imagepath += i + ".jpg"; System.out.println(imagepath);

			File outFile = new File(imagepath);

			FileOutputStream out = new FileOutputStream(outFile);

			ImageOutputStream outImage = ImageIO.createImageOutputStream(out);

			writer.setOutput(outImage);

			writer.write(new IIOImage(img, null, null));
			
			outImage.close();
			
			out.close();
			
			map.put(i, imagepath);
			
		}
		
		return map;

	}
	public static Map<Integer,String> tranferForPdfSign(File file, float zoom,String imgPath) throws PDFException, PDFSecurityException, IOException {
		// 图片存放路径
		Map<Integer,String> map = new HashMap<Integer,String>();
		
		// 生成图片目录
//		String temppath = file.getParent()  + "/img/" + file.getName();
//		temppath = temppath.substring(0, temppath.lastIndexOf("."))+"/";
		String temppath = "E:/office/pdfToImg/img/";
		File image = new File(temppath);
		if(!image.exists()){
			image.mkdirs();
		}
		// ICEpdf document class

		Document document = null;

		float rotation = 0f;

		document = new Document();

		// 设置PDF文件路径
		document.setFile(file.getCanonicalPath());

		int pages = document.getNumberOfPages();

		for (int i = 0; i < pages; i++) {
			String imagepath = temppath;
			
			BufferedImage img = (BufferedImage) document.getPageImage(i,

			GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation,

			zoom);

			Iterator iter = ImageIO.getImageWritersBySuffix(FILETYPE_JPG);

			ImageWriter writer = (ImageWriter) iter.next();
			
			// 图片路径
			imagepath += i + ".jpg"; System.out.println(imagepath);

			File outFile = new File(imagepath);

			FileOutputStream out = new FileOutputStream(outFile);

			ImageOutputStream outImage = ImageIO.createImageOutputStream(out);

			writer.setOutput(outImage);

			writer.write(new IIOImage(img, null, null));
			
			outImage.close();
			
			out.close();
			
			map.put(i, imagepath);
			
		}
		
		return map;

	}
	public static void main(String[] args) {
		try {
//			tranfer(new File("F:/office/MVC.pdf"),(float) 1.5);
			tranferForPdfSign(new File("E:/office/pdfToImg/img/20160303184511288.pdf"),(float) 1.5,"");
		} catch (PDFException e) {
			e.printStackTrace();
		} catch (PDFSecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
