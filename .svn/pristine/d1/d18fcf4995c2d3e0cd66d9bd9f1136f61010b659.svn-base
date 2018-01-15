package com.mmec.css.mmec.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.mmec.css.articles.ProAssistant;
import com.mmec.css.certdn.form.CertForm;
import com.mmec.css.certdn.form.TSAFrom;
import com.mmec.css.conf.IConf;
import com.mmec.css.file.FileLoad;
import com.mmec.css.file.FolderUints;
import com.mmec.css.mmec.MFilePath;
import com.mmec.css.mmec.form.ShowMessage;
import com.mmec.css.mmec.form.UserForm;
import com.mmec.css.mmec.service.MMECVerifyService;
import com.mmec.css.mmec.service.impl.MMECVerifyServiceImpl;
import com.mmec.thrift.service.DataResult;
import com.mmec.thrift.service.ResultVerify;
import com.mmec.util.ByteToOther;
import com.mmec.util.SHA_MD;
import com.mmec.util.ThreadLocalMap;


public class MMECVerifyServlet extends HttpServlet{
	private static final long serialVersionUID = -3816037340327595484L;
	private final static Logger logger = Logger.getLogger(MMECVerifyServlet.class.getName()) ;
	
	
	private void sendForward(HttpServletRequest request,
			HttpServletResponse response,String sendPath) throws ServletException, IOException
	{
		RequestDispatcher dispatcher = request.getRequestDispatcher(sendPath); 
		dispatcher.forward(request, response);
	}
	
	/**
	 * 实现验证
	 * @throws IOException 
	 * @throws ServletException 
	 *
	 */
	public void getContFileVF(HttpServletRequest request,
			HttpServletResponse response,String fPath) throws ServletException, IOException
	{
		//验证
		MMECVerifyService mmecVerifyService=new MMECVerifyServiceImpl();
		ShowMessage showMessage=mmecVerifyService.getContFileVF(fPath);
		showMessage.setTime(ProAssistant.getNowTime());//日志的时�?		request.setAttribute("showMessage", showMessage);
		//解析用户证书
		MFilePath mPath=new MFilePath();
		mPath.setBasePath(fPath);
		
		List<UserForm>  userFormList= mmecVerifyService.getUserFormList(mPath.getUserGroupSignPath());	
		request.setAttribute("userFormList", userFormList);//日志的时�?		sendForward(request, response,"/jsp/mmecverify.jsp");
	}
	
	
	/**
	 * zip格式存储并解�?	 * @throws IOException 
	 * @throws ServletException 
	 * @return  成功后返�?解压后的文件名称
	 */
	private String unZip(HttpServletRequest request,
			HttpServletResponse response,String path) throws ServletException, IOException
	{
		FileLoad  fl=new FileLoad(request);
		fl.writeToPath(path);
		String zipFileName=fl.getFileNameList()[0];
		
		String fName=zipFileName.substring(0,zipFileName.lastIndexOf("."));
		File f=new  File(path+fName);
		if(f.exists())
		{
			try {
				FileUtils.deleteDirectory(f);
				logger.debug(fName+"：文件删除成功！");
			} catch (IOException e) {
				logger.error(fName+"：文件删除失",e);
				request.setAttribute("errMessage", fName+"：已存在，没有删除成功！");
				sendForward(request, response,"/err.jsp");
				return null;
			}
		}
		//解压文件
		try
		{
			FolderUints.unZipFolder(path+zipFileName);
		}
		catch (Exception e) {
			logger.error(path+zipFileName+"：解压失",e);
			request.setAttribute("errMessage", zipFileName+"：解压失败，请检查格式是否为zip格式");
			sendForward(request, response,"/err.jsp");
			return null;
		}
		return fName;
		
	}
	
