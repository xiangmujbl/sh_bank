package com.mmec.test.feeClient;

import java.io.UnsupportedEncodingException;
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



/**
 * 主要提供向中央承载系统请求接口
 * 
 * @author Administrator
 * 
 */
public class FeeClient {

	private TTransport transport;
	private TProtocol protocol;
	private FeeRMIServices.Client client;
	private ReturnData resData;
	private Logger log = Logger.getLogger(FeeRMIServices.class);
	private FeeRMIServices.Client service;

	/**
	 * 初始化资源
	 */
	public void init() {
//		transport = new TSocket("192.168.10.72", 9005);
		transport = new TSocket("127.0.0.1", 9003);
//		transport = new TSocket("192.168.10.114", 9005);
		protocol = new TBinaryProtocol(transport);
//		client = new FeeRMIServices.Client(protocol);
		
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol, "FeeRMIServices");
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
	
	/**
	 * 查询用户余额 测试
	 * @return
	 * @throws TException 
	 */
	public static String test_queryUserAccount() throws TException{
		TTransport transport = new TSocket("127.0.0.1", 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		String s=service.queryUserAccount(6).toString();
		transport.close();
		return s;
	}
	
	/**
	 * 添加余额
	 * @return
	 * @throws TException 
	 */
	public static String test_addMoney() throws TException{
		TTransport transport = new TSocket("127.0.0.1", 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		Map<String,String> map=new HashMap<String,String>();
		map.put("userid", "1");
		map.put("money", "10");
		String s=service.addMoney(map).toString();
		transport.close();
		return s;
	}
	
	/**
	 * 减少余额
	 * @return
	 * @throws TException 
	 */
	public static String test_reduceMoney() throws TException{
		TTransport transport = new TSocket("127.0.0.1", 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		Map<String,String> map=new HashMap<String,String>();
		map.put("userid", "1");
		map.put("money", "10");
		String s=service.reduceMoney(map).toString();
		transport.close();
		return s;
	}
	
	/**
	 * 查询用户服务  key:userid,paycode
	 * @return
	 * @throws TException 
	 */
	public static String test_queryUserServe() throws TException{
		TTransport transport = new TSocket("127.0.0.1", 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		Map<String,String> map=new HashMap<String,String>();
		map.put("userid", "1");
		map.put("paycode", "contract");
		String s=service.queryUserServe(map).toString();
		transport.close();
		return s;
	}
	
	/**
	 * 添加指定服务的次数
	 * @return
	 * @throws TException 
	 */
	public static String test_addServeTimes() throws TException{
		TTransport transport = new TSocket("127.0.0.1", 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		Map<String,String> map=new HashMap<String,String>();
		map.put("userid", "3");
		map.put("paycode", "contract");
		map.put("times","1");
		map.put("money", "10");
		String s=service.addServeTimes(map).toString();
		transport.close();
		return s;
	}
	
	/**
	 * 减少指定服务的次数
	 * @return
	 * @throws TException 
	 */
	public static String test_reduceServeTimes() throws TException{
		TTransport transport = new TSocket("127.0.0.1", 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		Map<String,String> map=new HashMap<String,String>();
		map.put("userid", "1");
		map.put("paycode", "contract");
		map.put("times","1");
		String s=service.reduceServeTimes(map).toString();
		transport.close();
		return s;
	}
	
	/**
	 * 查询指定的paycode对应的代码
	 * @return
	 * @throws TTransportException
	 */
	public static String test_queryPayServe() throws TTransportException{
		TTransport transport = new TSocket("127.0.0.1", 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		transport.close();
		Map<String,String> map=new HashMap<String,String>();
		map.put("paycode", "contract");
		return "";
	}
	
	/**
	 * 添加服务 typecode,typecontractname,typedesc,typename
	 * @return
	 * @throws TException 
	 */
	public static String test_addPayServe() throws TException{
		TTransport transport = new TSocket("127.0.0.1", 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		Map<String,String> map=new HashMap<String,String>();
		map.put("typecode", "contract");
		map.put("typename","合同服务");
		map.put("typedesc","1");
		map.put("typecontractname", "1");
		String s=service.addPayServe(map).toString();
		transport.close();
		return s;
	}
	
	/**
	 * 更新指定服务
	 * @return
	 * @throws TTransportException
	 */
	public static String test_updatePayServe() throws TTransportException{
		TTransport transport = new TSocket("127.0.0.1", 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		transport.close();
		return "";
	}
	
	/**
	 * 保存订单
	 * @return
	 * @throws TTransportException
	 */
	public static String test_saveOrder() throws TTransportException{
		TTransport transport = new TSocket("127.0.0.1", 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		transport.close();
		return "";
	}
	
	
	/**
	 * 查询记录
	 * @return
	 * @throws TTransportException
	 */
	public static String test_queryPayRecord() throws TTransportException{
		TTransport transport = new TSocket("127.0.0.1", 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		transport.close();
		return "";
	}
	
	/**
	 * 
	 * @param args
	 * @throws TException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws TException{
//		System.out.println(test_reduceMoney());
//		System.out.println(test_addMoney());
		System.out.println(test_queryUserAccount());
//		System.out.println(test_queryUserServe());    
//		System.out.println(test_addServeTimes());
//		System.out.println(test_addPayServe());
//		System.out.println(test_reduceServeTimes());
//		System.out.println(test_queryUserServe());
	}
	
}
