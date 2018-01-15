package com.mmec.css.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import org.apache.commons.io.FileUtils;
import com.mmec.css.mmec.MFilePath;
import com.mmec.css.mmec.element.MHeadAndBody;


/**
 * 实现对txt文本的操�? * 演示�? *  TxtOperate j = new TxtOperate();
 *	    File f=new File("D:\\CSN100000000001\\ContractRecordSHA1.txt");
 *	    j.insertStringInHead(f,"Time:2013-11-12 10:10:10\r\nContNum:HT1000001");
 *	    j.insertStringInEnd(f,"Name:ContractRecord/Contract/Z_1_MMEC�?��合同.pdf\r\nSHA1-Digest: fa9hAIRmp/MJdiXJ9UQr85AmkEI=");
 * 
 * 
 * @author liuy
 * 
 */
public abstract class TxtOperate {
	/**
	 * 创建整个模板格式
	 *  @param basePath  基本路径
	 *  @param content 文件内容，包含头文件和正�?	 **/
	public static void createNewTemplate(MFilePath mFilePath,MHeadAndBody content)
	{
		try {
			File f=new File(mFilePath.getContractRecordSHA1Path());
			FileUtils.writeStringToFile(f,content.getHead());
			
			f=new File(mFilePath.getServerSignPath());
			FileUtils.writeStringToFile(f,content.getHead());
			
			f=new File(mFilePath.getContractSHA1Path());
			FileUtils.writeStringToFile(f,content.getHead());
			
			f=new File(mFilePath.getUserGroupSignPath());
			FileUtils.writeStringToFile(f,content.getHead());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	 /**
	   * 在文件里面的指定行插入一行数�?	   * 
	   * @param inFile
	   *          文件
	   * @param lineno
	   *          行号
	   * @param lineToBeInserted
	   *          要插入的数据
	   * @throws Exception
	   *           IO操作引发的异�?	   */
	  public static void insertStringInFile(File inFile, int lineno, String lineToBeInserted)
	      throws Exception {
	    // 临时文件
	    File outFile = File.createTempFile("name", ".tmp");
	    // 输入
	    FileInputStream fis = new FileInputStream(inFile);
	    BufferedReader in = new BufferedReader(new InputStreamReader(fis));
	    // 输出
	    FileOutputStream fos = new FileOutputStream(outFile);
	    PrintWriter out = new PrintWriter(fos);
	    // 保存�?��数据
	    String thisLine;
	    // 行号�?�?��
	    int i = 1;
	    while ((thisLine = in.readLine()) != null) {
	      // 如果行号等于目标行，则输出要插入的数�?	    
	    if (i == lineno) {
	        out.println(lineToBeInserted);
	      }
	      // 输出读取到的数据
	      out.println(thisLine);
	      // 行号增加
	      i++;
	    }
	    out.flush();
	    out.close();
	    in.close();
	    // 删除原始文件
	    inFile.delete();
	    // 把临时文件改名为原文件名
	    outFile.renameTo(inFile);
	  }
	  
	 /**
	  * 在第�?��插入字符�?	  * @param inFile
	  *        文件
	  * @param lineInserted
	  * 		待增加的字符�?	  *        
	  */
	 public static void insertStringInHead(File inFile,String lineInserted)
	 {
		 try {
			insertStringInFile(inFile,1,lineInserted);
		} catch (Exception e) {
			// TODO 自动生成 catch �?			e.printStackTrace();
		}
	 }	  
	  
	 
	 /**
	  * 在文件尾部增加一�?	  * @param inFile
	  *        文件
	  * @param lineInserted
	  * 		待增加的字符�?	  *        
	  */
	 public static void insertStringInEnd(File inFile,String lineInserted)
	 {
		 try {  
			    // 打开�?��写文件器，构造函数中的第二个参数true表示以追加形式写文件  
			    FileWriter writer = new FileWriter(inFile, true);  
			    writer.write(lineInserted);  
			    writer.close();  
			} catch (IOException e) {  
			    e.printStackTrace();  
		} 
	 }
}
