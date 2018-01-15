package com.mmec.css.mmec;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.mmec.css.file.FileLoad;
import com.mmec.css.security.Base64;
/**
 * MMEC的集合包
 * @author liuy
 */
public class MMECFileLoad extends FileLoad{
	private final static Logger logger = Logger.getLogger(MMECFileLoad.class.getName()) ;
	public MMECFileLoad(HttpServletRequest request) {
		super(request);
	}

	/**
	 * 获取签名的String[],并按照合同规范中的排�?z_1 ... f_2
	 * data格式：z_1_主合同名�?SDFSDFSDFSDF&F_1_附属合同名称
	 * @return
	 */
	public 	List getListFromData()
	{
		String data=(String) getFromHashMap().get("data");
		logger.debug("getListFromData:"+data);
		try {
			data=new String(Base64.decode(data),"GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		logger.debug("getListFromData:"+data);
		String[] sl= data.split("&");
		List<FileNameRule> l=new ArrayList<FileNameRule>();
		for(int i=0;i<sl.length;i++)
		{
			String[] slp=sl[i].split("=");
			FileNameRule fileNameRule=new FileNameRule();
			int x=slp[0].lastIndexOf("_");
			fileNameRule.setFileFingerprint(slp[1]);
			fileNameRule.setFileName(slp[0].substring(x+1));
			fileNameRule.setFileNameHeade(slp[0].substring(0, x+1));
			l.add(fileNameRule);
		}
		return l;
	}
	
	/**
	 * 获取重新定义的名�?	 * @return
	 */
	public FileNameRule getRename(String fileName)
	{
		List l=getListFromData();
		for(int i=0;i<l.size();i++)
		{
			FileNameRule fileNameRule=(FileNameRule) l.get(i);
			if(fileNameRule.getFileName().endsWith(fileName))
			{
				return fileNameRule;
			}
		}
		return null;
	}
	
	/**
	 * 使用签名信息的规则对文件重新命名
	 * @return
	 */
	public void renameNameWrite(MFilePath mFilePath)
	{
		List l=getFileItemList();
		//写入的文件基本路�?	
		for(int i=0;i<l.size();i++)
 	    {
 	    	FileItem fileItem=(FileItem) l.get(i);
 	    	String fileName=fileItem.getName();
 	    	logger.debug(fileName);
 	    	//上传组建的id
 	    	String fieldName=fileItem.getFieldName();
 	    	int x=fieldName.lastIndexOf("_");
 	    	fieldName=fieldName.substring(0, x+1);
 	    	fileName=fileName.substring(fileName.lastIndexOf("\\")+1);
 	    	logger.debug(fileName);
 	    	if(StringUtils.isNotEmpty(fileName))
 	    	{
 		    	File f = new File(mFilePath.getLoadFilePath()+fieldName+fileName);
 		    	logger.debug(mFilePath.getLoadFilePath()+fieldName+fileName);
 	 	    	InputStream in;
 				try {
 					in = fileItem.getInputStream();
 					FileUtils.writeByteArrayToFile(f, IOUtils.toByteArray(in));
 				} catch (IOException e) {
 					e.printStackTrace();
 				}
 	    	}
 	    }
	}
}
