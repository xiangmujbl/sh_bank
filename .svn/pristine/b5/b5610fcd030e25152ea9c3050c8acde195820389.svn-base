package com.mmec.centerService.vpt.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Element;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mmec.centerService.vpt.entity.RequestLogBean;
import com.mmec.centerService.vpt.service.VptService;
import com.mmec.util.CacheUtil;

public class VptCache2DBUtil implements Runnable
{
	private static ApplicationContext app;
	private static CacheUtil cacheUtil = new CacheUtil();
	
	public static ApplicationContext getApp()
	{
		return app;
	}

	public static void setApp(ApplicationContext app)
	{
		VptCache2DBUtil.app = app;
	}
	public VptCache2DBUtil(ApplicationContext app)
	{
		this.app = app;
	}
	@Override
	public void run()
	{
		Map<String,Integer> dataMap = null;
		int doTime = 1;
	
		//初始化vpt配置信息
		// ThriftRMIServices.Processor((Iface)app.getBean("iface"));
		VptService vptService = null;
		RequestLogBean rb = null;
		while(true)
		{
			if(null == vptService)
			{
				vptService = (VptService)app.getBean("vptService");	
			}
//			System.out.println("I'm  in task ,当前第"+doTime+"次执行任务");
			dataMap = new HashMap<String,Integer>();
			//从内存中读取 需要入库的数据
			synchronized(cacheUtil.getVptConfigCache()){
				for(Object o:cacheUtil.getVptConfigCache().getKeys()){
					Element element = cacheUtil.getVptConfigCache().get(o);
					String cacheName = (String)element.getKey();
					//将数据从缓存中取出 放到本地变量中,删除缓存  入库
					if(cacheName.indexOf("#Map")> -1 )
					{
						dataMap.putAll((Map<? extends String, ? extends Integer>) element.getValue());
					}
	//				log.info("key="+((String)vptCache.get(o).getKey()+":value="+v));
	//				System.out.println("key="+((String)cacheUtil.getVptConfigCache().get(o).getKey()+":value="+v));
					//清除缓存中数据
					cacheUtil.removeVptConfig(cacheName);
				}	
			}
			//循环本地变量  入库
			for (String key : dataMap.keySet()) {  
				System.out.println("Key = " + key);  
				//appId+"#"+IP+"#"+user+"#"+year+"#"+month+"#"+day+"#"+hour;
				String[] requestLog = key.split("#");
				rb = new RequestLogBean();
				if(!"null".equals(requestLog[0]) )
				{
					rb.setAppId(requestLog[0]);
				}
				if(!"null".equals(requestLog[1]) )
				{
					rb.setIp(requestLog[1]);
				}
				if(!"null".equals(requestLog[2]) )
				{
					rb.setUserInfo(requestLog[2]);
				}
				rb.setYear(Integer.parseInt(requestLog[3]));
				rb.setMonth(Integer.parseInt(requestLog[4]));
				rb.setDay(Integer.parseInt(requestLog[5]));
				rb.setHour(Integer.parseInt(requestLog[6]));
				rb.setRequestTimes(dataMap.get(key));
				vptService.addRequestLog(rb);
			}  
			try
			{
				doTime++;
				//	等待一段时间后 重启执行
				Thread.sleep(60000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
}