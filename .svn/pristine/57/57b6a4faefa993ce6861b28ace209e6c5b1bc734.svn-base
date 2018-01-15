package com.mmec.test.feeClient;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.mmec.thrift.service.ContractRMIServices;
import com.mmec.thrift.service.DepositoryRMIServices;
import com.mmec.thrift.service.FeeRMIServices;
import com.mmec.thrift.service.ReturnData;
import com.mmec.thrift.service.UserRMIServices;
import com.sun.corba.se.spi.activation.Server;
import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil.ToStringAdapter;



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
	private ContractRMIServices.Client contractservice;
	private static String ip="192.168.10.63";

	/**
	 * 初始化资源
	 */
	public void init() {
//		transport = new TSocket("192.168.10.72", 9005);
		transport = new TSocket(ip, 9003);
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
	 * @param int
	 * @return
	 * @throws TException 
	 */
	public static String test_userQuery() throws TException{
		TTransport transport = new TSocket(ip, 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"ContractRMIServices");
		ContractRMIServices.Client service = new ContractRMIServices.Client(mp);
		Map<String,String> datamap=new HashMap<String,String>();
		datamap.put("queryall","notnull");
		String s=service.createContract(datamap).toString();
		transport.close();
		return s;
	}
	
	
	/**
	 * 查询用户余额 测试
	 * @param int
	 * @return
	 * @throws TException 
	 */
	public static String test_queryUserAccount(int userid) throws TException{
		TTransport transport = new TSocket(ip, 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		String s=service.queryUserAccount(userid).toString();
		transport.close();
		return s;
	}
	
	/**
	 * 添加余额
	 * @param map内userid对应用户主键,money对应用户充值的金额
	 * @return
	 * @throws TException 
	 */
	public static String test_addMoney(Map<String,String> map) throws TException{
		TTransport transport = new TSocket(ip, 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		String s=service.addMoney(map).toString();
		transport.close();
		return s;
	}
	
	/**
	 * 减少余额
	 * @param map内userid对应用户主键,money对应用户要扣费的金额
	 * @return
	 * @throws TException 
	 */
	public static String test_reduceMoney(Map<String,String> map) throws TException{
		TTransport transport = new TSocket(ip, 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		String s=service.reduceMoney(map).toString();
		transport.close();
		return s;
	}
	
	/**
	 * 查询用户服务
	 * @param  map内userid对应用户主键,paycode对应服务标识的代码
	 * @return
	 * @throws TException 
	 */
	public static String test_queryUserServe(Map<String,String> map) throws TException{
		TTransport transport = new TSocket(ip, 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		String s=service.queryUserServe(map).toString();
		transport.close();
		return s;
	}
	
	/**
	 * @param map中:userid 用户主键  paycode 服务标识  times服务剩余次数 money为购买此次服务扣费数目
	 * @return
	 * @throws TException
	 */
	public static String test_addServeTimes(Map<String,String> map) throws TException{
		TTransport transport = new TSocket(ip, 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		String s=service.addServeTimes(map).toString();
		transport.close();
		return s;
	}
	
	/**
	 * 减少指定服务的次数
	 * @param map中 userid 用户主键  paycode 服务标识 times减少的服务次数
	 * @return
	 * @throws TException 
	 */
	public static String test_reduceServeTimes(Map<String,String> map) throws TException{
		TTransport transport = new TSocket(ip, 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		String s=service.reduceServeTimes(map).toString();
		transport.close();
		return s;
	}
	
	/**
	 * 查询指定的paycode对应的代码
	 * @return
	 * @throws TException 
	 */
	public static String test_queryPayServe() throws TException{
		TTransport transport = new TSocket(ip, 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		Map<String,String> map=new HashMap<String,String>();
//		map.put("paycode", "contract");
		map.put("queryall","");
		String s=service.queryPayServe(map).getPojo();
		transport.close();
		return s;
	}
	
	/**
	 * 添加服务 typecode,typecontractname,typedesc,typename
	 * @return
	 * @throws TException 
	 */
	public static String test_addPayServe() throws TException{
		TTransport transport = new TSocket(ip, 9003);
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
		TTransport transport = new TSocket(ip, 9003);
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
	 * @throws TException 
	 */
	public static String test_saveOrder(HashMap<String,String> map1,HashMap<String,String> map2) throws TException{
		TTransport transport = new TSocket(ip, 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		System.out.println(service.saveOrder(map1, map2));
		transport.close();
		return "";
	}
	
	
	/**
	 * 查询记录
	 * @map:userid,paycode 可以只穿userid或者只穿paycode 或者两个都传
	 * @return
	 * @throws TException 
	 */
	public static String test_queryPayRecord() throws TException{
		TTransport transport = new TSocket(ip, 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		Map<String,String> datamap=new HashMap<String,String>();
//		datamap.put("userid", "1");
//		datamap.put("paycode", "contract");
//		datamap.put("pagenumber", "0");
//		datamap.put("pagesize", "10");
//		datamap.put("payid", "1");
		datamap.put("queryall", "");
		String s=service.queryPayRecord(datamap).toString();
		transport.close();
		return s;
	}
	
	/**
	 * @throws TException 
	 * 
	 */
	public static String test_queryPage() throws TException{
		TTransport transport = new TSocket(ip, 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		Map<String,String> datamap=new HashMap<String,String>();
		datamap.put("userid","1");
		datamap.put("pagenumber", "0");
		datamap.put("pagesize", "10");
		String s=service.updatePayServe(datamap).toString();
		transport.close();
		return s;
	}
	
	public static String test_queryAllUser() throws TException{
		TTransport transport = new TSocket(ip, 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"UserRMIServices");
		UserRMIServices.Client service = new UserRMIServices.Client(mp);
		Map<String,String> datamap=new HashMap<String,String>();
		datamap.put("isPage", "2");
		String s=service.getAllUser(datamap).getPojo().toString();
		transport.close();
		return s;
	}
	
	//for 云签
	/**
	 * 保存订单
	 * @throws TException
	 */
	public static void yunqian_saveOrder() throws TException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TTransport transport = new TSocket(ip, 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		Map<String,String> datamap=new HashMap<String,String>();
		Map<String,String> mmap=new HashMap<String,String>();
		datamap.put("createTime",sdf.format(new Date()));
		datamap.put("invoiceStatus","2");
		datamap.put("orderId",String.valueOf(System.currentTimeMillis()));
		datamap.put("orderStatus","1");
		datamap.put("orderType","contract");
		datamap.put("payWay","1");
		datamap.put("price","1");
		datamap.put("remark","1");
		datamap.put("time","1");
		datamap.put("commodity","1");
		datamap.put("tradeType","1");
		datamap.put("userId","1");
		datamap.put("packageId",null);
		datamap.put("payMethod","1");
		mmap.put("type", "1");
		mmap.put("title", "title");
		mmap.put("name", "name");
		mmap.put("mobile", "mobile");
		mmap.put("mailAddress", "mailAddress");
		mmap.put("company", "company");
		mmap.put("code", "code");
		mmap.put("registerAddress", "registerAddress");
		mmap.put("registerPhone", "registerPhone");
		mmap.put("bankName", "bankName");
		mmap.put("bankAccount", "bankAccount");
		mmap.put("content", "content");
		mmap.put("mailMethod", "mailMethod");
		System.out.println(service.saveOrder(datamap, mmap).toString());
		transport.close();
	}
	
	/**
	 * 更新订单状态
	 * orderId为查询参数   其它三个为更改值参数
	 * @throws TException 
	 */
	public static void update_orderStatus() throws TException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TTransport transport = new TSocket(ip, 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		Map<String,String> map=new HashMap<String,String>();
		map.put("orderStatus", "2");
		map.put("changeTime",sdf.format(new Date()));
		map.put("orderId", "abc");
		map.put("payplamOrderId", "123456");
		System.out.println(service.updateOrderStatus(map));
	}
	
	/**
	 * 查询消费记录信息-普通(不带充值)
	 * @throws TException 
	 */
	public static void query_payRecord() throws TException{
		TTransport transport = new TSocket(ip, 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		Map<String,String> map=new HashMap<String,String>();
		map.put("userid", "1");
		map.put("pagenumber", "0");
		map.put("pagesize", "10");
		map.put("paycode","contract");
		System.out.println(service.queryPayRecord(map));
	}
	
	/**
	 * 查询订单信息
	 * @throws TException 
	 */
	public static void query_Order() throws TException{
		TTransport transport = new TSocket(ip, 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		Map<String,String> map=new HashMap<String,String>();
		map.put("userid","1");
		map.put("pagenumber","0");
		map.put("pagesize","10");
		map.put("paycode","contract");
		System.out.println(service.queryOrder(map));
	}
	
	
	
	/**
	 * 保存存证信息
	 */
	public static void save_evidence() throws TException{
		TTransport transport = new TSocket(ip, 9006);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"DepositoryRMIServices");
		DepositoryRMIServices.Client service = new DepositoryRMIServices.Client(mp);
		Map<String,String> map=new HashMap<String,String>();
		map.put("appid", "sr7R5V1LGQ");
		map.put("orderid", "hyctest01");
		map.put("creatorname", "cjhtest01");
		map.put("title", "hyc测试1");
		map.put("type","1");
		map.put("userstr","cjhtest01,cjhtest022");
		map.put("filepathlist","C:\\Users\\Administrator\\Desktop\\vva.txt,"
				+ "C:\\Users\\Administrator\\Desktop\\vvb.txt");
		System.out.println(service.uploadEvidence(map));
	}
	
	/**
	 * 查看存证信息
	 */
	public static void evidence_detail() throws TException{
		TTransport transport = new TSocket(ip, 9006);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"DepositoryRMIServices");
		DepositoryRMIServices.Client service = new DepositoryRMIServices.Client(mp);
		Map<String,String> map=new HashMap<String,String>();
		map.put("appid", "sr7R5V1LGQ");
		map.put("orderid", "hyctest01");
		System.out.println(service.evidenceDetail(map));
	}
	
	/**
	 * 下载存证信息
	 */
	public static void downLoad_evidence() throws TException{
		TTransport transport = new TSocket(ip, 9006);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"DepositoryRMIServices");
		DepositoryRMIServices.Client service = new DepositoryRMIServices.Client(mp);
		Map<String,String> map=new HashMap<String,String>();
		map.put("appid", "sr7R5V1LGQ");
		map.put("orderid", "hyctest01");
		System.out.println(service.downloadEvidence(map));
	}
	
	
	/**
	 * 测试函数-(测试成功和不成功时的返回)
	 * @param args
	 * @throws TException 
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws TException{
		downLoad_evidence();
//		test_userQuery();
		
//		System.out.println(test_queryPayRecord());
		
//		System.out.println(test_queryAllUser());
//		//用户账户金额查询测试
//		//1-成功
//		System.out.println("1_1"+test_queryUserAccount(1));
//		//错误 检查返回
//		//1-用户ID对应的账户不存在
//		System.out.println("1_2"+test_queryUserAccount(8888));
//		
//		
//		//用户充值测试
//		//1-成功 
//		Map<String,String> map_cz_succ=new HashMap<String,String>();
//		map_cz_succ.put("userid", "1");
//		map_cz_succ.put("money", "5");
//		System.out.println("2_1"+test_addMoney(map_cz_succ));
//		
//		//错误 检查返回
//		//1-map参数传错
//		Map<String,String> map_cz_fail1=new HashMap<String,String>();
//		System.out.println("2_2"+test_addMoney(map_cz_fail1));
//		
//		Map<String,String> map_cz_fail2=new HashMap<String,String>();
//		map_cz_fail2.put("userid", "1a");
//		map_cz_fail2.put("money", "5");
//		System.out.println("2_3"+test_addMoney(map_cz_fail2));
//		
//		//2-用户ID对应用户不存在
//		Map<String,String> map_cz_fail3=new HashMap<String,String>();
//		map_cz_fail3.put("userid", "3333");
//		map_cz_fail3.put("money", "5");
//		System.out.println("2_4"+test_addMoney(map_cz_fail3));
//		
//		
//		//3-充值的money金额数目不合法
//		Map<String,String> map_cz_fail4=new HashMap<String,String>();
//		map_cz_fail4.put("userid", "1");
//		map_cz_fail4.put("money", "9999999999999999");
//		System.out.println("2_5"+test_addMoney(map_cz_fail4));
//		
//		
//		//用户扣费测试
//		//1-成功
//		Map<String,String> map_kf_succ=new HashMap<String,String>();
//		map_kf_succ.put("userid", "1");
//		map_kf_succ.put("money", "5");
//		map_kf_succ.put("paycode", "contract");
//		map_kf_succ.put("payid", "CPB237043248270941");
//		System.out.println("3_1"+test_reduceMoney(map_kf_succ));
//		
//		//错误返回
//		//1-map参数传错
//		Map<String,String> map_fail_1=new HashMap<String,String>();
//		map_fail_1.put("userid", "3333");
//		map_fail_1.put("money", "5");
//		System.out.println("3_2"+test_reduceMoney(map_fail_1));
//		
//		//2-用户ID对应用户不存在
//		Map<String,String> map_kf_fail2=new HashMap<String,String>();
//		map_kf_fail2.put("userid", "3333");
//		map_kf_fail2.put("money", "5");
//		map_kf_fail2.put("paycode", "contract");
//		map_kf_fail2.put("payid", "CPB237043248270941");
//		System.out.println("3_3"+test_reduceMoney(map_kf_fail2));
//		
//		//3-扣费的money金额数目不合法
//		Map<String,String> map_kf_fail3=new HashMap<String,String>();
//		map_kf_fail3.put("userid", "3333");
//		map_kf_fail3.put("money", "-5");
//		map_kf_fail3.put("paycode", "contract");
//		map_kf_fail3.put("payid", "CPB237043248270941");
//		System.out.println("3_4"+test_reduceMoney(map_kf_fail3));
//		
//		
//		//4-用户余额不足
//		Map<String,String> map_kf_fail4=new HashMap<String,String>();
//		map_kf_fail4.put("userid", "1");
//		map_kf_fail4.put("money", "500");
//		map_kf_fail4.put("paycode", "contract");
//		map_kf_fail4.put("payid", "CPB237043248270941");
//		System.out.println("3_5"+test_reduceMoney(map_kf_fail4));
//		
//		
//		//5-扣费的服务不存在
//		Map<String,String> map_kf_fail5=new HashMap<String,String>();
//		map_kf_fail5.put("userid", "1");
//		map_kf_fail5.put("money", "5");
//		map_kf_fail5.put("paycode", "contrac");
//		map_kf_fail5.put("payid", "CPB237043248270941");
//		System.out.println("3_6"+test_reduceMoney(map_kf_fail5));
//		
		//用户服务次数检查 成功4
//		Map<String,String> map_cc_succ=new HashMap<String,String>();
//		map_cc_succ.put("userid", "1");
//		map_cc_succ.put("paycode", "contract");
//		System.out.println("4_1"+test_queryUserServe(map_cc_succ));
//		
//		//用户不存在
//		Map<String,String> map_cc_fail2=new HashMap<String,String>();
//		map_cc_fail2.put("userid", "3333");
//		map_cc_fail2.put("paycode", "contract");
//		System.out.println("4_2"+test_queryUserServe(map_cc_fail2));
//		
//		//服务编码不存在
//		Map<String,String> map_cc_fail1=new HashMap<String,String>();
//		map_cc_fail1.put("userid", "1");
//		map_cc_fail1.put("paycode", "contrac");  
//		System.out.println("4_1"+test_queryUserServe(map_cc_fail1));
		
		
//     充次数--成功5
//		Map<String,String> map_chci_succ=new HashMap<String,String>();
//		map_chci_succ.put("userid", "1");
//		map_chci_succ.put("paycode", "contract");
//		map_chci_succ.put("times","10");
//		map_chci_succ.put("money", "5");
//		System.out.print("5_1"+test_addServeTimes(map_chci_succ));
//		
//		//充次数用户不存在
//		Map<String,String> map_chci_succ1=new HashMap<String,String>();
//		map_chci_succ1.put("userid", "333");
//		map_chci_succ1.put("paycode", "contract");
//		map_chci_succ1.put("times","10");
//		map_chci_succ1.put("money", "5");
//		System.out.print("5_2"+test_addServeTimes(map_chci_succ1));
//		
//		//充服务不存在
//		Map<String,String> map_chci_succ2=new HashMap<String,String>();
//		map_chci_succ2.put("userid", "1");
//		map_chci_succ2.put("paycode", "contrac");
//		map_chci_succ2.put("times","10");
//		map_chci_succ2.put("money", "5");
//		System.out.print("5_3"+test_addServeTimes(map_chci_succ2));
//		
//		//少参数 没有填服务
//		Map<String,String> map_chci_succ3=new HashMap<String,String>();
//		map_chci_succ3.put("userid", "333");
//		map_chci_succ3.put("times","10");
//		map_chci_succ3.put("money", "5");
//		System.out.print("5_4"+test_addServeTimes(map_chci_succ3));
		
		//扣费的余额不足
//		Map<String,String> map_chci_succ4=new HashMap<String,String>();
//		map_chci_succ4.put("userid", "1");
//		map_chci_succ4.put("paycode", "contract");
//		map_chci_succ4.put("times","10");
//		map_chci_succ4.put("money", "550");
//		System.out.print("5_5"+test_addServeTimes(map_chci_succ4));
		
		
		//扣次数 成功6
//		Map<String,String> map_kcis_1=new HashMap<String,String>();
//		map_kcis_1.put("userid", "1");
//		map_kcis_1.put("paycode", "contract");
//		map_kcis_1.put("payid", "1");
//		map_kcis_1.put("times", "1");
//		System.out.println("6_1"+test_reduceServeTimes(map_kcis_1));
		
		//扣次  次数为负数 拦截--
//		Map<String,String> map_kcis_2=new HashMap<String,String>();
//		map_kcis_2.put("userid", "1");
//		map_kcis_2.put("paycode", "contract");
//		map_kcis_2.put("payid", "1");
//		map_kcis_2.put("times", "-5");
//		System.out.println("6_1"+test_reduceServeTimes(map_kcis_2));
		
		//增加服务
//		Map<String,String> map_zjfw=new HashMap<String,String>();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		TTransport transport = new TSocket(ip, 9003);
//		transport.open();
//		TBinaryProtocol protocol = new TBinaryProtocol(transport);
//		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
//		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
//		Map<String,String> datamap=new HashMap<String,String>();
//		Map<String,String> mmap=new HashMap<String,String>();
//		datamap.put("createTime",sdf.format(new Date()));
//		datamap.put("invoiceStatus","2");
//		datamap.put("orderId",String.valueOf(System.currentTimeMillis()));
//		datamap.put("orderStatus","1");
//		datamap.put("orderType","contract");
//		datamap.put("payWay","1");
//		datamap.put("price","1");
//		datamap.put("remark","1");
//		datamap.put("time","1");
//		datamap.put("commodity","1");
//		datamap.put("tradeType","1");
//		datamap.put("userId","1");
//		datamap.put("packageId","1");
//		datamap.put("payMethod","1");
//		mmap.put("type", "1");
//		mmap.put("title", "title");
//		mmap.put("name", "name");
//		mmap.put("mobile", "mobile");
//		mmap.put("mailAddress", "mailAddress");
//		mmap.put("company", "company");
//		mmap.put("code", "code");
//		mmap.put("registerAddress", "registerAddress");
//		mmap.put("registerPhone", "registerPhone");
//		mmap.put("bankName", "bankName");
//		mmap.put("bankAccount", "bankAccount");
//		mmap.put("content", "content");
//		mmap.put("mailMethod", "mailMethod");
//		System.out.println(service.saveOrder(datamap, mmap).toString());
//		transport.close();
//		yunqian_saveOrder();
	}
	
}
