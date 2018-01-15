package com.test;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bean.CallWebServiceUtil;
import com.bean.GlobalUtil;
import com.bean.MD5Util;

public class ChangeMobileTest extends HttpServlet{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChangeMobileTest() {
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
		
		String userId=request.getParameter("userId");
		String appId = request.getParameter("appId");
		String signType = request.getParameter("signType");
		String newMobile = request.getParameter("newMobile");
		String oldMobile = request.getParameter("oldMobile");
		String identityId = request.getParameter("identityId");
		/*Date needdate = new Date();
		long needtime = needdate.getTime();*/
		String time = request.getParameter("time");
		//String md5Str = appId + "&" +newMobile+"&" +oldMobile+"&" +identityId+"&"+ time + "&" + userId;
		//String appSecretKey = request.getParameter("appSecretKey");
		//String md5str1 = md5Str + "&" + appSecretKey;
		String sign = request.getParameter("sign");
		String endpoint = GlobalUtil.endpoint_addition;

		
        String[] paramName = new String[] { "appId","time","sign","signType","oldMobile","newMobile","userId","identityId"};
    
        String[] paramValue = new String[] {appId, time, sign, signType, oldMobile,newMobile,userId,identityId};

		String result = CallWebServiceUtil.CallHttpsService(endpoint, "http://wsdl.com/", "changeMobile", paramName, paramValue);
        
		System.out.println(result);
		request.getSession().setAttribute("result", result);
		response.sendRedirect("jsp/result.jsp");
	}
}
