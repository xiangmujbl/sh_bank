package com.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.bean.GlobalUtil;

public class InitServlet extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);

		GlobalUtil.endpoint = config.getInitParameter("endpoint");
		GlobalUtil.endpoint1 = config.getInitParameter("endpoint1");

		GlobalUtil.endpoint_addition = config.getInitParameter("endpoint_addition");
		GlobalUtil.endpoint_specialization = config.getInitParameter("endpoint_specialization");
		GlobalUtil.IMGPath = config.getInitParameter("IMGPath");
	}
}