	public void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException  
	{		
		
		String type=request.getParameter("type");
		String path=IConf.getValue("mmecUp");
		String fName=null;
		if(type.equals("zip"))
		{
			fName=unZip(request,response,path);
		}
		if(type.equals("name"))
		{
			fName=request.getParameter("fName");
		}
//		getContFileVF(request,response,path+fName);
		getContFileVF(request,response,fName);
	}
	
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException 
	{
			doGet(request,response);
	}
	
	/**
	 * 实现验证
	 * @throws IOException 
	 * @throws ServletException 
	 *
	 */
	public ShowMessage getContFileVF(String fPath) throws ServletException, IOException
	{
		//验证
		MMECVerifyService mmecVerifyService=new MMECVerifyServiceImpl();
		ShowMessage showMessage=mmecVerifyService.getContFileVF(fPath);
		showMessage.setTime(ProAssistant.getNowTime());
		
		ResultVerify rv = (ResultVerify) ThreadLocalMap.get(Thread.currentThread().getId());
		rv.setStatus(showMessage.getCode());
		rv.setDesc(showMessage.getDescription());
		
		logger.info("当前线程： " + Thread.currentThread().getId());
		return showMessage;
	}
	
	/**
	 * 取合同标题和编号
	 * @throws IOException 
	 * @throws ServletException 
	 *
	 */
	public void getSerialNumAndTitle(String fPath) throws ServletException, IOException
	{
		// 局部变量
		ResultVerify rv = (ResultVerify) ThreadLocalMap.get(Thread.currentThread().getId());
		
		// 验证
		MMECVerifyService mmecVerifyService=new MMECVerifyServiceImpl();
		
		MFilePath mPath=new MFilePath();
		mPath.setBasePath(fPath);
		
		// 解析合同标题和编号
		Map<String,String> map = mmecVerifyService.getTitel(mPath.getUserGroupSignPath());	
		
		// 封装合同标题和编号
		if (null == rv.getData()){
			DataResult dr = new DataResult();
			dr.setTitle(map.get("title"));
			dr.setContSerialNum(map.get("contSerialNum"));
			rv.setData(dr);
		}else{
			rv.data.setTitle(map.get("title"));
			rv.data.setContSerialNum(map.get("contSerialNum"));
		}
		
		
		//解析用户证书
		List<UserForm>  userFormList= mmecVerifyService.getUserFormList(mPath.getUserGroupSignPath());	
		
		if(null == userFormList){
			return ;
		}
		
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		
		// 遍历所有用户信息组
		for(UserForm uf : userFormList){
			// 缓存缔约公司和时间
			Map<String, String> rmap = new HashMap();
			
			CertForm cf = uf.getCertForm();
			String[] ss = cf.getSubjectDN().split(",");
			for(String temp : ss){
				if(!temp.contains("CN=")){
					continue;
				}
				
				// 封装缔约人信息
				rmap.put("cn", temp.replace("CN=",""));
			}

			TSAFrom tf = uf.getTsaFrom();
			
			// 封装缔约人信息
			rmap.put("time", tf.getTsaTime());
			
			list.add(rmap);
		}
		
		logger.info("当前线程： " + Thread.currentThread().getId());
		
		// 封装数据保存到局部缓存中
		rv.getData().signer = list;
	}
	
	/**
	 * 验签包散列值
	 * @param fPath
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getZipSHA1(String fPath) throws ServletException, IOException
	{
		logger.info("当前线程： " + Thread.currentThread().getId());
		
		// 局部变量
		ResultVerify rv = (ResultVerify) ThreadLocalMap.get(Thread.currentThread().getId());
		
		File file = new File(fPath);
		
		ByteToOther bo= SHA_MD.encodeFileSHA1(file);
		
		// 单位KB
		if (null == rv.getData()){
			DataResult dr = new DataResult();
			dr.setZipSize((new Long(file.length()/1024)).toString());
			dr.setZipSha1(bo.toHexString());
			rv.setData(dr);
		}else{
			rv.data.setZipSize((new Long(file.length()/1024)).toString());
			rv.data.setZipSha1(bo.toHexString());
		}
		
	}
}
