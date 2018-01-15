package com.mmec.css.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import com.mmec.css.credlink.Escape;

/**
 * 
 * 实现对文件夹的操作，包含如下操作�? * 1 文件夹读�? * 2 文件夹删�? * 3 文件夹压�?解压
 * @author liuy
 *
 */

public abstract class FolderUints {
	
	/**
	 * 读取文件夹下文件的类�?
	 * 1 true  代表文件�?	 * 2 false 代表文件
	 * @param f  文件夹的对象
	 */
	public static List readFileType(File f)
	{
		File[] fl= f.listFiles();
		List<FileInfo> l=new ArrayList<FileInfo>();
		for(int i=0;i<fl.length;i++)
		{
			File fp=fl[i];
			FileInfo finfo=new FileInfo();
			finfo.setFileName(fp.getName());
			finfo.setF(fp);
			finfo.setFilePath(Escape.escape(fp.getPath()));
			if(fp.isDirectory())
			{
				finfo.setFiletype(true);
			}
			else
			{
				finfo.setFiletype(false);
			}
			l.add(finfo);
		}
		return l;
	}
	  
	  /** 
	   *  根据路径删除指定的目录或文件，无论存在与�?
	   *@param sPath  要删除的目录或文�?
	   *@return 删除成功返回 true，否则返�?false�?
	   */  
	  public static boolean deleteFolder(String sPath) {  
	      boolean flag = false;  
	      File file = new File(sPath);  
	      // 判断目录或文件是否存�? 
	      if (!file.exists()) {  // 不存在返�?false  
	          return flag;  
	      } else {  
	          // 判断是否为文�? 
	          if (file.isFile()) {  // 为文件时调用删除文件方法  
	              return deleteFile(sPath);  
	          } else {  // 为目录时调用删除目录方法  
	              return deleteDirectory(sPath);  
	          }  
	      }  
	  }  
	  
	  /** 
	   * 删除单个文件 
	   * @param   sPath    被删除文件的文件�?
	   * @return 单个文件删除成功返回true，否则返回false 
	   */  
	  private static boolean deleteFile(String sPath) {  
		  boolean flag = false;  
		  File file = new File(sPath);  
	      // 路径为文件且不为空则进行删除  
	      if (file.isFile() && file.exists()) {  
	          file.delete();  
	          flag = true;  
	      }  
	      return flag;  
	  }  
	  
	  /** 
	   * 删除目录（文件夹）以及目录下的文�?
	   * @param   sPath 被删除目录的文件路径 
	   * @return  目录删除成功返回true，否则返回false 
	   */  
	  private static boolean deleteDirectory(String sPath) {  
	      //如果sPath不以文件分隔符结尾，自动添加文件分隔�? 
	      if (!sPath.endsWith(File.separator)) {  
	          sPath = sPath + File.separator;  
	      }  
	      File dirFile = new File(sPath);  
	      //如果dir对应的文件不存在，或者不是一个目录，则�?�? 
	      if (!dirFile.exists() || !dirFile.isDirectory()) {  
	          return false;  
	      }  
	      boolean flag = true;  
	      //删除文件夹下的所有文�?包括子目�?  
	      File[] files = dirFile.listFiles();  
	      for (int i = 0; i < files.length; i++) {  
	          //删除子文�? 
	          if (files[i].isFile()) {  
	              flag = deleteFile(files[i].getAbsolutePath());  
	              if (!flag) break;  
	          } //删除子目�? 
	          else {  
	              flag = deleteDirectory(files[i].getAbsolutePath());  
	              if (!flag) break;  
	          }  
	      }  
	      if (!flag) return false;  
	      //删除当前目录  
	      if (dirFile.delete()) {  
	          return true;  
	      } else {  
	          return false;  
	      }  
	  } 
	  
	  /**
		 *  实现文件夹压�?		 *  String path="E:\\Tools\\MyTools\\Tomcat6\\file\\fileLoad\\CSN100000000001\\";
		 *  ZipOutputStream out = new ZipOutputStream(new FileOutputStream("D://1.zip"));
		 *  File file=new  File(path);
		 *  compressionFolder(out,file,file.getPath());
		 *  out.close();
		 * 
		 * @param zosm  输出文件路径
		 * @param folder  输入的文件夹路径
		 * @param basePath   不需要的路径去除
		 * @throws Exception 
		 */
		public static void compressionFolder(ZipOutputStream out,File folder,String basePath) throws Exception
		{
			File[] srcfile = folder.listFiles();
			for (int i = 0; i < srcfile.length; i++) 
			{
				File f=srcfile[i];
				if(f.isDirectory())
				{
					//文件�?					compressionFolder(out,f,basePath);
				}
				else
				{
					//文件
					compressionFile(out,srcfile[i],basePath);
				}
		    }
		}
		
