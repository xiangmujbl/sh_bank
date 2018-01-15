package com.mmec.thrift;

import java.net.InetSocketAddress;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mmec.thrift.service.ApsRMIServices;
import com.mmec.thrift.service.ContractRMIServices;
import com.mmec.thrift.service.CssRMIServices;
import com.mmec.thrift.service.FeeRMIServices;
import com.mmec.thrift.service.UserRMIServices;

public class ServiceThrift {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ThriftRMIServices.Processor processor = new
		// ThriftRMIServices.Processor(new ThriftRMIServicesImpl());
		ApplicationContext app = new ClassPathXmlApplicationContext(
				"applicationContext.xml");

		// ThriftRMIServices.Processor processor = new
		// ThriftRMIServices.Processor((Iface)app.getBean("iface"));

		TMultiplexedProcessor processor = new TMultiplexedProcessor();

		processor.registerProcessor("ApsRMIServices",
				new ApsRMIServices.Processor((com.mmec.thrift.service.ApsRMIServices.Iface) app.getBean("apsIface")));

		processor.registerProcessor("CssRMIServices",
				new CssRMIServices.Processor((com.mmec.thrift.service.CssRMIServices.Iface) app.getBean("cssIface")));
		
		processor.registerProcessor("FeeRMIServices",
				new FeeRMIServices.Processor((com.mmec.thrift.service.FeeRMIServices.Iface) app.getBean("feeIface")));
		
		processor.registerProcessor("UserRMIServices",
				new UserRMIServices.Processor((com.mmec.thrift.service.UserRMIServices.Iface) app.getBean("userIface")));
		
//		processor.registerProcessor("contractIface",
//				new ContractRMIServices.Processor((com.mmec.thrift.service.ContractRMIServices.Iface) app.getBean("contractIface")));

		// 配置文件缓存
//		CacheProperties cache = CacheProperties.getInstance();

		try {
//			TServerTransport serverTransport = new TServerSocket(
//					new InetSocketAddress(cache.getValue("serverIp"),
//							Integer.parseInt(cache.getValue("serverPort"))));

			TServerTransport serverTransport = new TServerSocket(
					new InetSocketAddress("127.0.0.1",9003));
			Args trArgs = new Args(serverTransport);
			//设置最大、最小线程数
			trArgs.maxWorkerThreads(100);
			trArgs.minWorkerThreads(10);
			trArgs.processor(processor);

			//使用二进制来编码应用层的数据
			trArgs.protocolFactory(new TBinaryProtocol.Factory(true, true));
			
			//使用普通的socket来传输数据
			trArgs.transportFactory(new TTransportFactory());

			/*
			 在生产环境中，ThriftServer的选择是很重要的。
			 建议在TThreadPoolServer和 TThreadedSelectorServer中进行相应的选择。
			 个人建议选择TThreadedSelectorServer,
			 因为其支持网络NIO，在一个业务的处理过程中，
			 很大一部分时间会阻塞在网络通信上，
			 并且TThreadedSelectorServer能吞吐更多的网络连接，
			 而ThreadPoolServer吞吐的网络连接数和其启动的线程数量有关，
			 如果存在客户端调用代码未正常关闭Transport时，
			 其网络连接只有在时间超时时才会正常释放掉，
			 造成服务的无法正常提供。
			 */
			TServer server = new TThreadPoolServer(trArgs);
			

			System.out.println("server begin ......................");
			server.serve();
			System.out.println("---------------------------------------");
			server.stop();
		} catch (Exception e) {
			throw new RuntimeException("index thrift server start failed!!"
					+ "/n" + e.getMessage());
		}
	}
}
