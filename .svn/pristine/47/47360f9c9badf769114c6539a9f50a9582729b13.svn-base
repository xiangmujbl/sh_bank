package com.mmec.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HtmlContent {
	private static Logger  log = Logger.getLogger(HtmlContent.class);
	// public static String do_post(String url, List<NameValuePair>
	// name_value_pair) throws IOException {
	// String body = "{}";
	// DefaultHttpClient httpclient = new DefaultHttpClient();
	// try {
	// HttpPost httpost = new HttpPost(url);
	// httpost.setEntity(new UrlEncodedFormEntity(name_value_pair,
	// StandardCharsets.UTF_8));
	// HttpResponse response = httpclient.execute(httpost);
	// HttpEntity entity = response.getEntity();
	// body = EntityUtils.toString(entity);
	// // UrlEncodedFormEntity a = new UrlEncodedFormEntity(parameters,
	// encoding)
	// } finally {
	// httpclient.getConnectionManager().shutdown();
	// }
	// return body;
	// }
	public static String do_get(String url) throws ClientProtocolException,
			IOException {
		String body = "{}";
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			body = EntityUtils.toString(entity);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return body;
	}

	/*
	 * 获取所指向url的输出流(指所有内容)
	 * 
	 * @urlString url路径 如:http://www.baidu.com返回的String 则为html代码
	 */
	public static  String getHtml(String urlString) {
		try {
			StringBuffer html = new StringBuffer();
			java.net.URL url = new java.net.URL(urlString); // 根据 String 表示形式创建
															// URL 对象。
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url
					.openConnection();// 返回一个 URLConnection 对象，它表示到 URL
										// 所引用的远程对象的连接。
			java.io.InputStreamReader isr = new java.io.InputStreamReader(
					conn.getInputStream());// 返回从此打开的连接读取的输入流。
			java.io.BufferedReader br = new java.io.BufferedReader(isr);// 创建一个使用默认大小输入缓冲区的缓冲字符输入流。

			String temp;
			while ((temp = br.readLine()) != null) { // 按行读取输出流
				if (!temp.trim().equals("")) {
					html.append(temp).append("\n"); // 读完每行后换行
				}
			}
			br.close(); // 关闭
			isr.close(); // 关闭
			return html.toString(); // 返回此序列中数据的字符串表示形式。
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取一个网页全部内容
	 */
	public static String getOneHtml(final String htmlurl) throws IOException {
		URL url;
		String temp;
		final StringBuffer sb = new StringBuffer();
		try {
			url = new URL(htmlurl);
			final BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream(), "utf-8"));// 读取网页全部内容
			while ((temp = in.readLine()) != null) {
				sb.append(temp);
			}
			in.close();
		} catch (final MalformedURLException me) {
			log.info("你输入的URL格式有问题！请仔细输入");
			me.getMessage();
			throw me;
		} catch (final IOException e) {
			e.printStackTrace();
			throw e;
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param s
	 * @return 获得网页标题
	 */
	public String getTitle(final String s) {
		String regex;
		String title = "";
		final List<String> list = new ArrayList<String>();
		regex = "<title>.*?</title>";
		final Pattern pa = Pattern.compile(regex, Pattern.CANON_EQ);
		final Matcher ma = pa.matcher(s);
		while (ma.find()) {
			list.add(ma.group());
		}
		for (int i = 0; i < list.size(); i++) {
			title = title + list.get(i);
		}
		return outTag(title);
	}

	/**
	 * 
	 * @param s
	 * @return 获得链接
	 */
	public List<String> getLink(final String s) {
		String regex;
		final List<String> list = new ArrayList<String>();
		regex = "<a[^>]*href=(\"([^\"]*)\"|\'([^\']*)\'|([^\\s>]*))[^>]*>(.*?)</a>";
		final Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
		final Matcher ma = pa.matcher(s);
		while (ma.find()) {
			list.add(ma.group());
		}
		return list;
	}

	/**
	 * 
	 * @param s
	 * @return 获得脚本代码
	 */
	public List<String> getScript(final String s) {
		String regex;
		final List<String> list = new ArrayList<String>();
		regex = "<script.*?</script>";
		final Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
		final Matcher ma = pa.matcher(s);
		while (ma.find()) {
			list.add(ma.group());
		}
		return list;
	}

	/**
	 * 
	 * @param s
	 * @return 获得CSS
	 */
	public List<String> getCSS(final String s) {
		String regex;
		final List<String> list = new ArrayList<String>();
		regex = "<style.*?</style>";
		final Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
		final Matcher ma = pa.matcher(s);
		while (ma.find()) {
			list.add(ma.group());
		}
		return list;
	}

	/**
	 * 
	 * @param s
	 * @return 去掉标记
	 */
	public String outTag(final String s) {
		return s.replaceAll("<.*?>", "");
	}

	/**
	 * 
	 * @param s
	 * @return 获取雅虎知识堂文章标题及内容
	 */
	public HashMap<String, String> getFromYahoo(final String s) {
		final HashMap<String, String> hm = new HashMap<String, String>();
		final StringBuffer sb = new StringBuffer();
		String html = "";
		log.info("\n------------------开始读取网页(" + s
				+ ")--------------------");
		try {
			html = getOneHtml(s);
		} catch (final Exception e) {
			e.getMessage();
		}
		// log.info(html);
		log.info("------------------读取网页(" + s
				+ ")结束--------------------\n");
		log.info("------------------分析(" + s
				+ ")结果如下--------------------\n");
		String title = outTag(getTitle(html));
		title = title.replaceAll("_雅虎知识堂", "");
		// Pattern pa=Pattern.compile("<div
		final Pattern pa = Pattern.compile("<div Pattern.DOTALL");
		final Matcher ma = pa.matcher(html);
		while (ma.find()) {
			sb.append(ma.group());
		}
		String temp = sb.toString();
		temp = temp.replaceAll("(<br>)+?", "\n");// 转化换行
		temp = temp.replaceAll("<p><em>.*?</em></p>", "");// 去图片注释
		hm.put("title", title);
		hm.put("original", outTag(temp));
		return hm;

	}

	/**
	 * 
	 * @param args
	 *            测试一组网页，针对雅虎知识堂
	 */
	public static void main(final String args[]) {
		String url = "";
		final List<String> list = new ArrayList<String>();
		System.out.print("输入URL，一行一个，输入结束后输入 go 程序开始运行:   \n");
		/*
		 * http://ks.cn.yahoo.com/question/1307121201133.html
		 * http://ks.cn.yahoo.com/question/1307121101907.html
		 * http://ks.cn.yahoo.com/question/1307121101907_2.html
		 * http://ks.cn.yahoo.com/question/1307121101907_3.html
		 * http://ks.cn.yahoo.com/question/1307121101907_4.html
		 * http://ks.cn.yahoo.com/question/1307121101907_5.html
		 * http://ks.cn.yahoo.com/question/1307121101907_6.html
		 * http://ks.cn.yahoo.com/question/1307121101907_7.html
		 * http://ks.cn.yahoo.com/question/1307121101907_8.html
		 */
		final BufferedReader br = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			while (!(url = br.readLine()).equals("go")) {
				list.add(url);
			}
		} catch (final Exception e) {
			e.getMessage();
		}
		final HtmlContent wc = new HtmlContent();
		HashMap<String, String> hm = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			hm = wc.getFromYahoo(list.get(i));
			log.info("标题： " + hm.get("title"));
			log.info("内容： \n" + hm.get("original"));
		}
		/*
		 * String htmlurl[] = {
		 * "http://ks.cn.yahoo.com/question/1307121201133.html",
		 * "http://ks.cn.yahoo.com/question/1307121101907.html",
		 * "http://ks.cn.yahoo.com/question/1307121101907_2.html",
		 * "http://ks.cn.yahoo.com/question/1307121101907_3.html",
		 * "http://ks.cn.yahoo.com/question/1307121101907_4.html",
		 * "http://ks.cn.yahoo.com/question/1307121101907_5.html",
		 * "http://ks.cn.yahoo.com/question/1307121101907_6.html",
		 * "http://ks.cn.yahoo.com/question/1307121101907_7.html",
		 * "http://ks.cn.yahoo.com/question/1307121101907_8.html" }; WebContent
		 * wc = new WebContent(); HashMap<String, String> hm = new
		 * HashMap<String, String>(); for (int i = 0; i < htmlurl.length; i++) {
		 * hm = wc.getFromYahoo(htmlurl[i]); log.info("标题： " +
		 * hm.get("title")); log.info("内容： \n" + hm.get("original"));
		 * }
		 */
		/*
		 * String html=""; String link=""; String sscript=""; String content="";
		 * log.info(htmlurl+" 开始读取网页内容：");
		 * html=wc.getOneHtml(htmlurl); log.info(htmlurl+"
		 * 读取完毕开始分析……"); html=html.replaceAll("
		 * (<script.*?)((\r\n)*)(.*?)((\r\n)*)(.*?)(</script>)","
		 * ");//去除脚本 html=html.replaceAll("
		 * (<style.*?)((\r\n)*)(.*?)((\r\n)*)(.*?)(</style>)","
		 * ");//去掉CSS html=html.replaceAll("<title>.*?</title>"," ");//除去页面标题
		 * html=html.replaceAll(
		 * "<a[^>]*href=(\"([^\"]*)\"|\'([^\']*)\'|([^\\s>]*))[^>]*>(.*?)</a>","
		 * ");//去掉链接 html=html.replaceAll("(\\s){2,}?"," ");//除去多余空格
		 * html=wc.outTag(html);//多余标记 log.info(html);
		 */

		/*
		 * String s[]=html.split(" +"); for(int i=0;i<s.length;i++){
		 * content=(content.length()>s[i].length())?content:s[i]; }
		 * log.info(content);
		 */

		// log.info(htmlurl+"网页内容结束");
		/*
		 * log.info(htmlurl+"网页脚本开始："); List
		 * script=wc.getScript(html); for(int i=0;i<script.size();i++){
		 * log.info(script.get(i)); }
		 * log.info(htmlurl+"网页脚本结束：");
		 * 
		 * log.info(htmlurl+"CSS开始："); List css=wc.getCSS(html);
		 * for(int i=0;i<css.size();i++){ log.info(css.get(i)); }
		 * log.info(htmlurl+"CSS结束：");
		 * 
		 * log.info(htmlurl+"全部链接内容开始："); List list=wc.getLink(html);
		 * for(int i=0;i<list.size();i++){ link=list.get(i).toString(); }
		 * log.info(htmlurl+"全部链接内容结束：");
		 * 
		 * log.info("内容"); log.info(wc.outTag(html));
		 */
	}
}