		private static void compressionFile(ZipOutputStream out,File f,String basePath) throws Exception
		{
	        String filePath=f.getPath();
	        filePath=filePath.replace(basePath, "");
	        String s=filePath.substring(0, 1);
	        if(s.equals(File.separator))
	        {
	        	filePath=filePath.substring(1, filePath.length());
	        }
	        out.putNextEntry(new ZipEntry(filePath)); 
	        byte[] blist=FileUtils.readFileToByteArray(f);
	        out.write(blist);
	        out.closeEntry();
		}
		
	  /**
		 * 多文件压�?在本目录下生成对应压缩文件，已流的形式返�?		 * 
		 * @param folder  输入的文件夹路径
		 * @throws IOException
		 */
		public static byte[] compressionFolder(File folder) throws Exception
		{
			ByteArrayOutputStream  out=new ByteArrayOutputStream();
			ZipOutputStream zosm = new ZipOutputStream(out);
			compressionFolder(zosm,folder,folder.getPath());
			zosm.close();
			return out.toByteArray();
		}
	  
		
		private static void getDir(String directory, String subDirectory){
		     String dir[];
		     File fileDir = new File(directory);
		     try {
		      if (subDirectory == "" && fileDir.exists() != true)
		       fileDir.mkdir();
		      else if (subDirectory != "") {
		       dir = subDirectory.replace('\\', '/').split("/");
		       for (int i = 0; i < dir.length; i++) {
		        File subFile = new File(directory + File.separator + dir[i]);
		        if(subFile.exists() == false)
		         subFile.mkdir();
		        directory += File.separator + dir[i];
		       }
		      }
		     }catch (Exception ex) {
		       System.out.println(ex.getMessage());
		     }
		 }
		 
		   /**
			 * 解压文件夹，在指定的目录下生成对应的文件 
			 * @param zipFileNaame
			 *            待解压的文件zip文件
			 * @param outputDirectory
			 *            待解压的输出文件目录
		 * @throws Exception 
		 * @throws IOException 
			 * 
			 */ 
		 public static  void unZipFolder(String zipFileName, String outputDirectory) throws Exception 
		 {
		     //获取压缩包名称，并在输出目录下创�?			 
			 File zip=new File(zipFileName);
			 String zipName=zip.getName();
			 String baseName=outputDirectory+File.separator+zipName.substring(0,zipName.lastIndexOf("."));
			 FileUtils.forceMkdir(new File(baseName));
		     ZipFile zipFile = new ZipFile(zipFileName);
		     java.util.Enumeration e = zipFile.getEntries();
		     ZipEntry zipEntry = null;
		      while (e.hasMoreElements()) 
		      {
		         zipEntry = (ZipEntry) e.nextElement();
		         if (zipEntry.isDirectory()) {                // 如果得到的是个目录，
		          String name = new String(zipEntry.getName().getBytes());         // 就创建在指定的文件夹下创建目�?		           name = name.substring(0, name.length() - 1);
		          File f = new File(baseName + File.separator + name);
		          f.mkdir();
		         }
		         else {
		          String fileName = new String(zipEntry.getName().getBytes()); 
		          fileName = fileName.replace('\\', '/');
		          // System.out.println("测试文件1�? +fileName);
		          if (fileName.indexOf("/") != -1){
		           getDir(baseName,fileName.substring(0, fileName.lastIndexOf("/")));
		           // System.out.println("文件的路径："+fileName);
		           fileName=fileName.substring(fileName.lastIndexOf("/")+1,fileName.length());
		              
		          }
		          File f = new File(baseName + File.separator + zipEntry.getName());
		             f.createNewFile();
		             InputStream in = zipFile.getInputStream(zipEntry);
		             FileOutputStream out=new FileOutputStream(f);
		             byte[] by = new byte[1024];
		             int c;
		             while ( (c = in.read(by)) != -1) {
		              out.write(by, 0, c);
		           }
		           out.close();
		           in.close();
		         }
		       } 
		      zipFile.close();
		 }
		 
		 
		   /**
			 * 解压文件夹，在zip包下面生成同样的文件解压文件 
			 * @param zipFileNaame
			 *            待解压的文件zip文件
			 * @param outputDirectory
			 *            待解压的输出文件目录
		 * @throws IOException 
			 * 
			 */ 
		 public static void unZipFolder(String zipFileName) throws Exception
		 { 
			 File f=new File(zipFileName);		 
			 String outputDirectory= f.getParentFile().getPath();
			 unZipFolder(zipFileName,outputDirectory);
		 }
}