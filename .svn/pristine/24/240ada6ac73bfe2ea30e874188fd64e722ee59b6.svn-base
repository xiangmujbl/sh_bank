package com.mmec.css.mmec.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mmec.css.certdn.PKIFormInstance;
import com.mmec.css.certdn.form.CertForm;
import com.mmec.css.certdn.form.TSAFrom;
import com.mmec.css.mmec.MFilePath;
import com.mmec.css.mmec.element.WholeElement;
import com.mmec.css.mmec.form.ElementForm;
import com.mmec.css.mmec.form.ShowMessage;
import com.mmec.css.mmec.form.UserForm;
import com.mmec.css.mmec.service.MMECVerifyService;
import com.mmec.css.mmec.uints.MMECUints;
import com.mmec.css.mmec.uints.MMECVerify;

public class MMECVerifyServiceImpl implements MMECVerifyService{

	private final static Logger logger = Logger.getLogger(MMECVerifyServiceImpl.class.getName());
	public CertForm getCertForm(String certStr) {
		return PKIFormInstance.getInstance().getCertForm(certStr);
	}
	  
	/**
	 * 验证合同文件完整性，并返回结果，验证步骤如下：
	 * 1  验证合同
	 * 2  验证签名
	 * 3  验证服务端
	 * @param filePath  文件根路径
	 * @return
	 */
	public ShowMessage getContFileVF(String filePath) {
		ShowMessage showMessage=new ShowMessage();
		if(filePath==null||filePath.equals("")||filePath.length()==0)
		{
			showMessage.setCode("10001");
			showMessage.setDescription("file path is empty");
			return showMessage;
		}
		MFilePath mPath=new MFilePath();
		mPath.setBasePath(filePath);
		try {
			/**1 验证文档结构**/
			boolean b=MMECVerify.getInstance().isDocStruct(mPath);
			logger.debug("Document structure  is ok");
			if(b)
			{
				/**2 合同完整性验证**/
				b=MMECVerify.getInstance().getContCompleteVerif(mPath);
			}
			return MMECVerify.getInstance().getShowMessage();
		} catch (IllegalAccessException e) {
			logger.error(e);
			showMessage.setCode("11000");
			showMessage.setDescription(e.getMessage()+",for details, please see the log");
			return showMessage;
		} catch (InvocationTargetException e) {
			logger.error(e);
			showMessage.setCode("11000");
			showMessage.setDescription(e.getMessage()+",for details, please see the log");
			return showMessage;
		} catch (NoSuchMethodException e) {
			logger.error(e);
			showMessage.setCode("11000");
			showMessage.setDescription(e.getMessage()+",for details, please see the log");
			return showMessage;
		}
	}

	public TSAFrom getTSAFrom(String tsaStr) {
		return PKIFormInstance.getInstance().getTSAFrom(tsaStr);
	}

	public List<UserForm> getUserFormList(String sgPath) {
		List<UserForm>  userFormList=new ArrayList<UserForm>();
		WholeElement who=MMECUints.getInstance().discreteHeadAndData(sgPath);
		if(who==null)
		{
			return null;
		}
		for(ElementForm elForm : who.getElementList())
		{
			UserForm userForm=new UserForm();
			if(elForm.getCert()==null)
			{
				return null;
			}
			CertForm certForm=getCertForm(elForm.getCert());
			if(certForm==null)
			{
				return null;
			}
			
			if(elForm.getTimeStamp()==null)
			{
				return null;
			}
			TSAFrom  tsaFrom=getTSAFrom(elForm.getTimeStamp());
			if(tsaFrom==null)
			{
				return null;
			}
			userForm.setCertForm(certForm);
			userForm.setTsaFrom(tsaFrom);
			userFormList.add(userForm);
		}
		return userFormList;
	}
	
	/**
	 * 取合同标题和编号
	 * @param sgPath
	 * @return
	 */
	public Map<String,String> getTitel(String sgPath) {
		Map result = new HashMap<String,String>();
		
		WholeElement who=MMECUints.getInstance().discreteHeadAndData(sgPath);
		if(who==null)
		{
			return new HashMap<String,String>();
		}
		
		String title = (String) who.getHeadForm().get("Title");
		result.put("title",title);
		
		String contSerialNum = (String) who.getHeadForm().get("ContSerialNum");
		result.put("contSerialNum",contSerialNum);
		
		return result;
	}
}
