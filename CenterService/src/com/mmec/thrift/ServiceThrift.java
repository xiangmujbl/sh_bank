package com.mmec.thrift;

import java.net.InetSocketAddress;

import org.apache.thrift.TException;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadPoolServer.Args;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mmec.centerService.vpt.service.VptService;
import com.mmec.centerService.vpt.util.VptCache2DBUtil;
import com.mmec.css.conf.IConf;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ApsRMIServices;
import com.mmec.thrift.service.ContractRMIServices;
import com.mmec.thrift.service.CssRMIServices;
import com.mmec.thrift.service.DepositoryRMIServices;
import com.mmec.thrift.service.FeeRMIServices;
import com.mmec.thrift.service.InternelRMIServices;
import com.mmec.thrift.service.SerialRMIServices;
import com.mmec.thrift.service.TempleteRMIServices;
import com.mmec.thrift.service.UserRMIServices;
import com.mmec.thrift.service.VideoRMIServices;
import com.mmec.util.FileUtil;
import com.mmec.util.TimerTaskUtil;

public class ServiceThrift {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext app = new ClassPathXmlApplicationContext(
				"applicationContext.xml");

		//初始化vpt配置信息
		VptService vs = (VptService)app.getBean("vptService")	;	
		vs.initVptConfig();
		
		TMultiplexedProcessor processor = new TMultiplexedProcessor();
		//加载 短信模块
		processor.registerProcessor("ApsRMIServices",
				new ApsRMIServices.Processor((com.mmec.thrift.service.ApsRMIServices.Iface) app.getBean("apsIface")));

		processor.registerProcessor("CssRMIServices",
				new CssRMIServices.Processor((com.mmec.thrift.service.CssRMIServices.Iface) app.getBean("cssIface")));
		//计费
		processor.registerProcessor("FeeRMIServices",
				new FeeRMIServices.Processor((com.mmec.thrift.service.FeeRMIServices.Iface) app.getBean("feeIface")));
		//用户
		processor.registerProcessor("UserRMIServices",
				new UserRMIServices.Processor((com.mmec.thrift.service.UserRMIServices.Iface) app.getBean("userIface")));
		//合同
		processor.registerProcessor("ContractRMIServices",
				new ContractRMIServices.Processor((com.mmec.thrift.service.ContractRMIServices.Iface) app.getBean("contractIface")));
		//存管
		processor.registerProcessor("DepositoryRMIServices",
				new DepositoryRMIServices.Processor((com.mmec.thrift.service.DepositoryRMIServices.Iface) app.getBean("depositoryIface")));
		//验真--云签向中央承载拉取合同签署信息
		processor.registerProcessor("SerialRMIServices",
				new SerialRMIServices.Processor((com.mmec.thrift.service.SerialRMIServices.Iface) app.getBean("serialIface")));
		//自助模板
		processor.registerProcessor("TempleteRMIServices",
				new TempleteRMIServices.Processor((com.mmec.thrift.service.TempleteRMIServices.Iface) app.getBean("templeteIface")));

		//视频签署模板
		processor.registerProcessor("VideoRMIServices",
				new VideoRMIServices.Processor((com.mmec.thrift.service.VideoRMIServices.Iface) app.getBean("videoIface")));
		
		processor.registerProcessor("InternelRMIServices",
						new InternelRMIServices.Processor((com.mmec.thrift.service.InternelRMIServices.Iface) app.getBean("internelIface")));
		
		TProcessor logProcessor = new LogProcessor(processor);  

		try {
			//从配置文件中获取 IP地址和端口
			String serverIp= IConf.getValue("SERVER_IP");
			int serverPort=  Integer.parseInt(IConf.getValue("SERVER_PORT"));
			
			TServerTransport serverTransport = new TServerSocket(new InetSocketAddress(serverIp,serverPort));
			
			
			Args trArgs = new Args(serverTransport);
			//设置最大、最小线程数
			int maxThreads = Integer.parseInt(IConf.getValue("MAX_THREAD"));
			int minThreads = Integer.parseInt(IConf.getValue("MIN_THREAD"));
			trArgs.maxWorkerThreads(maxThreads);
			trArgs.minWorkerThreads(minThreads);
			trArgs.processor(logProcessor);

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
			//启动请求处理
			new Thread(new VptCache2DBUtil(app)).start();
			//启动图片转换
//			new Thread(new PDF2IMGTThread(app)).start();
//			new TimerTaskUtil().createScheduler();//定时器
			System.out.println("server begin ......................");
			server.serve();
			System.out.println("---------------------------------------");
			server.stop();
		} catch (Exception e) {
			try {
				FileUtil.getStackTrace(e);
			} catch (ServiceException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			throw new RuntimeException("index thrift server start failed!!"
					+ "/n" + e.getMessage());
			
		}
	}
}
/** 
 * 代理对象 
 */  
class LogProcessor implements TProcessor  
{  
    private TMultiplexedProcessor processor;  
      
    public LogProcessor(TMultiplexedProcessor processor)  
    {  
        this.processor = processor;  
    }  
      
    /** 
     * 该方法，客户端每调用一次，就会触发一次 
     */  
    public boolean process(TProtocol in, TProtocol out) throws TException  
    {  
        /** 
         * 从TProtocol里面获取TTransport对象 
         * 把TTransport对象转换成TSocket，然后在TSocket里面获取Socket，就可以拿到客户端IP 
         */  
        TSocket socket = (TSocket)in.getTransport();  
        socket.setTimeout(30000); 
//        System.out.println("本次请求访问的客户端IP地址为："+socket.getSocket().getRemoteSocketAddress());  
        
        return processor.process(in, out);  
    }
    
}  
