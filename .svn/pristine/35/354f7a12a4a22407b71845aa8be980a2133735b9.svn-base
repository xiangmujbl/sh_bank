package com.mmec.util.pdf;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;



import org.apache.log4j.Logger;

import com.mmec.exception.ServiceException;


public class ZIPTool
{   
	private static Logger log = Logger.getLogger(ZIPTool.class);
	public static String unzip(String zipPath,String destPath) throws ServiceException{
		String outpath="";
		ZipFile in;
		try
		{
			String destFilePath = destPath;
			in = new ZipFile(zipPath,Charset.forName("GBK"));//
			Enumeration e = in.entries();
			while(e.hasMoreElements())
			{
				ZipEntry entry = (ZipEntry) e.nextElement();
				if (entry.isDirectory())
				{
					File dir = new File(destFilePath + entry.getName());
					if (!dir.exists())
					{
						dir.mkdirs();
					}
					continue;
				}
				BufferedInputStream bis = new BufferedInputStream(
						in.getInputStream(entry));
				
				File file = new File(destFilePath + entry.getName());
				File parent = file.getParentFile();
				if (parent != null && (!parent.exists()))
				{
					parent.mkdirs();
				}
				outpath=parent.getParent();
				FileOutputStream fos = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);
				byte[] buf = new byte[1024];
				int len = 0;
				while ((len = bis.read(buf, 0, 1024)) != -1)
				{
					fos.write(buf, 0, len);
				}
				bos.flush();
				bos.close();
				bis.close();
			}
			in.close();	
		} 
		catch (FileNotFoundException e){
			e.printStackTrace();	
			throw new ServiceException("解压失败","源文件不存在");
		}
	    catch(IOException e){  
	    	e.printStackTrace();
	    	throw new ServiceException("解压失败","IO异常");
	    }
		catch (Exception e){ 
			throw new ServiceException("解压失败","系统异常");
		}
		return outpath;
	}
	
	public static void main(String[] args) throws ServiceException{
		ZIPTool.unzip("C:\\Users\\Administrator\\Desktop\\CP8275352290021162.zip", "C:\\Users\\Administrator\\Desktop\\");
		
	}
}

