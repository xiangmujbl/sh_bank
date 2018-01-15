package com.mmec.util;

import com.mmec.css.conf.IConf;

import cfca.ra.toolkit.CFCARAClient;
import cfca.ra.toolkit.exception.RATKException;

public class TestConfig {
    // 连接超时时间 毫秒
    public static final int connectTimeout = 3000;
    // 读取超时时间 毫秒
    public static final int readTimeout = 30000;
    // URL（http、https方式）
    public static final String url = "https://114.215.155.12:8082/RA/CSHttpServlet";
    // 测试服务器ip（socket、ssl socket方式）
    public static final String ip = "180.96.21.25";
    // 测试服务器端口（socket、ssl socket方式）
    public static final int port = 9091;

    //正式服务器ip
//    public static final String FORMAL_IP="180.96.21.25";
    //正式服务器CS端口
//    public static final int FORMAL_CSPORT=9093;
    //正式服务器BS端口
//    public static final int FORMAL_BAPORT=9092;
    
    // 通信证书配置
    public static final String keyStorePath = "C:/Users/jq/Desktop/tmp/bocadmin1.jks";
    public static final String keyStorePassword = "Abcd1234";
    // 信任证书链配置
    public static final String trustStorePath = "C:/Users/jq/Desktop/tmp/bocadmin1.jks";
    public static final String trustStorePassword = "Abcd1234";

    // 客户端与RA之间为短链接
    // 该方法仅作为demo示例，使用时直接创建CFCARAClient对象即可
    // 连接参数不变时，CFCARAClient对象可重复使用，无需重新创建
    public static CFCARAClient getCFCARAClient() throws RATKException {
        int type = 5;
        CFCARAClient client = null;
        switch (type) {
        case 1:
            // 初始化为http连接方式，指定url
            client = new CFCARAClient(url, connectTimeout, readTimeout);
            break;
        case 2:
            // 初始化为https连接方式，指定url，另需配置ssl的证书及信任证书链
            client = new CFCARAClient(url, connectTimeout, readTimeout);
            client.initSSL(keyStorePath, keyStorePassword, trustStorePath, trustStorePassword);
            // 如需指定ssl协议、算法、证书库类型，使用如下方式
            // client.initSSL(keyStorePath, keyStorePassword, trustStorePath, trustStorePassword, "SSL", "IbmX509", "IbmX509", "JKS", "JKS");
            break;
        case 3:
            // 初始化为socket 连接方式，指定ip和端口
            client = new CFCARAClient(ip, port, connectTimeout, readTimeout);
            break;
        case 4:
            // 初始化为ssl socket 连接方式，指定ip和端口，另需配置ssl的证书及信任证书链
            client = new CFCARAClient(ip, port, connectTimeout, readTimeout);
            client.initSSL(keyStorePath, keyStorePassword, trustStorePath, trustStorePassword);
            // 如需指定ssl协议、算法、证书库类型，使用如下方式
            // client.initSSL(keyStorePath, keyStorePassword, trustStorePath, trustStorePassword, "SSL", "IbmX509", "IbmX509", "JKS", "JKS");
            break;
        case 5:
        	client = new CFCARAClient(IConf.getValue("RAIP"), Integer.parseInt(IConf.getValue("RACSPORT")), connectTimeout, readTimeout);
        default:
            break;
        }
        return client;
    }
}
