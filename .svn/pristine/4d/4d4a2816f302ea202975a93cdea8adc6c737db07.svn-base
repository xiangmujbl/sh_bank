package com.mmec.test;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class UserCacheTest
{
	public static void main(String[] args)
	{
		CacheManager m =CacheManager.create();
		Cache c = m.getCache("userCache");
		c.put(new Element("userName", "huyaoc"));
		for(Object o:c.getKeys()){
			String v = (String)c.get(o).getValue();
			System.out.println((String)c.get(o).getKey()+":"+v);
		}	

	}
}
