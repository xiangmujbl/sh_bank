package com.mmec.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mmec.centerService.contractModule.service.ContractService;
import com.mmec.centerService.contractModule.service.CreateContractService;
import com.mmec.centerService.contractModule.service.DownloadService;
import com.mmec.centerService.contractModule.service.SignContractService;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ContractRMIServices;
import com.mmec.thrift.service.ReturnData;

public class testUtil extends TestCase {
	
	public static  ApplicationContext context;
	
	@Autowired
	private CreateContractService createContractService;
	
	@Autowired
	public static SignContractService signContractService;
	
	@Autowired
	private DownloadService downloadService;
	
	@Autowired
	private ContractService contractService;
	/*
	@Test
	public void testCreate() {
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		createContractService = (CreateContractService)context .getBean("createContractService");
		Map<String,String> datamap = new HashMap<String,String>();
		datamap.put("appId", "appid001");
		datamap.put("customId", "uid001,uid002");
		datamap.put("tempNumber", "temp_001");
		String data = "{\"PayType\":\"1\",\"ContractNo\":\"1436771117958\",\"ewm\":\"1234123412341234123412341234123412341234123412欢迎使用 phpMyAdmin欢迎使用 phpMyAdmin欢迎使用 phpMyAdmin欢迎使用 phpMyAdmin欢迎使用 phpMyAdmin欢迎使用 phpMyAdmin欢迎使用 phpMyAdmin欢迎使用 phpMyAdmin欢迎使用 phpMyAdmin欢迎使用 phpMyAdmin3412341234123412341234123412341234\",\"PickUpAddress\":\"test\",\"Sale_MBR_SPE_ACCT_NO\":null,\"Buy_MBR_SPE_ACCT_NO\":\"null\",\"fkxs\":\"7\",\"cdhp\":\"90\",\"cdhp1\":\"90\",\"tempfkxs\":\"中国银行\"}";
		datamap.put("tempData", data);
		datamap.put("ucid", "uid001");
		datamap.put("offerTime", "2016-01-01 10:10:10");
		datamap.put("startTime", "2015-01-01 10:10:10");
		datamap.put("endTime", "2016-02-01 10:10:10");
		datamap.put("title", "test");
		datamap.put("pname", "test");
		datamap.put("orderId", System.currentTimeMillis()+"");
		ReturnData rd = null;;
		try {
			rd = createContractService.createContract(datamap);
			System.out.println(rd);
		} catch (ServiceException e) {
			rd = new ReturnData(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),"");
			System.out.println(rd);
		}
		
	}
*/
//	@Test
//	public void testSign() throws ServiceException
//	{
//		context = new ClassPathXmlApplicationContext("applicationContext.xml");
//		signContractService = (SignContractService)context .getBean("signContractService");
//		Map<String,String> datamap = new HashMap<String,String>();
//		datamap.put("orderId", "test20151225004");
//		datamap.put("ucid", "uid002");
//		datamap.put("appId", "appid001");
//		datamap.put("optFrom", "1");
//		datamap.put("signMode", "1");
//		datamap.put("isPDF", "N");
//		ReturnData rd = null;
//		try {
//			rd = signContractService.serviceSignContract(datamap);
//			System.out.println(rd);
//		} catch (ServiceException e) {
//			rd = new ReturnData(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),"");
//			System.out.println(rd);
//		}	
//	}
		
	
//	@Test
//	public void testQueryContract() throws ServiceException
//	{
//		context = new ClassPathXmlApplicationContext("applicationContext.xml");
//		contractService = (ContractService)context .getBean("contractService");
//		Map<String,String> datamap = new HashMap<String,String>();
//		datamap.put("orderId", "test20151225004");
//		datamap.put("appId", "appid001");
//		ReturnData rd = null;
//		try {
//			rd = contractService.queryContract(datamap);
//			System.out.println(rd);
//		} catch (ServiceException e) {
//			rd = new ReturnData(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),"");
//			System.out.println(rd);
//		}
//	}
	
//	@Test
	public void Zip() throws ServiceException
	{
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		downloadService = (DownloadService)context.getBean("downloadService");
		Map<String,String> datamap = new HashMap<String,String>();
		datamap.put("orderId", "test20151225004");
		datamap.put("appId", "appid001");
		ReturnData rd = null;
		try {
			rd = downloadService.zipDownload(datamap);
			System.out.println(rd);
		} catch (ServiceException e) {
			rd = new ReturnData(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),"");
			System.out.println(rd);
		}
	}
	

	/**
	 * 用户查询
	 */
	public static void testEventSign() throws TException {
		TTransport transport = new TSocket("192.168.10.108", 9010);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"ContractRMIServices");
		ContractRMIServices.Client service = new ContractRMIServices.Client(mp);
		Map<String,String> datamap = new HashMap<String,String>();
		datamap.put("orderId", "test20151225004");
		datamap.put("ucid", "uid002");
		datamap.put("appId", "appid001");
		datamap.put("optFrom", "1");
		datamap.put("signMode", "3");
		datamap.put("isPDF", "N");

		System.out.println(service.signContract(datamap));
		transport.close();
	}
	public static void main(String[] args) {
//		double h = 1695;
//		double nh = 1263;//合同真实高度
//		DecimalFormat df = new DecimalFormat("###.000");
//		double zoom = Double.valueOf(df.format(h/nh));
//		int a = (int) (1 * zoom);
//		
//		int i =  3335%1263;
//		System.out.println(i);
		try {
			testEventSign();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
