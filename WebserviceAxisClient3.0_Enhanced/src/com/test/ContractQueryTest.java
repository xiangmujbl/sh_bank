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

public class ContractQueryTest extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ContractQueryTest() {
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
		
		String orderId=request.getParameter("orderId");
		String appId = request.getParameter("appId");
		String signType = request.getParameter("signType");
		
		Date needdate = new Date();
		long needtime = needdate.getTime();
		String time = needtime + "";
		String md5str = appId + "&" + orderId + "&" + time;
		String appSecretKey = request.getParameter("appSecretKey");
		String md5str1 = md5str + "&" + appSecretKey;
		String sign = MD5Util.MD5Encode(md5str1, "UTF-8");
		String endpoint = GlobalUtil.endpoint;

		//������
        String[] paramName = new String[] { "appId","time","signType","sign","orderId"};
        //����ֵ
        String[] paramValue = new String[] {appId, time, signType, sign, orderId};

		String result = CallWebServiceUtil.CallHttpsService(endpoint, "http://wsdl.com/", "queryContract", paramName, paramValue);
        
		System.out.println(result);
		request.getSession().setAttribute("result", result);
		response.sendRedirect("jsp/result.jsp");// �ض���jsp
	}

}
