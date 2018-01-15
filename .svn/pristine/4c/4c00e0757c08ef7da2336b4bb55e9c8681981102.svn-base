package com.mmec.test.userClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolExecutorTest {
	public static void main(String[] args) {  
		  ExecutorService fixedThreadPool = Executors.newFixedThreadPool(100);  
		  for (int i = 0; i < 1; i++) {  
			  fixedThreadPool.execute(new Runnable() {  
				  public void run() {  
					    try {  
					    	new UserClient().test_getCustomByMobile();
					    } catch (Exception e) {  
					    	e.printStackTrace();  
					    }  
				  }  
		     });  
	     }  
	 }  
}
