package com.mmec.thrift;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.mmec.thrift.service.FeeRMIServices;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantParam;
import com.mmec.util.MmecException;

/**
 * 计费客户端调用类
 * 
 * @author Administrator
 *
 */
public class FeeClient {

	private static Logger log = Logger.getLogger(FeeClient.class);

	public static void main(String[] args) {
		try {
			reduceTimes(1, "contract", 1, "123");
			// addTimes(1,"contract",1,"123");
		} catch (MmecException e) {
			// TODO Auto-generated catch block
			log.info(e.getErrorMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 查询服务的次数
	 * 
	 * @throws TException
	 * @throws TTransportException
	 */

	public static String ip = "127.0.0.1";

	/**
	 * 扣次
	 * 
	 * @param userid
	 *            用户主键ID
	 * @param paycode
	 *            服务码： 合同/contract 保全/baoquan 买卖盾/tradingshield
	 * @param times
	 *            扣的次数
	 * @throws MmecException
	 */
	public static void reduceTimes(int userid, String paycode, int times, String serialnum) throws MmecException {
		TTransport transport = new TSocket(ConstantParam.IP, ConstantParam.PORT);
		try {
			transport.open();
			TBinaryProtocol protocol = new TBinaryProtocol(transport);
			TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol, "FeeRMIServices");
			FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
			Map<String, String> datamap = new HashMap<String, String>();
			datamap.put("userid", String.valueOf(userid));
			datamap.put("paycode", paycode);
			datamap.put("times", String.valueOf(times));
			datamap.put("payid", serialnum);
			try {
				ReturnData d = service.reduceServeTimes(datamap);
				if (!d.retCode.equals("0000")) {
					throw new MmecException(d.retCode, d.desc);
				}
				;
			} catch (TException e) {
				e.printStackTrace();
				throw new MmecException("9001", "扣次方法 执行异常");
			}
		} catch (TTransportException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new MmecException("9002", "连接中央承载系统异常:" + ip);
		} finally {
			transport.close();
		}
	}

	/**
	 * 退次
	 * 
	 * @param userid
	 *            用户表主键ID
	 * @param paycode
	 *            服务维护码
	 * @param times
	 *            退的次数
	 * @param serailnum
	 *            流水号
	 * @throws MmecException
	 */
	public static void addTimes(int userid, String paycode, int times, String serialnum) throws MmecException {
		TTransport transport = new TSocket(ConstantParam.IP, ConstantParam.PORT);
		try {
			transport.open();
			TBinaryProtocol protocol = new TBinaryProtocol(transport);
			TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol, "FeeRMIServices");
			FeeRMIServices.Client service = new FeeRMIServices.Client(mp);
			Map<String, String> datamap = new HashMap<String, String>();
			datamap.put("userid", String.valueOf(userid));
			datamap.put("paycode", paycode);
			datamap.put("times", String.valueOf(times));
			datamap.put("payid", serialnum);
			datamap.put("money", "0");
			try {
				ReturnData d = service.addServeTimes(datamap);
				if (!d.retCode.equals("0000")) {
					throw new MmecException(d.retCode, d.desc);
				}
			} catch (TException e) {
				e.printStackTrace();
				throw new MmecException("9003", "退次方法 执行异常");
			}
		} catch (TTransportException e1) {
			e1.printStackTrace();
			throw new MmecException("9004", "连接中央承载系统异常:" + ip);
		} finally {
			transport.close();
		}
	}
}