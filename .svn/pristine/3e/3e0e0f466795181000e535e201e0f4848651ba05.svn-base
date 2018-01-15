package com.test;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

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
import org.apache.commons.httpclient.protocol.Protocol;

import com.bean.GlobalUtil;
import com.bean.MD5Util;
import com.bean.MyProtocolSocketFactory;

/**
 * Servlet implementation class SignBySmsTest
 */
public class SignTest extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SignTest() {
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

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");

		// 参数装填
		String appId = request.getParameter("appId");
		String signType = request.getParameter("signType");
		String userId = request.getParameter("userId");
		String orderId = request.getParameter("orderId");
		String sealId = request.getParameter("sealId");
		String certType = request.getParameter("certType");
		
		Date needdate = new Date();
		long needtime = needdate.getTime();
		String time = needtime + "";
		
		// MD5拼接，校验
		String md5str = appId + "&" + certType + "&" + orderId + "&" + sealId + "&" + time + "&" + userId;
		String appSecretKey = request.getParameter("appSecretKey");
		String md5str1 = md5str + "&" + appSecretKey;
		String sign = MD5Util.MD5Encode(md5str1, "UTF-8");
		String endpoint = GlobalUtil.endpoint;
		//使用ServiceClient调用https的WebService
			/*	ServiceClient serviceClient = new ServiceClient();

				MyProtocolSocketFactory socketfactory = new MyProtocolSocketFactory();

				Protocol protocol = new Protocol("https", socketfactory, 443);
				
				Options options = serviceClient.getOptions();
				
				options.setProperty(HTTPConstants.CUSTOM_PROTOCOL_HANDLER, protocol);
				*/
				
				ServiceClient serviceClient = new ServiceClient();	
				
				Options options = serviceClient.getOptions();
				//设置调用地址
				OMElement ret = null;
		        EndpointReference targetEPR = new EndpointReference(endpoint);
		        options.setTo(targetEPR);
		        serviceClient.setOptions(options);
		        OMFactory fac = OMAbstractFactory.getOMFactory();
		        //设置命名空间
		        OMNamespace omNs = fac.createOMNamespace("http://wsdl.com/","sign");
		        OMElement eleData = fac.createOMElement("sign", omNs);
		        //对应参数的节点
		        String[] strs = new String[] { "appId", "time", "sign", "signType", "userId","orderId","sealId","certType" };
		        //参数值
		        String[] val = new String[] { appId, time, sign, signType, userId, orderId, sealId, certType};

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
		        
				System.out.println(result);
				request.getSession().setAttribute("result", result);
				response.sendRedirect("jsp/result.jsp");// 跳转至结果页
//		Service service = new Service();
//		try {
//			// 接口调用
//			Call call = (Call) service.createCall();
//			call.setTargetEndpointAddress(new java.net.URL(endpoint));
//			call.setOperationName(new QName("http://wsdl.com/", "sign"));
//
//			call.addParameter("appId", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("time", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("sign", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("signType", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("userId", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("orderId", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("sealId", XMLType.XSD_STRING, ParameterMode.IN);
//			call.addParameter("certType", XMLType.XSD_STRING, ParameterMode.IN);
//
//			String result = call.invoke(new Object[] { appId, time, sign, signType, userId, orderId, sealId, certType })
//					.toString();
//			System.out.println(result);
//			request.getSession().setAttribute("result", result);
//			response.sendRedirect("jsp/result.jsp");// 跳转至结果页
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
	}
}
