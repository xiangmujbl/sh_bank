package com.mmec.centerService.vpt.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.liuy.pdf.IConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mmec.aps.service.NoteService;
import com.mmec.centerService.vpt.dao.RequestLogDao;
import com.mmec.centerService.vpt.dao.VPTConfigDao;
import com.mmec.centerService.vpt.entity.RequestLogBean;
import com.mmec.centerService.vpt.entity.RequestTimesBean;
import com.mmec.centerService.vpt.entity.VPTConfigEntity;
import com.mmec.centerService.vpt.service.VptService;
import com.mmec.util.CacheUtil;
@Service("vptService")
public class VptServiceImpl implements VptService
{
	//平台阈值信息
	private static int APP_HOUR_REQUEST_TIMES = 0;
	private static int APP_DAY_REQUEST_TIMES = 0;
	private static int APP_LIMIT_HOUR_REQUEST_TIMES = 0;
	private static int APP_LIMIT_DAY_REQUEST_TIMES = 0;
	private static String APP_WRNING_MOBILE = "";
	private static int APP_WRNING_INTERVAL_TIME = 0;

	//用户阈值信息
	private static int USER_HOUR_REQUEST_TIMES = 0;
	private static int USER_DAY_REQUEST_TIMES = 0;
	private static int USER_LIMIT_HOUR_REQUEST_TIMES = 0;
	private static int USER_LIMIT_DAY_REQUEST_TIMES = 0;
	private static String USER_WRNING_MOBILE = "";
	private static int USER_WRNING_INTERVAL_TIME = 0;
	
	//IP阈值信息
	private static int IP_HOUR_REQUEST_TIMES = 0;
	private static int IP_DAY_REQUEST_TIMES = 0;
	private static int IP_LIMIT_HOUR_REQUEST_TIMES = 0;
	private static int IP_LIMIT_DAY_REQUEST_TIMES = 0;
	private static String IP_WRNING_MOBILE = "";
	private static int IP_WRNING_INTERVAL_TIME = 0;
	
//	static
//	{
//		APP_HOUR_REQUEST_TIMES = Integer.parseInt(IConf.getValue("APP_HOUR_REQUEST_TIMES"));
//		APP_DAY_REQUEST_TIMES =  Integer.parseInt(IConf.getValue("APP_DAY_REQUEST_TIMES"));
//		APP_LIMIT_HOUR_REQUEST_TIMES =  Integer.parseInt(IConf.getValue("APP_LIMIT_HOUR_REQUEST_TIMES"));
//		APP_LIMIT_DAY_REQUEST_TIMES =  Integer.parseInt(IConf.getValue("APP_LIMIT_DAY_REQUEST_TIMES"));
//		APP_WRNING_MOBILE = IConf.getValue("APP_WRNING_MOBILE");
//		APP_WRNING_INTERVAL_TIME =  Integer.parseInt(IConf.getValue("APP_WRNING_INTERVAL_TIME"));
//
//		//用户阈值信息
//		USER_HOUR_REQUEST_TIMES =  Integer.parseInt(IConf.getValue("USER_HOUR_REQUEST_TIMES"));
//		USER_DAY_REQUEST_TIMES =  Integer.parseInt(IConf.getValue("USER_DAY_REQUEST_TIMES"));
//		USER_LIMIT_HOUR_REQUEST_TIMES =  Integer.parseInt(IConf.getValue("USER_LIMIT_HOUR_REQUEST_TIMES"));
//		USER_LIMIT_DAY_REQUEST_TIMES =  Integer.parseInt(IConf.getValue("USER_LIMIT_DAY_REQUEST_TIMES"));
//		USER_WRNING_MOBILE = IConf.getValue("USER_WRNING_MOBILE");
//		USER_WRNING_INTERVAL_TIME =  Integer.parseInt(IConf.getValue("USER_WRNING_INTERVAL_TIME"));
//		
//		//IP阈值信息
//		IP_HOUR_REQUEST_TIMES =  Integer.parseInt(IConf.getValue("IP_HOUR_REQUEST_TIMES"));
//		IP_DAY_REQUEST_TIMES =  Integer.parseInt(IConf.getValue("IP_DAY_REQUEST_TIMES"));
//		IP_LIMIT_HOUR_REQUEST_TIMES =  Integer.parseInt(IConf.getValue("IP_LIMIT_HOUR_REQUEST_TIMES"));
//		IP_LIMIT_DAY_REQUEST_TIMES =  Integer.parseInt(IConf.getValue("IP_LIMIT_DAY_REQUEST_TIMES"));
//		IP_WRNING_MOBILE = IConf.getValue("IP_WRNING_MOBILE");
//		IP_WRNING_INTERVAL_TIME =  Integer.parseInt(IConf.getValue("IP_WRNING_INTERVAL_TIME"));
//	}
	
