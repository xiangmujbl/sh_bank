package com.mmec.css.mmec.uints;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import com.mmec.css.articles.ProAssistant;
import com.mmec.css.mmec.element.MHeadAndBody;
import com.mmec.css.mmec.element.MMECElement;
import com.mmec.css.mmec.form.ShowMessage;
import com.mmec.util.StringUtil;

/**
 * 实现《签名验证规范�?中读取txt和sg文件的名称及内容验证
 * 验证规则参�?：�?Txt及sg验证规则.mmap�? * @author liuy
 * @version 2014-1-24
 *
 */

public class MMECVerifyRules {
	
//	private final static Logger logger = Logger.getLogger(ValidaRules.class.getName()) ;
	private ShowMessage showMessage=new ShowMessage();
	private MHeadAndBody mHeadAndBody=new MHeadAndBody();
	public MHeadAndBody getMHeadAndBody() {
		return mHeadAndBody;
	}

	public void setMHeadAndBody(MHeadAndBody headAndBody) {
		mHeadAndBody = headAndBody;
	}

	public ShowMessage getShowMessage() {
		return showMessage;
	}

	public void setShowMessage(ShowMessage showMessage) {
		this.showMessage = showMessage;
	}
	
	/**
	 * 扩展头验证，参�?《txt及sg验证规则�?-延伸
	 * 1 头文件必须要含有“ContSerialNum
	 * 2 头文件不允许使用，Cert，Signature，TimeStamp
	 * @param row   每一行头的验证内�?	 * @return
	 */
	private boolean expandHead(String fName,String row)
	{
		String[] sl={MMECElement.cert,MMECElement.signature,
				MMECElement.signature,MMECElement.name,MMECElement.sha1Digest};
		int i=row.indexOf(":");
		row=row.substring(0,i);
		for(int y=0;y<sl.length;y++)
		{
			if(row.toLowerCase().equals(sl[y].toLowerCase()))
			{
				showMessage.setCode("10012");
 				showMessage.setDescription(fName+" ["+row+"]:Not allowed in the header");
 				showMessage.setTime(ProAssistant.getNowTime());
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 扩展body验证，参考�?txt及sg验证规则�?-延伸
	 * @param body   待验证的主体内容
	 * @return
	 */
	private boolean expand(String fName,StringBuffer body)
	{
		String[] bodyList= body.toString().split("&");
		//txt规则
		if(fName.endsWith(".txt"))
		{
			for(int i=0;i<bodyList.length;i++)
			{
				//奇数为开头为：SHA1-Digest，偶数为：Name
				if(i%2==0)
				{
					if(bodyList[i].startsWith(MMECElement.name))
					{
						continue;
					}
					else
					{
	            		showMessage.setCode("10007");
	     				showMessage.setDescription(fName+" ["+bodyList[i]+ "]:is err,it must be "+MMECElement.name+" begin");
	     				showMessage.setTime(ProAssistant.getNowTime());
						return false;
					}
				}
				else
				{
					if(bodyList[i].startsWith(MMECElement.sha1Digest))
					{
						continue;
					}
					else
					{
	            		showMessage.setCode("10008");
	            		showMessage.setDescription(fName+" ["+bodyList[i]+ "]:is err,it must be "+MMECElement.sha1Digest+" begin");
	     				showMessage.setTime(ProAssistant.getNowTime());
						return false;
					}
				}
			}
		}
		if(fName.endsWith("UserGroupSign.sg"))
		{
			for(int i=0;i<bodyList.length;i++)
			{
				//Cert，Signature，TimeStamp次序
				if(i%3==0)
				{
					if(bodyList[i].startsWith(MMECElement.cert))
					{
						continue;
					}
					else
					{
	            		showMessage.setCode("10009");
	            		showMessage.setDescription(fName+" ["+bodyList[i]+ "]:is err,it must be "+MMECElement.cert+" begin");
	     				showMessage.setTime(ProAssistant.getNowTime());
						return false;
					}
				}
				if(i%3==1)
				{
					if(bodyList[i].startsWith(MMECElement.signature))
					{
						continue;
					}
					else
					{
	            		showMessage.setCode("10010");
	            		showMessage.setDescription(fName+" ["+bodyList[i]+ "]:is err,it must be "+MMECElement.signature+" begin");
	     				showMessage.setTime(ProAssistant.getNowTime());
						return false;
					}
				}
				if(i%3==2)
				{
					if(bodyList[i].startsWith(MMECElement.timeStamp))
					{
						continue;
					}
					else
					{
	            		showMessage.setCode("10011");
	            		showMessage.setDescription(fName+" ["+bodyList[i]+ "]:is err,it must be "+MMECElement.timeStamp+" begin");
	     				showMessage.setTime(ProAssistant.getNowTime());
						return false;
					}
				}
			}
//		if(fName.endsWith("ServerSign.sg"))
//		{
//			for(int i=0;i<bodyList.length;i++)
//			{
//				if(bodyList[i].startsWith(MMECElement.timeStamp))
//				{
//					continue;
//				}
//				else
//				{
//					return false;
//				}
//			}
//		}
	  }
	  mHeadAndBody.setCont(body.toString());
	  return true;
	}
	
	/**
	 * 基础验证，参考�?txt及sg验证规则�?-基础
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public  boolean BaseVF(String path) throws IOException
	{ 
		 File f=new File(path);
		 FileReader read = new FileReader(f);
         BufferedReader br = new BufferedReader(read);
         StringBuffer header=new StringBuffer();
         StringBuffer body=new StringBuffer();
         boolean flag=true;
         String row;
         while ((row = br.readLine()) != null) 
         {  	 
        	 if(row.isEmpty())
        	 {
        		 flag=false;
        	 }
        	 else
        	 {
        		 //验证":"是否唯一
        		 int y=StringUtil.calChar(row,':');
            	 if(y!=1)
            	 {
            		showMessage.setCode("10002");
     				showMessage.setDescription(f.getName()+"["+row+"] is Wrong format,it have "+y+" ':' symbol");
     				showMessage.setTime(ProAssistant.getNowTime());
            		return false; 
            	 }  
        		 y=StringUtil.calChar(row,'&');
            	 if(y>0)
            	 {
            		showMessage.setCode("10006");
     				showMessage.setDescription(f.getName()+"["+row+"] is Wrong format,it have '&' symbols");
     				showMessage.setTime(ProAssistant.getNowTime());
            		return false; 
            	 } 
        		 if(flag)
        		 {
        			 boolean bp= expandHead(f.getName(),row);
        			 if(bp)
        			 {
            			 header.append(row); 
            			 header.append("&"); 
        			 }
        			 else
        			 {
        				 return false;
        			 }
        		 }
        		 else
        		 {
        			 body.append(row); 
        			 body.append("&");
        		 }
        	 }
         }
         //关闭连接
         br.close();
         read.close();
         
         if(header.length()==0)
         {
        		showMessage.setCode("10003");
 				showMessage.setDescription(f.getName()+" header is null");
 				showMessage.setTime(ProAssistant.getNowTime());
        		return false; 
         }
         if(body.length()==0)
         {
     		showMessage.setCode("10004");
			showMessage.setDescription(f.getName()+" body is null");
			showMessage.setTime(ProAssistant.getNowTime());
    		return false; 
         }
         
         //验证头文件是否含�?ContSerialNum:"元素
         int i=header.indexOf(MMECElement.contSerialNum);
         if(i==-1)
         {
      		showMessage.setCode("10005");
			showMessage.setDescription("ContSerialNum is not exist");
			showMessage.setTime(ProAssistant.getNowTime());
        	return false;
         }
         //验证扩展内容
         boolean b=expand(f.getName(),body);
         if(b)
         {
       		showMessage.setCode("0");
			showMessage.setDescription("ok");
			showMessage.setTime(ProAssistant.getNowTime());
			mHeadAndBody.setHead(header.toString());
        	return true;
         }
         else
         {
        	 return false;
         }
	}
}
