package com.mmec.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import com.mmec.business.bean.HttpParam;

/**
 * @author zzh 2016-12-26
 * 
 */

public class HttpSend {
	public static String sendGet(String url, List<HttpParam> list) throws IOException {
        StringBuffer buffer = new StringBuffer(); //用来拼接参数
        StringBuffer result = new StringBuffer(); //用来接受返回值
        URL httpUrl = null; //HTTP URL类 用这个类来创建连接
        URLConnection connection = null; //创建的http连接
        BufferedReader bufferedReader = null; //接受连接受的参数
        //如果存在参数，我们才需要拼接参数 类似于 localhost/index.html?a=a&b=b
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                buffer.append(list.get(i).getKey()).append("=").append(URLEncoder.encode(list.get(i).getValue(), "utf-8"));
                //如果不是最后一个参数，不需要添加&
                if ((i + 1) < list.size()) {
                    buffer.append("&");
                }
            }
            url = url + "?" + buffer.toString();
        }
        //创建URL
        httpUrl = new URL(url);
        //建立连接
        connection = httpUrl.openConnection();
        connection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;charset=UTF-8;q=0.9,*/*;q=0.8");
        
        connection.setRequestProperty("connection", "keep-alive");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
        connection.connect();
        //接受连接返回参数
        bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        bufferedReader.close();
        return result.toString();
    }
}
