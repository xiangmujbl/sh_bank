package com.mmec.util;

import java.util.ArrayList;
import java.util.Calendar;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.mmec.css.conf.IConf;
import com.mmec.thrift.service.ContractRMIServices;

public class TimerTaskUtil implements Job{
	
	private Logger log = Logger.getLogger(TimerTaskUtil.class);
	//该方法实现需要执行的任务
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {	
		//从context中获取instName，groupName以及dataMap
		String instName = context.getJobDetail().getName();
		String groupName = context.getJobDetail().getGroup();
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		//从dataMap中获取myDescription，myValue以及myArray
		String myDescription = dataMap.getString("myDescription");
		int myValue = dataMap.getInt("myValue");
		ArrayList<String> myArray = (ArrayList<String>) dataMap.get("myArray");

	    int day = dataMap.getInt("day");       //日
        int month = dataMap.getInt("month");//月
        int year = dataMap.getInt("year");      //年 
		
        log.info("Instance =" + instName + ", group = " + groupName
        		+ ", description = " + myDescription + ", value =" + myValue
        		+ ", array item0 = " + myArray.get(0));
        System.out.println("year="+year+",month="+month+",day="+day);
        
        String serverIp= IConf.getValue("SERVER_IP");
		int serverPort=  Integer.parseInt(IConf.getValue("SERVER_PORT"));	
		TTransport transport = new TSocket(serverIp, serverPort);
		try {
			transport.open();
		} catch (TTransportException e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol,
				"ContractRMIServices");
		ContractRMIServices.Client service = new ContractRMIServices.Client(mp);
		try {
			 service.addSecurity();
		} catch (TException e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
		transport.close();		
	}
	
	public void createScheduler()
	{
		try{
			// 创建一个Scheduler
			SchedulerFactory schedFact = new StdSchedulerFactory();
			Scheduler sched = schedFact.getScheduler();
			sched.start();
			// 创建一个JobDetail，指明name，groupname，以及具体的Job类名，
			//该Job负责定义需要执行任务
			JobDetail jobDetail = new JobDetail("myJob", "myJobGroup",TimerTaskUtil.class);
			jobDetail.getJobDataMap().put("type", "FULL");			
			jobDetail.getJobDataMap().put("myDescription", "my job description"); 
			jobDetail.getJobDataMap().put("myValue", 1998); 
			ArrayList<String> list = new ArrayList<String>(); 
			list.add("item1"); 
			jobDetail.getJobDataMap().put("myArray", list);
			
			Calendar calendar = Calendar.getInstance();
	        int day = calendar.get(Calendar.DATE);       //日
	        int month = calendar.get(Calendar.MONTH) + 1;//月
	        int year = calendar.get(Calendar.YEAR);      //年
	        
			jobDetail.getJobDataMap().put("year", year); 
	        jobDetail.getJobDataMap().put("month", month);
			jobDetail.getJobDataMap().put("day", day); 
	       
//			CronTrigger  cc = new CronTrigger(name, group, jobName, jobGroup, startTime, endTime, cronExpression, timeZone);
			CronTrigger cronTrigger = new CronTrigger("myTrigger", "myGroup"); 
			try { 
				/*
				    * 表示一周的每天。
					/ 表示开始时刻与间隔时段。例如 Minutes 字段赋值 2/10 表示在一个小时内每 20 分钟执行一次，从第 2 分钟开始。
					? 仅适用于 Day-of-Month 和 Day-of-Week。? 表示对该字段不指定特定值。
				    Seconds 
					Minutes 
					Hours 
					Day-of-Month 
					Month 
					Day-of-Week 
					Year (Optional field)
				 */
				//执行定时任务的时间为每月1号的0时0分0秒
				cronTrigger.setCronExpression("0 0 0 1 * ?");
//				cronTrigger.setCronExpression("0 32 16 * * ?");
			} catch (Exception e) { 
				e.printStackTrace();
				log.info(e.getMessage());
			} 			
			// 用scheduler将JobDetail与cronTrigger关联在一起，开始调度任务
			sched.scheduleJob(jobDetail, cronTrigger);	
		}catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}
	
	
	public static void main(String[] args) {
		new TimerTaskUtil().createScheduler();
//		String dname = "CN=www.yunsign.com,OU=maimaiwang,O=maimaiwang,L=NJ,ST=JS,C=CN";
//		String alias = "www.yunsign.com";
//		String keystorePath = "d:/zlex.keystore";
//		String keypass = "654321";
//		String storepass = "123456";
//		String certPath = "d:/zlex.cer";
//		CertificateCoder.generateCert(dname, alias, keystorePath, keypass, storepass, certPath);
	}
}
