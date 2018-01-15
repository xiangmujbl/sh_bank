package com.mmec.css.articles;

import javax.servlet.http.HttpServletRequest;

/**
 * 剥离参数
 * @author liuy
 *
 */

public class StripPara {
	public static String getBeforePage(HttpServletRequest request)
	{
		String url=request.getHeader("Referer");
		return getBeforePage(url);
	}
	public static String getBeforePage(String s)
	{
		int x=s.lastIndexOf("/");
		int y=s.indexOf("?");
		if(y==-1)
		{
			return s.substring(x, s.length());
		}
		else
		{
			return s.substring(x, y);
		}
	}
}
