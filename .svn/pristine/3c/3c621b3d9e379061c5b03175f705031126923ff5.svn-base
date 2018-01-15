package com.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.protocol.Protocol;

import com.PictureAndBase64;
import com.bean.CallWebServiceUtil;
import com.bean.GlobalUtil;
import com.bean.MD5Util;
import com.bean.MyProtocolSocketFactory;
import com.google.gson.Gson;

/**
 * 注册接口演示 Servlet implementation class RegisterTest
 */
public class RegisterWithCheckIdcard extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterWithCheckIdcard() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");

		// 参数装填
		String appId = "";
		String signType = "";
		String type = "";
		String isAdmin = "";
		String userId = "";
		String userName = "";
		String cardType = "";
		String cardNum = "";
		String mobile = "";
		String email = "";
		String licenseNo = "";
		String companyName = "";
		String companyType = "";
		String appSecretKey = "";
		String phoneNumber = "";
		// 参数装填
		String idCardPicA = "";
		String idCardPicB = "";
		String licensePic = "";
		String proxyPic = "";

		File file = null;
		int maxFileSize = 5000 * 1024;
		int maxMemSize = 5000 * 1024;

		String IMGPath = GlobalUtil.IMGPath;

		// 验证上传内容类型
		String contentType = request.getContentType();
		if ((contentType.indexOf("multipart/form-data") >= 0)) {

			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置内存中存储文件的最大值
			factory.setSizeThreshold(maxMemSize);

			// 创建一个新的文件上传处理程序
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 设置最大上传的文件大小
			upload.setSizeMax(maxFileSize);
			try {
				// 解析获取的文件
				List fileItems = upload.parseRequest(request);

				// 处理上传的文件
				Iterator i = fileItems.iterator();

				while (i.hasNext()) {
					FileItem fi = (FileItem) i.next();
					if (!fi.isFormField()) {
						// 获取上传文件的参数
						String fieldName = fi.getFieldName();
						String fileName = fi.getName();
						// boolean isInMemory = fi.isInMemory();
						// long sizeInBytes = fi.getSize();

						if (fieldName.equals("IdCardFront")) {
							// 写入文件
							if (!fileName.equals("")) {
								if (fileName.lastIndexOf("\\") >= 0) {
									file = new File(IMGPath
											+ fileName.substring(fileName.lastIndexOf("\\")));
								} else {
									file = new File(IMGPath
											+ fileName.substring(fileName.lastIndexOf("\\") + 1));
								}
								fi.write(file);
								idCardPicA = PictureAndBase64.GetImageStr(file.getAbsolutePath());
							}
						}
						if (fieldName.equals("IdCardBack")) {
							// 写入文件
							if (!fileName.equals("")) {
								if (fileName.lastIndexOf("\\") >= 0) {
									file = new File(IMGPath
											+ fileName.substring(fileName.lastIndexOf("\\")));
								} else {
									file = new File(IMGPath
											+ fileName.substring(fileName.lastIndexOf("\\") + 1));
								}
								fi.write(file);
								idCardPicB = PictureAndBase64.GetImageStr(file.getAbsolutePath());
							}
						}
						if (fieldName.equals("BusinessLicense")) {
							// 写入文件
							if (!fileName.equals("")) {
								if (fileName.lastIndexOf("\\") >= 0) {
									file = new File(IMGPath
											+ fileName.substring(fileName.lastIndexOf("\\")));
								} else {
									file = new File(IMGPath
											+ fileName.substring(fileName.lastIndexOf("\\") + 1));
								}
								fi.write(file);
								licensePic = PictureAndBase64.GetImageStr(file.getAbsolutePath());
							}
						}
						if (fieldName.equals("Attorney")) {
							// 写入文件
							if (!fileName.equals("")) {
								if (fileName.lastIndexOf("\\") >= 0) {
									file = new File(IMGPath
											+ fileName.substring(fileName.lastIndexOf("\\")));
								} else {
									file = new File(IMGPath
											+ fileName.substring(fileName.lastIndexOf("\\") + 1));
								}
								fi.write(file);
								proxyPic = PictureAndBase64.GetImageStr(file.getAbsolutePath());
							}
						}
					} else {
						if (fi.getFieldName().equals("appId")) {
							appId = fi.getString();
						} else if (fi.getFieldName().equals("signType")) {
							signType = fi.getString();
						} else if (fi.getFieldName().equals("type")) {
							type = fi.getString();
						} else if (fi.getFieldName().equals("isAdmin")) {
							isAdmin = fi.getString();
						} else if (fi.getFieldName().equals("userId")) {
							userId = fi.getString();
						} else if (fi.getFieldName().equals("userName")) {
							userName = fi.getString();
							userName = new String(userName.getBytes("ISO-8859-1"), "UTF-8");
						} else if(fi.getFieldName().equals("cardType")){
							cardType = fi.getString();
						}else if (fi.getFieldName().equals("cardNum")) {
							cardNum = fi.getString();
						} else if (fi.getFieldName().equals("mobile")) {
							mobile = fi.getString();
						} else if (fi.getFieldName().equals("email")) {
							email = fi.getString();
						} else if (fi.getFieldName().equals("licenseNo")) {
							licenseNo = fi.getString();
						} else if (fi.getFieldName().equals("companyName")) {
							companyName = fi.getString();
							companyName = new String(companyName.getBytes("ISO-8859-1"), "UTF-8");
						} else if (fi.getFieldName().equals("companyType")) {
							companyType = fi.getString();
							companyType = new String(companyType.getBytes("ISO-8859-1"), "UTF-8");
						} else if (fi.getFieldName().equals("app_key")) {
							appSecretKey = fi.getString();
						} else if (fi.getFieldName().equals("phoneNumber")) {
							phoneNumber = fi.getString();
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		Date needdate = new Date();
		long needtime = needdate.getTime();
		String time = needtime + "";
		Map<String, String> map = new HashMap<String, String>();
		Gson gson = new Gson();
		map.put("type", type);
		map.put("isAdmin", isAdmin);
		map.put("userId", userId);
		map.put("userName", userName);
		map.put("cardType", cardType);
		map.put("cardNum", cardNum);
		map.put("mobile", mobile);
		map.put("email", email);
		map.put("licenseNo", licenseNo);
		map.put("companyName", companyName);
		map.put("companyType", companyType);
		map.put("phoneNumber", phoneNumber);
		map.put("idCardPicA", idCardPicA);
		map.put("idCardPicB", idCardPicB);
		map.put("licensePic", licensePic);
		map.put("proxyPic", proxyPic);

		List list = new ArrayList();
		list.add(map);
		String info = gson.toJson(list);

		// md5拼接，校验
		String md5str = appId + "&" + info + "&" + time;
		String md5str1 = md5str + "&" + appSecretKey;
		String sign = MD5Util.MD5Encode(md5str1, "UTF-8");
		String endpoint = GlobalUtil.endpoint_addition;
		//使用ServiceClient调用https的WebService
//		ServiceClient serviceClient = new ServiceClient();
//
//		MyProtocolSocketFactory socketfactory = new MyProtocolSocketFactory();
//
//		Protocol protocol = new Protocol("https", socketfactory, 443);
//		
//		Options options = serviceClient.getOptions();
//		
//		options.setProperty(HTTPConstants.CUSTOM_PROTOCOL_HANDLER, protocol);
//		
//		//设置调用地址
//		OMElement ret = null;
//        EndpointReference targetEPR = new EndpointReference(endpoint);
//        options.setTo(targetEPR);
//        serviceClient.setOptions(options);
//        OMFactory fac = OMAbstractFactory.getOMFactory();
//        //设置命名空间
//        OMNamespace omNs = fac.createOMNamespace("http://wsdl.com/","registerWithCheckIdcard");
//        OMElement eleData = fac.createOMElement("registerWithCheckIdcard", omNs);
//        //对应参数的节点
//        String[] strs = new String[] { "appId", "time", "sign", "signType", "info" };
//        //参数值
//        String[] val = new String[] {appId, time, sign, signType, info};
//
//        //添加参数值
//        OMNamespace emOmNs = fac.createOMNamespace("","");        
//        for (int i = 0; i < strs.length; i++) {
//            OMElement inner = fac.createOMElement(strs[i], emOmNs);
//            inner.setText(val[i]);
//            eleData.addChild(inner);
//        }
//        //发送soap请求
//        ret = serviceClient.sendReceive(eleData);
//        
//        //解析返回值
//        Iterator<?> iterator=ret.getChildElements(); 
//        String result = "";
//        while(iterator.hasNext()){
//        	OMNode omNode = (OMNode) iterator.next();
//        	if(omNode.getType()==OMNode. ELEMENT_NODE){
//	        	OMElement omElement = (OMElement) omNode;
//	        	if (omElement.getLocalName().toLowerCase().equals("return" )) {
//	        		result = omElement.getText();
//	        	}
//	        }
//        }
//        
		 String[] paramName = new String[] { "appId", "time", "sign", "signType", "info" };
		    
	        String[] paramValue = new String[] {appId, time, sign, signType, info};

			String result = CallWebServiceUtil.CallHttpsService(endpoint, "http://wsdl.com/", "registerWithCheckIdcard", paramName, paramValue);
		
		System.out.println(result);
		request.getSession().setAttribute("result", result);
		response.sendRedirect("jsp/result.jsp");// 跳转至结果页
//		Service service = new Service();
//		try {
//			// 接口调用
//			Call call = (Call) service.createCall();
//			call.setTargetEndpointAddress(new java.net.URL(endpoint));
//			call.setOperationName(new QName("http://wsdl.com/", "registerWithCheckIdcard"));
//
//			call.addParameter("appId", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("time", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("sign", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("signType", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("info", XMLType.XSD_STRING, ParameterMode.IN);
//			call.setReturnType(XMLType.XSD_STRING);
//
//			String result = call.invoke(new Object[] { appId, time, sign, signType, info })
//					.toString();
//			System.out.println(result);
//			request.getSession().setAttribute("result", result);
//			response.sendRedirect("jsp/result.jsp");// 跳转至结果页
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
	}
}
