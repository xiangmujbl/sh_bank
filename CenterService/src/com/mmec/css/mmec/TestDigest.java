package com.mmec.css.mmec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.mmec.css.credlink.TSA;

public class TestDigest {
	public static void main(String[] args){
		
		try {
			MessageDigest digest =  MessageDigest.getInstance("SHA");
			
			TSA tsa = new TSA();
			String time = tsa.createTSA("CONTSERIALNUM=CP4163705440683536&CERTFINGERPRINT=BC5611A343783D8789A86D9E7568AC740A407715");
			System.out.println(time);
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
