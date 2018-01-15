package com.mmec.test.feeClient;

import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import com.mmec.thrift.service.FeeRMIServices;

public class FeeClientForYunSign{
	public static String ip="192.168.10.63";
	
	/**
	 * 查询余额
	 * @throws TException
	 */
	public static void queryUserAccountDetail() throws TException{
		TTransport transport = new TSocket(ip, 9003);
		transport.open();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,"FeeRMIServices");
		FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
		Map<String,String> map=new HashMap<String,String>();
		map.put("id","1");
		map.put("paycode","contract");
		System.out.println(service.queryUserAccountDetail(map));
	}
	
	
	
	public static void main(String []args) throws TException{
		queryUserAccountDetail();
	}
}