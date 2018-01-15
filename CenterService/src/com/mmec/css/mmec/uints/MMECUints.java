package com.mmec.css.mmec.uints;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import com.mmec.css.mmec.element.MHeadAndBody;
import com.mmec.css.mmec.element.WholeElement;
import com.mmec.css.mmec.form.ElementForm;
import com.mmec.css.mmec.form.ShowMessage;

public  class MMECUints {
	
	private final static Logger logger = Logger.getLogger(MMECUints.class.getName()) ;
	private static MMECUints mmecUints = null; 
	private  ShowMessage showMessage=new ShowMessage();
    public ShowMessage getShowMessage() {
		return showMessage;
	}
	public void setShowMessage(ShowMessage showMessage) {
		this.showMessage = showMessage;
	}
	private MMECUints() {
    }
    public static MMECUints getInstance() {
       if(mmecUints == null) {
    	   mmecUints = new MMECUints();
       }
       return mmecUints;
    }
	
    private String readBody(String data)
    {
    	int i=data.indexOf(":");
    	return data.substring(i+1,data.length());
    }
    /**
     * 读取正文内容
     * @param body
     * @param fPath  文件路径   
     *       1  读取txt文件
     *       2  读取sg文件   
     * @return
     */
	private  List<ElementForm>  readData(String body,String fPath)
	{
		List<ElementForm> l=new ArrayList<ElementForm>();
		String[] strList=body.split("&");
		ElementForm elForm=new ElementForm();
		if(fPath.endsWith(".txt"))
		{
			for(int i=0;i<strList.length;i++)
			{
				if(i%2==0)
				{
					elForm.setName(readBody(strList[i]));
				}
				if(i%2==1)
				{
					elForm.setSha1Digest(readBody(strList[i]));
					l.add(elForm);
					elForm=new ElementForm();
				}
			}
		}
		if(fPath.endsWith("UserGroupSign.sg"))
		{
			for(int i=0;i<strList.length;i++)
			{
				if(i%3==0)
				{
					
					elForm.setCert(readBody(strList[i]));
				}
				if(i%3==1)
				{
					elForm.setSignature(readBody(strList[i]));
				}
				if(i%3==2)
				{
					elForm.setTimeStamp(readBody(strList[i]));
					l.add(elForm);
					elForm=new ElementForm();
				}
			}
		}
		return l;
	}
	/**
	 * 读取MMEC中的文件，分离头和内容，返回相应内容
	 * @throws IOException 
	 * @throws IOException 
	 *
	 */
	public  WholeElement discreteHeadAndData(String filePath)
	{
		MMECVerifyRules v=new MMECVerifyRules();
		//执行验证规则
		boolean b=false;
		try 
		{
			b = v.BaseVF(filePath);
		} 
		catch (IOException e) 
		{
			showMessage.setCode("10001");
			showMessage.setDescription(filePath+" is not exist");
			MMECUints.getInstance().setShowMessage(showMessage);
			logger.error(filePath+ "is not exist",e);
			return null;
		}
		if(!b)
		{
			ShowMessage showMessage=v.getShowMessage();
			MMECUints.getInstance().setShowMessage(showMessage);
			return null;
		}
		MHeadAndBody mHeadAndBody=v.getMHeadAndBody();
		//读取头文�?		
		String head=mHeadAndBody.getHead();
		HashMap<String, String> hash=new HashMap<String, String>();
		String[] headList= head.split("&");
		for(int i=0;i<headList.length;i++)
		{
			String[] headListp=headList[i].split(":");
			if(headListp.length==1)
			{
				hash.put(headListp[0], " ");
			}
			if(headListp.length==2)
			{
				hash.put(headListp[0], headListp[1]);
			}
		}
		//读取body
		String body=mHeadAndBody.getCont();
		List<ElementForm>  entList=readData(body,filePath);
		//返回完整的读取内�?		
		WholeElement who=new WholeElement();
		who.setHeadForm(hash);
		who.setElementList(entList);
		return who;
	}
}