	@Autowired
	private VPTConfigDao vptConfigDao;
	
	@Autowired
	private RequestLogDao requestLogDao;

	@Autowired
	private NoteService noteService;
	
	private static SimpleDateFormat sdfHour = new SimpleDateFormat("yyyy-MM-dd:HH");
	private static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
	
	private static CacheUtil cacheUtil = new CacheUtil();
	
	@Override
	public void initVptConfig()
	{
		System.out.println("It's in vptConfig");
		if(null == cacheUtil.getVpt("APP_HOUR_REQUEST_TIMES"))
		{
			List<VPTConfigEntity> vptConfigList = vptConfigDao.findAll();
			for(VPTConfigEntity vptConfigEntity : vptConfigList)
			{
				if("IP".equals(vptConfigEntity.getVptCode()))
				{
					IP_HOUR_REQUEST_TIMES = vptConfigEntity.getVptHourTimes();
					cacheUtil.setVptConfig("IP_HOUR_REQUEST_TIMES",IP_HOUR_REQUEST_TIMES);
					IP_DAY_REQUEST_TIMES = vptConfigEntity.getVptDayTimes();
					cacheUtil.setVptConfig("IP_DAY_REQUEST_TIMES",IP_DAY_REQUEST_TIMES);
					IP_LIMIT_HOUR_REQUEST_TIMES = vptConfigEntity.getLimitHourTimes();
					cacheUtil.setVptConfig("IP_LIMIT_HOUR_REQUEST_TIMES",IP_LIMIT_HOUR_REQUEST_TIMES);
					IP_LIMIT_DAY_REQUEST_TIMES = vptConfigEntity.getLimitDayTimes();
					cacheUtil.setVptConfig("IP_LIMIT_DAY_REQUEST_TIMES",IP_LIMIT_DAY_REQUEST_TIMES);
					IP_WRNING_MOBILE = vptConfigEntity.getWarningMobbile();
					cacheUtil.setVptConfig("IP_WRNING_MOBILE",IP_WRNING_MOBILE);
					IP_WRNING_INTERVAL_TIME = vptConfigEntity.getWarningIntervalTime();
					cacheUtil.setVptConfig("IP_WRNING_INTERVAL_TIME",IP_WRNING_INTERVAL_TIME);
				}
				else if("USER".equals(vptConfigEntity.getVptCode()))
				{
					USER_HOUR_REQUEST_TIMES = vptConfigEntity.getVptHourTimes();
					cacheUtil.setVptConfig("USER_HOUR_REQUEST_TIMES",USER_HOUR_REQUEST_TIMES);
					USER_DAY_REQUEST_TIMES = vptConfigEntity.getVptDayTimes();
					cacheUtil.setVptConfig("USER_DAY_REQUEST_TIMES",USER_DAY_REQUEST_TIMES);
					USER_LIMIT_HOUR_REQUEST_TIMES = vptConfigEntity.getLimitHourTimes();
					cacheUtil.setVptConfig("USER_LIMIT_HOUR_REQUEST_TIMES",USER_LIMIT_HOUR_REQUEST_TIMES);
					USER_LIMIT_DAY_REQUEST_TIMES = vptConfigEntity.getLimitDayTimes();
					cacheUtil.setVptConfig("USER_LIMIT_DAY_REQUEST_TIMES",USER_LIMIT_DAY_REQUEST_TIMES);
					USER_WRNING_MOBILE = vptConfigEntity.getWarningMobbile();
					cacheUtil.setVptConfig("USER_WRNING_MOBILE",USER_WRNING_MOBILE);
					USER_WRNING_INTERVAL_TIME = vptConfigEntity.getWarningIntervalTime();
					cacheUtil.setVptConfig("USER_WRNING_INTERVAL_TIME",USER_WRNING_INTERVAL_TIME);
				}
				else if("APP".equals(vptConfigEntity.getVptCode()))
				{
					APP_HOUR_REQUEST_TIMES = vptConfigEntity.getVptHourTimes();
					cacheUtil.setVptConfig("APP_HOUR_REQUEST_TIMES",APP_HOUR_REQUEST_TIMES);
					APP_DAY_REQUEST_TIMES = vptConfigEntity.getVptDayTimes();
					cacheUtil.setVptConfig("APP_DAY_REQUEST_TIMES",APP_DAY_REQUEST_TIMES);
					APP_LIMIT_HOUR_REQUEST_TIMES = vptConfigEntity.getLimitHourTimes();
					cacheUtil.setVptConfig("APP_LIMIT_HOUR_REQUEST_TIMES",APP_LIMIT_HOUR_REQUEST_TIMES);
					APP_LIMIT_DAY_REQUEST_TIMES = vptConfigEntity.getLimitDayTimes();
					cacheUtil.setVptConfig("APP_LIMIT_DAY_REQUEST_TIMES",APP_LIMIT_DAY_REQUEST_TIMES);
					APP_WRNING_MOBILE = vptConfigEntity.getWarningMobbile();
					cacheUtil.setVptConfig("APP_WRNING_MOBILE",APP_WRNING_MOBILE);
					APP_WRNING_INTERVAL_TIME = vptConfigEntity.getWarningIntervalTime();
					cacheUtil.setVptConfig("APP_WRNING_INTERVAL_TIME",APP_WRNING_INTERVAL_TIME);
				}
			}
		}
//		else if(0 == APP_HOUR_REQUEST_TIMES)
//		{
//			IP_HOUR_REQUEST_TIMES = (int)cacheUtil.getVptConfig("IP_HOUR_REQUEST_TIMES");
//			IP_DAY_REQUEST_TIMES = (int)cacheUtil.getVptConfig("IP_DAY_REQUEST_TIMES");
//			IP_LIMIT_HOUR_REQUEST_TIMES = (int)cacheUtil.getVptConfig("IP_LIMIT_HOUR_REQUEST_TIMES");
//			IP_LIMIT_DAY_REQUEST_TIMES = (int)cacheUtil.getVptConfig("IP_LIMIT_DAY_REQUEST_TIMES");
//			IP_WRNING_MOBILE = (String)cacheUtil.getVptConfig("IP_WRNING_MOBILE");
//			IP_WRNING_INTERVAL_TIME = (int)cacheUtil.getVptConfig("IP_WRNING_INTERVAL_TIME");
//		
//			USER_HOUR_REQUEST_TIMES = (int)cacheUtil.getVptConfig("USER_HOUR_REQUEST_TIMES");
//			USER_DAY_REQUEST_TIMES = (int)cacheUtil.getVptConfig("USER_DAY_REQUEST_TIMES");
//			USER_LIMIT_HOUR_REQUEST_TIMES = (int)cacheUtil.getVptConfig("USER_LIMIT_HOUR_REQUEST_TIMES");
//			USER_LIMIT_DAY_REQUEST_TIMES = (int)cacheUtil.getVptConfig("USER_LIMIT_DAY_REQUEST_TIMES");
//			USER_WRNING_MOBILE = (String)cacheUtil.getVptConfig("USER_WRNING_MOBILE");
//			USER_WRNING_INTERVAL_TIME = (int)cacheUtil.getVptConfig("USER_WRNING_INTERVAL_TIME");
//		
//			APP_HOUR_REQUEST_TIMES = (int)cacheUtil.getVptConfig("APP_HOUR_REQUEST_TIMES");
//			APP_DAY_REQUEST_TIMES = (int)cacheUtil.getVptConfig("APP_DAY_REQUEST_TIMES");
//			APP_LIMIT_HOUR_REQUEST_TIMES = (int)cacheUtil.getVptConfig("APP_LIMIT_HOUR_REQUEST_TIMES");
//			APP_LIMIT_DAY_REQUEST_TIMES = (int)cacheUtil.getVptConfig("APP_LIMIT_DAY_REQUEST_TIMES");
//			APP_WRNING_MOBILE = (String)cacheUtil.getVptConfig("APP_WRNING_MOBILE");
//			APP_WRNING_INTERVAL_TIME = (int)cacheUtil.getVptConfig("APP_WRNING_INTERVAL_TIME");
//		}
		//读取数据库中 当前小时的所有访问次数数据
		//获取时分秒
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH)+1; 
		int day = c.get(Calendar.DATE); 
		int hour = c.get(Calendar.HOUR_OF_DAY);
		String nowHour = sdfHour.format(c.getTime());
		String nowDay = sdfDay.format(c.getTime());
		
