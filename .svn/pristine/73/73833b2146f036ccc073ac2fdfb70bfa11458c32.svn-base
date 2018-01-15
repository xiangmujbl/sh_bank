package com.mmec.util;
/*
 * 缓存工具类
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.Logger;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CacheUtil
{
	public static CacheManager m;
	//数据缓存
	public static Cache mmecCache;
	//请求统计缓存
	public static Cache vptCache;
	//请求统计缓存
	public static Cache vptConfigCache;
	//存证缓存
	public static Cache evidenceCache;
	public static Cache getVptConfigCache()
	{
		return vptConfigCache;
	}

	private static Logger log = Logger.getLogger(CacheUtil.class);
	
	public static Cache signCache;
	
	// 保全缓存
	public static Cache protectContract;
	
	static {
		String path = System.getProperty("user.dir") + File.separator +"conf"+ File.separator +"ehcache.xml";
		File file = new File(path);
		if (!file.isFile()) {
			path = System.getProperty("user.dir") + File.separator + "src" +  File.separator + "ehcache.xml";
		}
//		m =CacheManager.getInstance();
		try {
			CacheManager.create(path);
			m=CacheManager.getInstance();
		} catch (CacheException e) {
			e.printStackTrace();
		}
		mmecCache = m.getCache("mmecCache");
		signCache = m.getCache("signCache");
		vptCache = m.getCache("vptCache");
		vptConfigCache = m.getCache("vptConfigCache");
		evidenceCache=m.getCache("evidenceCache");
		protectContract = m.getCache("protectContract");
	}
	
	public void set(String key)
	{
		mmecCache.put(new Element(key, key));
	}
	

	public Object get(String key)
	{
		return mmecCache.get(key);
	}
	
	/*
	 * 保全同步使用 
	 * @param key
	 */
	public void setProtectKey(String key)
	{
		protectContract.put(new Element(key, key));
	}
	
	public Object getProtectKey(String key)
	{
		return protectContract.get(key);
	}
	
	/*
	 * 签署同步使用 
	 * @param key
	 */
	public void setSignKey(String key)
	{
		signCache.put(new Element(key, key));
	}
	
	public Object getSignKey(String key)
	{
		return signCache.get(key);
	}
	
	public void setEvidenceKey(String key)
	{
		evidenceCache.put(new Element(key, key));
	}
	
	public Object getEvidenceKey(String key)
	{
		return evidenceCache.get(key);
	}
	
	public static void getAll()
	{
		for(Object o:mmecCache.getKeys()){
			String v = (String)mmecCache.get(o).getValue();
			log.info("key="+((String)mmecCache.get(o).getKey()+":value="+v));
		}	
	}

	public void setVpt(String key,int times)
	{
		vptCache.put(new Element(key, times));
	}

	public void getSignAll()
	{
		for(Object o:signCache.getKeys()){
			String v = (String)signCache.get(o).getValue();
			log.info("key="+((String)signCache.get(o).getKey()+":value="+v));
		}	
	}

	public void removeVpt(String key)
	{
		vptCache.remove(key);
	}
	

	public Object getVpt(String key)
	{
//		if(null != vptCache.get(key))
//		{
//			return vptCache.get(key).getObjectValue();
//		}
//		else
//		{
//			return vptCache.get(key);
//		}
		if(null != vptCache.get(key))
		{
			
			if(null == vptCache.get(key).getValue())
			{
				if(null == vptCache.get(key).getObjectValue())
				{
					return 0;
				}
				else
				{
					return vptCache.get(key).getObjectValue();
				}
			}
			else
			{
				return vptCache.get(key).getValue();
			}
		}
		else
		{
			return null;
		}
		
	}

	public static void getAllVpt()
	{
		for(Object o:vptCache.getKeys()){
			Object v = vptCache.get(o).getValue();
//			log.info("key="+((String)vptCache.get(o).getKey()+":value="+v));
			System.out.println("key="+((String)vptCache.get(o).getKey()+":value="+v));
		}	
	}

	public void setVptConfig(String key,Object value)
	{
		vptConfigCache.put(new Element(key, value));
	}
	

	public void removeVptConfig(String key)
	{
		vptConfigCache.remove(key);
	}
	
	public Object getVptConfig(String key)
	{
		if(null != vptConfigCache.get(key))
		{
			return vptConfigCache.get(key).getObjectValue();
		}
		else
		{
			return vptConfigCache.get(key);
		}
	}
	//读取时枷锁
	public void acquireReadLockOnKey4vptConfigCache(String key)
	{
		vptConfigCache.acquireReadLockOnKey(key);  
	}
	//写入后  释放
	public void releaseReadLockOnKey4vptConfigCache(String key)
	{
		vptConfigCache.releaseReadLockOnKey(key);  
	}
	
	public static void main(String[] args)
	{
//		CacheUtil ut = new CacheUtil();
//		ut.setVpt("key1", 11);
//		ut.setVpt("key2", 12);
//		ut.setVpt("key3", 13);
//		ut.setVpt("key4", 14);
//		ut.getAllVpt();
//		System.out.println("===========================");
//		ut.setVpt("key1", 21);
//		ut.setVpt("key2", 22);
//		ut.setVpt("key3", 23);
//		ut.setVpt("key4", 24);
//		ut.setVpt("key5", 25);
//		ut.getAllVpt();
//		System.out.println("===========================");
//		ut.setVpt("key1", 31);
//		ut.setVpt("key2", 32);
//		ut.setVpt("key3", 33);
//		ut.setVpt("key4", 34);
//		ut.setVpt("key5", 35);
//		ut.setVpt("key6", 36);
//		ut.getAllVpt();
//		System.out.println("===========================");
//		ut.setVpt("key1", 31);
//		ut.setVpt("key1", 32);
//		ut.setVpt("key1", 33);
//		ut.setVpt("key1", 34);
//		ut.setVpt("key1", 35);
//		ut.setVpt("key1", 36);
//		ut.getAllVpt();
		
		Cache ss =  CacheManager.getInstance().getCache("certNum");
		List list = ss.getKeys();
		Element e =  ss.get("certNum");
		System.out.println(list.size());
		
//		CacheManager cm = CacheManager.getInstance();
//		Cache cache = cm.getCache("signCache");
//		Element e = new Element("key1", "Udz2ILyzx7#test20160707001");
//		cache.put(e);
//		Element e2 = cache.get(e);
		System.out.println(e.getObjectKey());
	}
}