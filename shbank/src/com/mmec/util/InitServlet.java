package com.mmec.util;

import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mmec.business.service.BaseService;
import com.mmec.sync.AbstractSyncTask;

/**
 * 初始化项目配 置 GlobalData类
 * 
 * @author Administrator
 * 
 */
public class InitServlet extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);

		// OCR注册验证身份服务地址
		ConstantParam.OcrIdentity_Endpoint = config.getInitParameter("OcrIdentity_Endpoint");

		// 验证二代身份证一所服务地址
		ConstantParam.IdentityCard_Endpoint = config.getInitParameter("IdentityCard_Endpoint");

		// 发送短信，先主通道后备用通道，取值1；如果先备用通道后主通道，取值非1
		ConstantParam.SendSMSFirstTrans = config.getInitParameter("SendSMSFirstTrans");

		// 合同路径
		ConstantParam.CONTRACT_PATH = config.getInitParameter("contractPath");
		// 创建合同时合同内容及附件的路径
		ConstantParam.CONTRACT_ATTACHMENT_PATH = config.getInitParameter("contractOrattachmentPath");
		// 中央承载系统IP地址
		ConstantParam.IP = config.getInitParameter("IP");
		// 中央承载系统PORT端口
		ConstantParam.PORT = Integer.parseInt(config.getInitParameter("PORT"));
		// ftp服务器ip
		ConstantParam.FTPIP = config.getInitParameter("FTPIP");
		// ftp服务器端口号
		ConstantParam.FTPPORT = Integer.parseInt(config.getInitParameter("FTPPORT"));
		// 上传图片路径
		ConstantParam.IMAGE_PATH = config.getInitParameter("IMAGE_PATH");

		// 邮箱服务器
		ConstantParam.EMAIL_SERVER_NAME = config.getInitParameter("EMAIL_SERVER_NAME");
		// 邮箱服务器用户名
		ConstantParam.EMAIL_FROM_USER = config.getInitParameter("EMAIL_FROM_USER");
		// 邮箱服务器密码
		ConstantParam.EMAIL_FROM_PWD = config.getInitParameter("EMAIL_FROM_PWD");
		// 硬件证书签署，灌章路径
		ConstantParam.GUANZHANG_PATH = config.getInitParameter("GUANZHANG_PATH");
		// 2.0合同路径
		ConstantParam.CONTRACT_PATH_OLD = config.getInitParameter("CONTRACT_PATH_OLD");
		// 对接地址
		ConstantParam.MMECPATH = config.getInitParameter("mmec_path");
		
		//签署成功备案接口
		ConstantParam.EXTERNALDATAIMPORT = config.getInitParameter("EXTERNALDATAIMPORT");
		//企查查key
		ConstantParam.KEY=config.getInitParameter("KEY");
		ServletContext servletContext = config.getServletContext();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);

		BaseService baseService = (BaseService) webApplicationContext.getBean("baseService");

		Timer timer = new Timer();
		AbstractSyncTask task = new AbstractSyncTask();
		task.setBaseService(baseService);
		timer.schedule(task, 1000, 2 * 60 * 1000);
	}
}