		List<RequestTimesBean> requestTimesBeabList = requestLogDao.queryRequestTimesByHourGroupByIp(year, month, day, hour);
		for(RequestTimesBean rb : requestTimesBeabList)	
		{
			cacheUtil.setVpt(rb.getIp()+"#"+nowHour,rb.getTimes());
		}
		requestTimesBeabList = requestLogDao.queryRequestTimesByDayGroupByIp(year, month, day);
		for(RequestTimesBean rb : requestTimesBeabList)	
		{
			cacheUtil.setVpt(rb.getIp()+"#"+nowDay,rb.getTimes());
		}
		requestTimesBeabList = requestLogDao.queryRequestTimesByHourGroupByAppId(year, month, day, hour);
		for(RequestTimesBean rb : requestTimesBeabList)	
		{
			cacheUtil.setVpt(rb.getAppId()+"#"+nowHour,rb.getTimes());
		}
		requestTimesBeabList = requestLogDao.queryRequestTimesByDayGroupByAppId(year, month, day);
		for(RequestTimesBean rb : requestTimesBeabList)	
		{
			cacheUtil.setVpt(rb.getAppId()+"#"+nowDay,rb.getTimes());
		}
		requestTimesBeabList = requestLogDao.queryRequestTimesByHourGroupByUserInfo(year, month, day, hour);
		for(RequestTimesBean rb : requestTimesBeabList)	
		{
			cacheUtil.setVpt(rb.getUserInfo()+"#"+nowHour,rb.getTimes());
		}
		requestTimesBeabList = requestLogDao.queryRequestTimesByDayGroupByUserInfo(year, month, day);
		for(RequestTimesBean rb : requestTimesBeabList)	
		{
			cacheUtil.setVpt(rb.getUserInfo()+"#"+nowDay,rb.getTimes());
		}
		System.out.println("In init () -----------------");
		cacheUtil.getAllVpt();
	}
	
	/*
	 * 请求处理
	 * 根据阈值配置将请求数据统计
	 * 当超出阈值 拒绝请求 发送通知信息给 预警人
	 */
	@Override
	public  boolean dealRequest(Map<String,String> datamap, String optType)
	{
		Date front = new Date();

		//获取当前时分秒
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH)+1; 
		int day = c.get(Calendar.DATE); 
		int hour = c.get(Calendar.HOUR_OF_DAY);
		String nowHour = sdfHour.format(c.getTime());
		String nowDay = sdfDay.format(c.getTime());
		//从用户请求中获取 IP APPID USER等信息
		String user = datamap.get("userAccount");
		if(null == user || "".equals(user))
		{
			 user = datamap.get("platformUserName");
		}
		if(null == user || "".equals(user))
		{
			 user = datamap.get("userId");
		}
		if(null == user || "".equals(user))
		{
			 user = datamap.get("ucid");
		}

			
		String appId = datamap.get("appId");
		String IP = datamap.get("requestIp");
		
		//将此次请求信息放入缓存  等待线程入库
		Map<String,Integer> map = new HashMap<String,Integer>();
		String mapKey = year+"#"+month+"#"+day+"#"+hour+"#Map";
		//读取缓存中数据时 需要对数据进行枷锁
		synchronized(cacheUtil.getVptConfigCache()){
			if(null != cacheUtil.getVptConfig(mapKey))
			{
				map = (HashMap<String,Integer>)cacheUtil.getVptConfig(mapKey);
			}
			String key = appId+"#"+IP+"#"+user+"#"+year+"#"+month+"#"+day+"#"+hour;
	
			if(null == map.get(key))
			{
				map.put(key,1);
				System.out.println(mapKey+" 次数"+1);
			}
			else
			{
				int times = map.get(key);
				times++;
				map.put(key,times);
				System.out.println(mapKey+" 次数"+times);
			}
			//ehcache 直接替换 不需要删除再添加 
			cacheUtil.setVptConfig(mapKey,map);
		}
		
		Date end = new Date();
