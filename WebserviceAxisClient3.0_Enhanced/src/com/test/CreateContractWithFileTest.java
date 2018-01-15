package com.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
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
 * Servlet implementation class CreateContractWithFile
 */
public class CreateContractWithFileTest extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateContractWithFileTest() {
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

		request.setCharacterEncoding("utf-8");
		String appId = "";
		String signType = "";
		String userId = "";
		String customsId = "";
		String orderId = "";
		String title = "";
		String offerTime = "";
		String templateId = "";
		String data = "";
		String appSecretKey = "";
		String attachFileName1 = "";
		String attachFileName2 = "";
		String attachFileName3 = "";
		String attachFileName4 = "";
		String attachFileName5 = "";
		String attachmentBase64 = "";
		List attachment = new ArrayList();

		String filePath = GlobalUtil.IMGPath;
		
		File file = null;
		// int maxFileSize = 50000 * 1024;
		// int maxMemSize = 50000 * 1024;

		// 验证上传内容类型
		String contentType = request.getContentType();
		if (contentType.indexOf("multipart/form-data") >= 0) {
			try {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				// 设置内存中存储文件的最大值
				// factory.setSizeThreshold(maxMemSize);

				// 创建一个新的文件上传处理程序
				ServletFileUpload upload = new ServletFileUpload(factory);
				// 设置最大上传的文件大小
				upload.setFileSizeMax(1024 * 1024 * 1024);
				// upload.setSizeMax(maxFileSize);
				List list = new ArrayList();

				List fileItems = upload.parseRequest(request);

				// 处理上传的文件
				Iterator i = fileItems.iterator();
				String fieldName="";
				String fileName="";
				while (i.hasNext()) {
					FileItem fi = (FileItem) i.next();
					
						if (!fi.isFormField()) {
						// 获取上传文件的参数
						fieldName = fi.getFieldName();
						fileName = fi.getName();

						if (fieldName.equals("attachFile1")) {
							Map map = new HashMap();
							
							// 写入文件
							if (!fileName.equals("")) {
								if (fileName.lastIndexOf("\\") >= 0) {
									file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\")));
								} else {
									file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\") + 1));
								}
								
								fi.write(file);
								attachmentBase64 = PictureAndBase64.GetImageStr(file.getAbsolutePath());
								attachment.add(attachmentBase64);
								map.put("attachmentName", attachFileName1);
								System.out.println(attachFileName1);
								map.put("attachmentBase64", attachmentBase64);
								list.add(map);
							} else {
								if (!attachFileName1.equals("")) {
									map.put("attachmentName", attachFileName1);
									map.put("attachmentBase64", "");
									list.add(map);
								}
							}

						}
						if (fieldName.equals("attachFile2")) {
							Map map = new HashMap();
							// 写入文件
							if (!fileName.equals("")) {
								if (fileName.lastIndexOf("\\") >= 0) {
									file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\")));
								} else {
									file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\") + 1));
								}
								fi.write(file);
								attachmentBase64 = PictureAndBase64.GetImageStr(file.getAbsolutePath());
								attachment.add(attachmentBase64);
								map.put("attachmentName", attachFileName2);
								map.put("attachmentBase64", attachmentBase64);
								list.add(map);
							} else {
								if (!attachFileName2.equals("")) {
									map.put("attachmentName", attachFileName2);
									map.put("attachmentBase64", "");
									list.add(map);
								}
							}

						}
						if (fieldName.equals("attachFile3")) {
							Map map = new HashMap();
							// 写入文件
							if (!fileName.equals("")) {
								if (fileName.lastIndexOf("\\") >= 0) {
									file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\")));
								} else {
									file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\") + 1));
								}
								fi.write(file);
								attachmentBase64 = PictureAndBase64.GetImageStr(file.getAbsolutePath());
								attachment.add(attachmentBase64);
								map.put("attachmentName", attachFileName3);
								map.put("attachmentBase64", attachmentBase64);
								list.add(map);
							} else {
								if (!attachFileName3.equals("")) {
									map.put("attachmentName", attachFileName3);
									map.put("attachmentBase64", "");
									list.add(map);
								}
							}

						}
						if (fieldName.equals("attachFile4")) {
							Map map = new HashMap();
							// 写入文件
							if (!fileName.equals("")) {
								if (fileName.lastIndexOf("\\") >= 0) {
									file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\")));
								} else {
									file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\") + 1));
								}
								fi.write(file);
								attachmentBase64 = PictureAndBase64.GetImageStr(file.getAbsolutePath());
								attachment.add(attachmentBase64);
								map.put("attachmentName", attachFileName4);
								map.put("attachmentBase64", attachmentBase64);
								list.add(map);
							} else {
								if (!attachFileName4.equals("")) {
									map.put("attachmentName", attachFileName4);
									map.put("attachmentBase64", "");
									list.add(map);
								}
							}

						}
						if (fieldName.equals("attachFile5")) {
							Map map = new HashMap();
							// 写入文件
							if (!fileName.equals("")) {
								if (fileName.lastIndexOf("\\") >= 0) {
									file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\")));
								} else {
									file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\") + 1));
								}
								fi.write(file);
								attachmentBase64 = PictureAndBase64.GetImageStr(file.getAbsolutePath());
								attachment.add(attachmentBase64);
								map.put("attachmentName", attachFileName5);
								map.put("attachmentBase64", attachmentBase64);
								list.add(map);
							} else {
								if (!attachFileName5.equals("")) {
									map.put("attachmentName", attachFileName5);
									map.put("attachmentBase64", "");
									list.add(map);
								}
							}

						}
					} else {
						if (fi.getFieldName().equals("appId")) {
							appId = fi.getString();
						} else if (fi.getFieldName().equals("signType")) {
							signType = fi.getString();
						} else if (fi.getFieldName().equals("userId")) {
							userId = fi.getString();
						} else if (fi.getFieldName().equals("customsId")) {
							customsId = fi.getString();
						} else if (fi.getFieldName().equals("orderId")) {
							orderId = fi.getString();
						} else if (fi.getFieldName().equals("title")) {
							title = new String(fi.getString().getBytes("ISO-8859-1"), "utf-8");
						} else if (fi.getFieldName().equals("offerTime")) {
							offerTime = fi.getString();
						} else if (fi.getFieldName().equals("templateId")) {
							templateId = fi.getString();
						} else if (fi.getFieldName().equals("data")) {
							data = fi.getString();
						} else if (fi.getFieldName().equals("attachFileName1")) {
							attachFileName1 = new String(fi.getString().getBytes("ISO-8859-1"), "utf-8");
						} else if (fi.getFieldName().equals("attachFileName2")) {
							attachFileName2 = new String(fi.getString().getBytes("ISO-8859-1"), "utf-8");
						} else if (fi.getFieldName().equals("attachFileName3")) {
							attachFileName3 = new String(fi.getString().getBytes("ISO-8859-1"), "utf-8");
						} else if (fi.getFieldName().equals("attachFileName4")) {
							attachFileName4 = new String(fi.getString().getBytes("ISO-8859-1"), "utf-8");
						} else if (fi.getFieldName().equals("attachFileName5")) {
							attachFileName5 = new String(fi.getString().getBytes("ISO-8859-1"), "utf-8");
						} else if (fi.getFieldName().equals("appSecretKey")) {
							appSecretKey = fi.getString();
						}
					}
						
						//校验上传文件和输入文件名是否一致
						if (!fi.isFormField() && attachFileName1!=null ) {
							if(fieldName.equals("attachFile1")){
								String fileNameSuf=fileName.substring(fileName.lastIndexOf(".") + 1);
								String attachFileNameSuf1=attachFileName1.substring(attachFileName1.lastIndexOf(".") + 1);
								if(!fileNameSuf.equals(attachFileNameSuf1)){
									String result = "文件名称和上传文件名称后缀不一致！";
									request.getSession().setAttribute("result", result);
									RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/result.jsp");
									dispatcher.forward(request, response);
									return;
								}	
							}
						}
						if (!fi.isFormField() && attachFileName2!=null ) {
							if(fieldName.equals("attachFile2")){
								String fileNameSuf=fileName.substring(fileName.lastIndexOf(".") + 1);
								String attachFileNameSuf2=attachFileName2.substring(attachFileName2.lastIndexOf(".") + 1);
								if(!fileNameSuf.equals(attachFileNameSuf2)){
									String result = "文件名称和上传文件名称后缀不一致！";
									request.getSession().setAttribute("result", result);
									RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/result.jsp");
									dispatcher.forward(request, response);
									return;
								}
							}
						}
						if (!fi.isFormField() && attachFileName3!=null ) {
							if(fieldName.equals("attachFile3")){
								String fileNameSuf=fileName.substring(fileName.lastIndexOf(".") + 1);
								String attachFileNameSuf3=attachFileName3.substring(attachFileName3.lastIndexOf(".") + 1);
								if(!fileNameSuf.equals(attachFileNameSuf3)){
									String result = "文件名称和上传文件名称后缀不一致！";
									request.getSession().setAttribute("result", result);
									RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/result.jsp");
									dispatcher.forward(request, response);
									return;
								}	
							}
						}
						if (!fi.isFormField() && attachFileName4!=null ) {
							if(fieldName.equals("attachFile4")){
								String fileNameSuf=fileName.substring(fileName.lastIndexOf(".") + 1);
								String attachFileNameSuf4=attachFileName4.substring(attachFileName4.lastIndexOf(".") + 1);
								if(!fileNameSuf.equals(attachFileNameSuf4)){
									String result = "文件名称和上传文件名称后缀不一致！";
									request.getSession().setAttribute("result", result);
									RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/result.jsp");
									dispatcher.forward(request, response);
									return;
								}
							}
						}
						if (!fi.isFormField() && attachFileName5!=null ) {
							if(fieldName.equals("attachFile5")){
								String fileNameSuf=fileName.substring(fileName.lastIndexOf(".") + 1);
								String attachFileNameSuf5=attachFileName5.substring(attachFileName5.lastIndexOf(".") + 1);
								if(!fileNameSuf.equals(attachFileNameSuf5)){
									String result = "文件名称和上传文件名称后缀不一致！";
									request.getSession().setAttribute("result", result);
									RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/result.jsp");
									dispatcher.forward(request, response);
									return;
								}
							}
						}
					
				}
				
				
				
				
				
				
				
				/*
				 * if (attachmentName == "" || attachmentName == null ||
				 * attachment.size() == 0) {
				 * 
				 * } else { String[] attachmentNames =
				 * attachmentName.split(","); for (int i = 0; i <
				 * attachmentNames.length; i++) { Map map = new HashMap();
				 * map.put("attachmentName", attachmentNames[i]);
				 * map.put("attachmentBase64", attachment.get(i));
				 * list.add(map); } }
				 */
				Date needdate = new Date();
				long needtime = needdate.getTime();
				String time = needtime + "";
				String attachmentInfo = new Gson().toJson(list);
				String md5 = appId + "&" + customsId + "&" + data + "&" + offerTime + "&" + orderId + "&" + templateId
						+ "&" + time + "&" + title + "&" + userId + "&" + appSecretKey;
				String sign = MD5Util.MD5Encode(md5, "utf-8");
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
		        OMNamespace omNs = fac.createOMNamespace("http://wsdl.com/","createContractByTemplateAndFile");
		        OMElement eleData = fac.createOMElement("createContractByTemplateAndFile", omNs);
		        //对应参数的节点
		        String[] strs = new String[] { "appId", "time", "signType", "sign", "userId","customsId","orderId","title","offerTime","templateId","data","attachmentInfo" };
		        //参数值
		        String[] val = new String[] {appId, time, signType, sign, userId, customsId, orderId,
						title, offerTime, templateId, data, new Gson().toJson(list) };

		        //添加参数值
		        OMNamespace emOmNs = fac.createOMNamespace("","");        
		        for (int j = 0; j < strs.length; j++) {
		            OMElement inner = fac.createOMElement(strs[j], emOmNs);
		            inner.setText(val[j]);
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
		        
				System.out.println(result);*/

		        String[] paramName = { "appId", "time", "signType", "sign", "userId","customsId","orderId","title","offerTime","templateId","data","attachmentInfo" };

		        String[] paramValue = {appId, time, signType, sign, userId, customsId, orderId,
						title, offerTime, templateId, data, new Gson().toJson(list) };

		        String result = CallWebServiceUtil.CallHttpService(endpoint, "http://wsdl.com/", "createContractByTemplateAndFile", paramName, paramValue);
		        System.out.println(result);
				request.getSession().setAttribute("result", result);
				response.sendRedirect("jsp/result.jsp");// 跳转至结果页
//				Service service = new Service();
//				try {
//					// 接口调用
//					Call call = (Call) service.createCall();
//					call.setTargetEndpointAddress(new java.net.URL(endpoint));
//					call.setOperationName(new QName("http://wsdl.com/", "createContractByTemplateAndFile"));
//
//					call.addParameter("appId", XMLType.XSD_STRING, ParameterMode.IN);
//					call.addParameter("time", XMLType.XSD_STRING, ParameterMode.IN);
//
//					call.addParameter("signType", XMLType.XSD_STRING, ParameterMode.IN);
//					call.addParameter("sign", XMLType.XSD_STRING, ParameterMode.IN);
//					call.addParameter("userId", XMLType.XSD_STRING, ParameterMode.IN);
//					call.addParameter("customsId", XMLType.XSD_STRING, ParameterMode.IN);
//					call.addParameter("orderId", XMLType.XSD_STRING, ParameterMode.IN);
//					call.addParameter("title", XMLType.XSD_STRING, ParameterMode.IN);
//					call.addParameter("offerTime", XMLType.XSD_STRING, ParameterMode.IN);
//					call.addParameter("templateId", XMLType.XSD_STRING, ParameterMode.IN);
//					call.addParameter("data", XMLType.XSD_STRING, ParameterMode.IN);
//					call.addParameter("attachmentInfo", XMLType.XSD_STRING, ParameterMode.IN);
//
//					call.setReturnType(XMLType.XSD_STRING);
//
//					String result = call.invoke(new Object[] { appId, time, signType, sign, userId, customsId, orderId,
//							title, offerTime, templateId, data, new Gson().toJson(list) }).toString();
//					System.out.println(new Gson().toJson(list));
//					System.out.println(result);
//					request.getSession().setAttribute("result", result);
//					response.sendRedirect("jsp/result.jsp");// 跳转至结果页
//				} catch (ServiceException e) {
//					e.printStackTrace();
//				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				String result = "附件文件大于10M,请重新选择！";
				request.getSession().setAttribute("result", result);
				RequestDispatcher dispatcher = request.getRequestDispatcher("jsp/result.jsp");
				dispatcher.forward(request, response);
				return;

			}

		}
	}

}
