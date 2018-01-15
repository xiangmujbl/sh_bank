package com.test;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.protocol.Protocol;

import com.bean.CallWebServiceUtil;
import com.bean.GlobalUtil;
import com.bean.MD5Util;
import com.bean.MyProtocolSocketFactory;

/**
 * Servlet implementation class CompleteUserInformationTest
 */
public class CompleteUserInformationTest extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CompleteUserInformationTest() {
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
		String appId = request.getParameter("appId");
		String signType = request.getParameter("signType");
		String userId = request.getParameter("userId");
		 String userName = new String(request.getParameter("userName").getBytes("ISO-8859-1"),"utf-8");
		String identityCard = request.getParameter("identityCard");
		String mobile = request.getParameter("mobile");
		String email = request.getParameter("email");
	//	String bussinessLicenseNo = request.getParameter("bussinessLicenseNo");
		// String companyName = new
		// String(request.getParameter("companyName").getBytes("ISO-8859-1"),
		// "UTF-8");
		String app_key = request.getParameter("app_key");
		String time = new Date().getTime() + "";
		String md5Str = appId + "&" + email + "&" + identityCard + "&"
				+ mobile + "&" + time + "&" + userId +"&"+userName+ "&" + app_key;

		String sign = MD5Util.MD5Encode(md5Str, "utf-8");
		String endpoint = GlobalUtil.endpoint_addition;
		try {
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
	        OMNamespace omNs = fac.createOMNamespace("http://wsdl.com/","completeUserInformation");
	        OMElement eleData = fac.createOMElement("completeUserInformation", omNs);
	        //对应参数的节点
	        String[] strs = new String[] { "appId", "time", "sign", "signType", "userId","userName", "identityCard", "mobile", "email" };
	        //参数值
	        String[] val = new String[] { appId, time, sign, signType, userId,userName, identityCard, mobile, email};

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
	        }*/
			 String[] paramName = new String[] { "appId", "time", "sign", "signType", "userId","userName", "identityCard", "mobile", "email" };
			    
		        String[] paramValue = new String[] { appId, time, sign, signType, userId,userName, identityCard, mobile, email};

				String result = CallWebServiceUtil.CallHttpsService(endpoint, "http://wsdl.com/", "completeUserInformation", paramName, paramValue);
			
			System.out.println(result);
			request.getSession().setAttribute("result", result);
			response.sendRedirect("jsp/result.jsp");// 跳转至结果页
			
//			Call call = (Call) service.createCall();
//			call.setTargetEndpointAddress(new java.net.URL(endpoint));
//			call.setOperationName(new QName("http://wsdl.com/", "completeUserInformation"));
//			call.addParameter("appId", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("time", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("sign", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("signType", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("userId", XMLType.XSD_STRING, ParameterMode.IN);
//			// call.addParameter("companyName", XMLType.XSD_STRING,
//			// ParameterMode.IN);
//			call.addParameter("bussinessLicenseNo", XMLType.XSD_STRING, ParameterMode.IN);
//			// call.addParameter("userName", XMLType.XSD_STRING,
//			// ParameterMode.IN);
//			call.addParameter("identityCard", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("mobile", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("email", XMLType.XSD_STRING, ParameterMode.IN);
//			call.setReturnType(XMLType.XSD_STRING);
//			String result = call.invoke(new Object[] { appId, time, sign, signType, userId,
//					bussinessLicenseNo, identityCard, mobile, email }).toString();
//			request.getSession().setAttribute("result", result);
//			response.sendRedirect("jsp/result.jsp");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
