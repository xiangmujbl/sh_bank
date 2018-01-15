package com.test;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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

/**
 * Servlet implementation class CustomLogo
 */
public class CustomLogo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CustomLogo() {
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String appId = "";
		String signType = "";
		String userId = "";
		String width = "";
		String height = "";
		String app_key = "";
		String IMGPath = GlobalUtil.IMGPath;
		int maxFileSize = 2 * 1024 * 1024;
		int maxMemSize = 2 * 1024 * 1024;
		File file = null;
		String base64 = "";
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

			// 解析获取的文件
			List fileItems;
			try {
				fileItems = upload.parseRequest(request);

				// 处理上传的文件
				Iterator i = fileItems.iterator();

				while (i.hasNext()) {
					FileItem fi = (FileItem) i.next();
					if (!fi.isFormField()) {
						String fieldName = fi.getFieldName();
						String fileName = fi.getName();

						if (fieldName.equals("image")) {
							if (!fileName.equals("")) {
								if (fileName.lastIndexOf("\\") >= 0) {
									file = new File(IMGPath
											+ fileName.substring(fileName.lastIndexOf("\\")));
								} else {
									file = new File(IMGPath
											+ fileName.substring(fileName.lastIndexOf("\\") + 1));
								}

								fi.write(file);

							}
							base64 = PictureAndBase64.GetImageStr(file.getAbsolutePath());
						}
					} else {
						if (fi.getFieldName().equals("appId")) {
							appId = fi.getString();
						} else if (fi.getFieldName().equals("userId")) {
							userId = fi.getString();
						} else if (fi.getFieldName().equals("width")) {
							width = fi.getString();
						} else if (fi.getFieldName().equals("height")) {
							height = fi.getString();
						} else if (fi.getFieldName().equals("signType")) {
							signType = fi.getString();
						} else if (fi.getFieldName().equals("app_key")) {
							app_key = fi.getString();
						}
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Date needdate = new Date();
		long needtime = needdate.getTime();
		String time = needtime + "";
		String md5 = appId + "&" + height + "&" + time + "&" + userId + "&" + width + "&" + app_key;
		String sign = MD5Util.MD5Encode(md5, "UTF-8");
		String endpoint = GlobalUtil.endpoint_addition;
		//使用ServiceClient调用https的WebService
				/*ServiceClient serviceClient = new ServiceClient();

				MyProtocolSocketFactory socketfactory = new MyProtocolSocketFactory();

				Protocol protocol = new Protocol("https", socketfactory, 443);
				
				Options options = serviceClient.getOptions();
				
				options.setProperty(HTTPConstants.CUSTOM_PROTOCOL_HANDLER, protocol);
				
				//设置调用地址
				OMElement ret = null;
		        EndpointReference targetEPR = new EndpointReference(endpoint);
		        options.setTo(targetEPR);
		        serviceClient.setOptions(options);
		        OMFactory fac = OMAbstractFactory.getOMFactory();
		        //设置命名空间
		        OMNamespace omNs = fac.createOMNamespace("http://wsdl.com/","customLogo");
		        OMElement eleData = fac.createOMElement("customLogo", omNs);
		        //对应参数的节点
		        String[] strs = new String[] { "appId", "time", "sign", "signType", "userId","base64","width","height" };
		        //参数值
		        String[] val = new String[] {appId, time, sign, signType, userId, base64, width, height};

		        //添加参数值
		        OMNamespace emOmNs = fac.createOMNamespace("","");        
		        for (int i = 0; i < strs.length; i++) {
		            OMElement inner = fac.createOMElement(strs[i], emOmNs);
		            inner.setText(val[i]);
		            eleData.addChild(inner);
		        }
		        //发送soap请求
		        ret = serviceClient.sendReceive(eleData);
		        
		        //解析返回值
		        Iterator<?> iterator=ret.getChildElements(); 
		        String result = "";
		        while(iterator.hasNext()){
		        	OMNode omNode = (OMNode) iterator.next();
		        	if(omNode.getType()==OMNode. ELEMENT_NODE){
			        	OMElement omElement = (OMElement) omNode;
			        	if (omElement.getLocalName().toLowerCase().equals("return" )) {
			        		result = omElement.getText();
			        	}
			        }
		        }
		        */
		 String[] paramName = new String[] { "appId", "time", "sign", "signType", "userId","base64","width","height" };
		    
	        String[] paramValue = new String[] {appId, time, sign, signType, userId, base64, width, height};

			String result = CallWebServiceUtil.CallHttpsService(endpoint, "http://wsdl.com/", "customLogo", paramName, paramValue);
		
				System.out.println(result);
				request.getSession().setAttribute("result", result);
				response.sendRedirect("jsp/result.jsp");// 跳转至结果页
//		Service service = new Service();
//		try {
//			// 接口调用
//			Call call = (Call) service.createCall();
//			call.setTargetEndpointAddress(new java.net.URL(endpoint));
//			call.setOperationName(new QName("http://wsdl.com/", "customLogo"));
//
//			call.addParameter("appId", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("time", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("sign", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("signType", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("userId", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("base64", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("width", XMLType.XSD_INT, ParameterMode.IN);
//			call.addParameter("height", XMLType.XSD_INT, ParameterMode.IN);
//			call.setReturnType(XMLType.XSD_STRING);
//			String result = call.invoke(
//					new Object[] { appId, time, sign, signType, userId, base64, width, height })
//					.toString();
//			System.out.println(result);
//			request.getSession().setAttribute("result", result);
//			response.sendRedirect("jsp/result.jsp");// 跳转至结果页
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}

	}
}
