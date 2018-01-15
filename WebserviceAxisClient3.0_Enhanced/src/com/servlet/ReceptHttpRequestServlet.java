package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 第三方客户平台接收回调请求及返回应答
 */
public class ReceptHttpRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReceptHttpRequestServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 设置HTTP响应的正文的MIME类型及字符bianma
		response.setContentType("text/html;charset=UTF-8");
		
		// 获得info请求参数
		String param = request.getParameter("info");
		
		//afterDecoder就是云签对接平台返回给第三方平台具体的回调shuju
		String afterDecoder = URLDecoder.decode(param);
		
		//第三方平台做自己的业务chuli.........
				
		//响应返回结果给云签对接pingtai
		PrintWriter out = response.getWriter();
		
		//把这里需要输出的内容"你好"二字，替换为文档上规定的返回数据的格式
		out.println("你好!");
		out.close();
	}
}
