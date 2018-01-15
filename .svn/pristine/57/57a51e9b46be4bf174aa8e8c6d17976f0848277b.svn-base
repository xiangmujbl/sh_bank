package com.mmec.test.userClient;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.mmec.thrift.service.FeeRMIServices;
import com.mmec.thrift.service.ReturnData;
import com.mmec.thrift.service.UserRMIServices;

/**
 * 主要提供向中央承载系统请求接口
 * 
 * @author Administrator
 * 
 */
public class UserClient {

	private TTransport transport;
	private TProtocol protocol;
	private FeeRMIServices.Client client;
	private ReturnData resData;
	private Logger log = Logger.getLogger(UserRMIServices.class);
	private FeeRMIServices.Client service;

	/**
	 * 初始化资源
	 */
	public void init() {
		transport = new TSocket("192.168.10.108", 9003);
		protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"UserRMIServices");
		service = new FeeRMIServices.Client(mp);
		try {
			transport.open();
		} catch (TTransportException e) {
			log.error("打开transport.open()失败,请检查是否服务开启!", e);
		}
	}

	/**
	 * 关闭资源
	 */
	public void closeRes() {
		if (transport.isOpen()) {
			transport.close();
		}
	}

	public static void main(String[] args) throws TException {

		//test_userRegister();
 		
		test_userUpdate();
		//test_userUpdate_changePasswod();
		test_userQuery();
	}
	
	/**
	 * 修改密码
	 */
	public static void test_userUpdate_changePasswod() throws TException {
		
		TTransport transport = new TSocket("192.168.10.108", 9010);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol, "UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);

		Map datamap= new HashMap();

		//账户信息
		datamap.put("optFrom", "MMEC");//必填；
		datamap.put("optType", "changePassword");//必填；
		datamap.put("appId","appid001");//必填
		datamap.put("password","666777");//必填；
		datamap.put("newpassword","444555");//必填；
		datamap.put("platformUserName","QQ123456");//必填；
		
		System.out.println(service.userUpdate(datamap));
		transport.close();
	}
	
	/**
	 * 用户查询
	 */
	public static void test_userQuery() throws TException {
		
		TTransport transport = new TSocket("192.168.10.108", 9010);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol, "UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);

		Map datamap= new HashMap();
		
		//账户信息
		datamap.put("optFrom", "MMEC");//必填
		datamap.put("appId","appid001");//必填
		datamap.put("platformUserName","QQ123456");//必填
		
		System.out.println(service.userQuery(datamap));
		transport.close();
	}
	
	/**
	 * 用户资料修改
	 */
	public static void test_userUpdate() throws TException {
		
		TTransport transport = new TSocket("192.168.10.108", 9010);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol, "UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);
		Map datamap= new HashMap();

		//账户基本信息
		datamap.put("optFrom", "MMEC");//必填
		datamap.put("optType", "changeCustom");//必填；changeCustom：修改个人；changeCompany：修改企业
		datamap.put("appId","appid001");//必填
		datamap.put("type","1");//必填；1：个人；2：企业
		datamap.put("platformUserName","QQ123456");//必填
		datamap.put("email","uuuu@sina1.com");//不必填
		datamap.put("mobile","1355555555");//不必填
		
		//个人信息
 		datamap.put("userName","水电费水电费色素");//不必填
		datamap.put("identityCard","3201115554789642");//不必填
		
		//企业信息
//		datamap.put("businessLicenseNo","11111111111");//必填
//		datamap.put("companyName","2222222222222");//必填
//		datamap.put("companyType","国企");//不必填
//		datamap.put("proxyIdNumber","320123411111111111");//必填
//		datamap.put("proxyUserName","wwwwwwwww");//必填
		
		System.out.println(service.userUpdate(datamap));
		transport.close();
	}
	
	/**
	 * 用户注册
	 * 
	 * @return
	 * @throws TException
	 */
	public static void test_userRegister() throws TException {
		
		TTransport transport = new TSocket("192.168.10.108", 9010);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol, "UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);
		Map datamap= new HashMap();
		
		//账户基本信息
		datamap.put("optFrom", "MMEC");//必填
		datamap.put("appId","appid001");//必填
		datamap.put("type","1");//必填；1：个人；2：企业
		datamap.put("account","13999999991_p");//必填；个人：手机_p；企业：邮箱_e
		datamap.put("password","123456");//必填
		datamap.put("platformUserName","QQ123456");//必填
		datamap.put("email","wwwwwwwwwwww@111.com");//个人不必填；企业必填
		datamap.put("mobile","13999999991");//个人必填；企业不必填
		
		//个人信息
 		datamap.put("userName","哇哇哇哇");//必填
		datamap.put("identityCard","3201115554789642");//必填
		
		//企业信息
//		datamap.put("businessLicenseNo","11111111111");//必填
//		datamap.put("companyName","2222222222222");//必填
//		datamap.put("companyType","国企");//不必填
//		datamap.put("proxyIdNumber","320123411111111111");//必填
//		datamap.put("proxyUserName","wwwwwwwww");//必填
		
		System.out.println(service.userRegister(datamap));//,customDataMap,null));
		transport.close();
	}
	
	public static Map<String, String> Bean2Map(Object javaBean) {
		Map<String, String> ret = new HashMap<String, String>();
		try {
			Method[] methods = javaBean.getClass().getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().startsWith("get")) {
					String field = method.getName();
					field = field.substring(field.indexOf("get") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);
					Object value = method.invoke(javaBean, (Object[]) null);
					if(null != value  && !"".equals(value))
					{
						ret.put((String) field, (String) value);
					}
				}
			}
		} catch (Exception e) {
		}
		return ret;
	}

}