//		System.out.println("数据入库耗时："+ (end.getTime() - front.getTime()));
		////////////////////////////////////////////////////////////////
		boolean ret = true;
		//如果参数中有IP  判断IP访问情况
		if(null != IP && !"".equals(IP))
		{
		    ret = judgeIpRequese( IP, year, month, day, hour, nowDay, nowHour);
		}
		
		if(ret && null != appId && !"".equals(appId))
		{
			ret = judgeAppRequese( appId, year, month, day, hour, nowDay, nowHour);
		}

		if(ret && null != user && !"".equals(user))
		{
			ret = judgeUserRequese( user, year, month, day, hour, nowDay, nowHour);
		}
		Date end2 = new Date();
//		System.out.println("处理逻辑判断总耗时："+ (end2.getTime() - front.getTime()));
		// TODO Auto-generated method stub
		return ret;
	}
	
	private static String smsContent = "您好，当前系统中#title#访问量已经达到#times#，请关注。";
	
	public void sendWarningMessage(String mobiles,String title,int times)
	{
		String[] mobile = mobiles.split(",");
		try
		{
			String sendMessage = smsContent.replace("#title#", title).replace("#times#", times+"");
			System.out.println(sendMessage);
			
			noteService.sendMessage4MW(mobile[0],sendMessage);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR); 
		int month = c.get(Calendar.MONTH); 
		int day = c.get(Calendar.DATE); 
		int hour = c.get(Calendar.HOUR_OF_DAY); 

		System.out.println(c.getTime());
		System.out.println("year:"+year+",month:"+month+",day:"+day+",hour:"+hour);
		System.out.println(sdfDay.format(c.getTime()));
	}
	
	//判断ip的访问情况  需要告警或者 限制访问
	public boolean judgeIpRequese(String IP,int year,int month,int day,int hour,String nowDay,String nowHour)
	{
		if(null != IP && !"".equals(IP))
		{
			//获取数据库中数据
			//小时访问次数
			int ipHourVisitTime = 0;
			//日访问次数
			int ipDayVisitTime = 0;
			//从 缓存中查找 ip的小时访问次数
			if(null != cacheUtil.getVpt(IP+"#"+nowHour))
			{
				ipHourVisitTime = (int) (cacheUtil.getVpt(IP+"#"+nowHour));
			}
			else
			{
				ipHourVisitTime = 0;// requestLogDao.queryIpRequestTimesByHour(IP, year, month, day, hour);
			}
			ipHourVisitTime++;

//			cacheUtil.removeVpt(IP+"#"+nowHour);
			cacheUtil.setVpt(IP+"#"+nowHour,ipHourVisitTime);
			
			//从 缓存中查找 ip的日访问次数
			if(null != cacheUtil.getVpt(IP+"#"+nowDay))
			{
				ipDayVisitTime = (int) (cacheUtil.getVpt(IP+"#"+nowDay));
			}
			else
			{
				ipDayVisitTime = 0;//requestLogDao.queryIpRequestTimesByDay(IP, year, month, day);
			}
			ipDayVisitTime++;
//			System.out.println("当前ip"+IP+"当前小时第"+ipHourVisitTime+"次访问");
//			System.out.println("当前ip"+IP+"当日第"+ipHourVisitTime+"次访问");
//			cacheUtil.removeVpt(IP+"#"+nowDay);
			cacheUtil.setVpt(IP+"#"+nowDay,ipDayVisitTime);
			
			//判断是否达到小时预警次数 、每天预警次数
			if(ipHourVisitTime >= IP_HOUR_REQUEST_TIMES)
			{
				if(ipHourVisitTime == IP_HOUR_REQUEST_TIMES || ((ipHourVisitTime - IP_HOUR_REQUEST_TIMES))%IP_WRNING_INTERVAL_TIME == 0)
				{
					sendWarningMessage(IP_WRNING_MOBILE,"IP地址为"+IP+","+day+"日"+hour+"时",ipDayVisitTime);
				}
				
			}
			//判断是否达到小时预警次数 、每天预警次数
			if(ipDayVisitTime >= IP_DAY_REQUEST_TIMES)
			{
				if(ipDayVisitTime == IP_DAY_REQUEST_TIMES || ((ipDayVisitTime - IP_DAY_REQUEST_TIMES))%IP_WRNING_INTERVAL_TIME == 0)
				{
					sendWarningMessage(IP_WRNING_MOBILE,"IP地址为"+IP+","+day+"日",ipDayVisitTime);
				}
				
			}
			//判断是否达到小时限制请求次数、每天限制请求次数
			if(ipHourVisitTime >= IP_LIMIT_HOUR_REQUEST_TIMES )
			{
				System.out.println("ipHourVisitTime:"+ipHourVisitTime);
				return false;
			}
			if(ipDayVisitTime >= IP_LIMIT_DAY_REQUEST_TIMES)
			{
				System.out.println("ipDayVisitTime:"+ipDayVisitTime);
				return false;
			}
		}
		return true;
	}
	

	//判断appId的访问情况  需要告警或者 限制访问
	public boolean judgeAppRequese(String appId,int year,int month,int day,int hour,String nowDay,String nowHour)
	{
		//获取数据库中数据
		//小时访问次数
		int appIdHourVisitTime = 0;
		//日访问次数appId
		int appIdDayVisitTime = 0;
		
		//从 缓存中查找 ip的小时访问次数
		if(null != cacheUtil.getVpt(appId+"#"+nowHour))
		{
			appIdHourVisitTime = (int) (cacheUtil.getVpt(appId+"#"+nowHour));
		}
		else
		{
			appIdHourVisitTime = 0;//requestLogDao.queryAppRequestTimesByHour(appId, year, month, day, hour);
		}
		appIdHourVisitTime++;

//		cacheUtil.removeVpt(appId+"#"+nowHour);
		cacheUtil.setVpt(appId+"#"+nowHour,appIdHourVisitTime);
		
		//从 缓存中查找 ip的日访问次数
		if(null != cacheUtil.getVpt(appId+"#"+nowDay))
		{
			appIdDayVisitTime = (int) (cacheUtil.getVpt(appId+"#"+nowDay));
		}
		else
		{
			appIdDayVisitTime = 0;//requestLogDao.queryAppRequestTimesByDay(appId, year, month, day);
		}
		appIdDayVisitTime++;
//		System.out.println("当前appId为"+appId+"当前小时第"+appIdHourVisitTime+"次访问");
//		System.out.println("当前appId为"+appId+"当日第"+appIdDayVisitTime+"次访问");
//		cacheUtil.removeVpt(appId+"#"+nowDay);
		cacheUtil.setVpt(appId+"#"+nowDay,appIdDayVisitTime);
		
		//判断是否达到小时预警次数 、每天预警次数
		if(appIdHourVisitTime >= APP_HOUR_REQUEST_TIMES)
		{
			if(appIdHourVisitTime == APP_HOUR_REQUEST_TIMES || ((appIdHourVisitTime - APP_HOUR_REQUEST_TIMES))%APP_WRNING_INTERVAL_TIME == 0)
			{
				sendWarningMessage(APP_WRNING_MOBILE,"appId为"+appId+","+day+"日"+hour+"时",appIdHourVisitTime);
			}
			
		}
		//判断是否达到小时预警次数 、每天预警次数
		if(appIdDayVisitTime >= APP_DAY_REQUEST_TIMES)
		{
			if(appIdDayVisitTime == APP_DAY_REQUEST_TIMES || ((appIdDayVisitTime - APP_DAY_REQUEST_TIMES))%APP_WRNING_INTERVAL_TIME == 0)
			{
				sendWarningMessage(APP_WRNING_MOBILE,"appId为"+appId+","+day+"日",appIdDayVisitTime);
			}
			
		}
		
		//判断是否达到小时限制请求次数、每天限制请求次数
		if(appIdHourVisitTime >= APP_LIMIT_HOUR_REQUEST_TIMES )
		{
			System.out.println("appIdHourVisitTime:"+appIdHourVisitTime);
			return false;
		}
		if(appIdDayVisitTime >= APP_LIMIT_DAY_REQUEST_TIMES)
		{
			System.out.println("ipDayVisitTime:"+appIdDayVisitTime);
			return false;
		}

		return true;
	}

	//判断user的访问情况  需要告警或者 限制访问
	public boolean judgeUserRequese(String user,int year,int month,int day,int hour,String nowDay,String nowHour)
	{
		System.out.println("judgeUserRequese");
		//获取数据库中数据
		//小时访问次数
		int userHourVisitTime = 0;
		//日访问次数appId
		int userDayVisitTime = 0;
		//从 缓存中查找 ip的小时访问次数
		if(null != cacheUtil.getVpt(user+"#"+nowHour))
		{
			userHourVisitTime = (int) (cacheUtil.getVpt(user+"#"+nowHour));
		}
		else
		{
			userHourVisitTime = requestLogDao.queryUserRequestTimesByHour(user, year, month, day, hour);
		}
		userHourVisitTime++;

		cacheUtil.removeVpt(user+"#"+nowHour);
		cacheUtil.setVpt(user+"#"+nowHour,userHourVisitTime);
		
		//从 缓存中查找 ip的日访问次数
		if(null != cacheUtil.getVpt(user+"#"+nowDay))
		{
			userDayVisitTime = (int) (cacheUtil.getVpt(user+"#"+nowDay));
		}
		else
		{
			userDayVisitTime = 0;//requestLogDao.queryUserRequestTimesByDay(user, year, month, day);
		}
		userDayVisitTime++;
//		System.out.println("当前appId为"+user+"当前小时第"+userHourVisitTime+"次访问");
//		System.out.println("当前appId为"+user+"当日第"+userDayVisitTime+"次访问");
//		cacheUtil.removeVpt(user+"#"+nowDay);
		cacheUtil.setVpt(user+"#"+nowDay,userDayVisitTime);
		
		//判断是否达到小时预警次数 、每天预警次数
		if(userHourVisitTime >= USER_HOUR_REQUEST_TIMES)
		{
			if(userHourVisitTime == USER_HOUR_REQUEST_TIMES || ((userHourVisitTime - USER_HOUR_REQUEST_TIMES))%USER_WRNING_INTERVAL_TIME == 0)
			{
				sendWarningMessage(USER_WRNING_MOBILE,"user为"+user+","+day+"日"+hour+"时",userHourVisitTime);
			}
			
		}
		//判断是否达到小时预警次数 、每天预警次数
		if(userDayVisitTime >= USER_DAY_REQUEST_TIMES)
		{
			if(userDayVisitTime == USER_DAY_REQUEST_TIMES || ((userDayVisitTime - USER_DAY_REQUEST_TIMES))%USER_WRNING_INTERVAL_TIME == 0)
			{
				sendWarningMessage(USER_WRNING_MOBILE,"user为"+user+","+day+"日",userDayVisitTime);
			}
			
		}

		//判断是否达到小时限制请求次数、每天限制请求次数
		if(userHourVisitTime >= USER_LIMIT_HOUR_REQUEST_TIMES )
		{
			System.out.println("userHourVisitTime:"+userHourVisitTime);
			return false;
		}
		if(userDayVisitTime >= USER_LIMIT_DAY_REQUEST_TIMES)
		{
			System.out.println("userDayVisitTime:"+userDayVisitTime);
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public void addRequestLog(RequestLogBean rb)
	{
		RequestLogBean requestLogBean = requestLogDao.findByIpAndAppIdAndUserInfoAndYearAndMonthAndDayAndHour
				(rb.getIp(), rb.getAppId(), rb.getUserInfo(), rb.getYear(), rb.getMonth(), rb.getDay(), rb.getHour());
		//添加访问记录
		if( null == requestLogBean)
		{
			requestLogDao.save(rb);
		}
		else
		{
			int requestTimes = rb.getRequestTimes() + requestLogBean.getRequestTimes();
			requestLogDao.updateRequestLogTimes(requestLogBean.getId(),requestTimes);
		}
		
	}

	
}
