package com.mmec.centerService.userModule.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mmec.centerService.userModule.dao.OptLogDao;
import com.mmec.centerService.userModule.entity.OptLogEntity;
import com.mmec.centerService.userModule.service.LogService;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantUtil;
@Service("logService")
public class LogServiceImpl implements LogService {
	
	private static String serverIp = "";
	
	static
	{
		serverIp = getLocalIP();
	}
	
	@Autowired
	private OptLogDao optLogDao;
	@Override
	public void log(Map<String, String> datamap,String optType,ServiceException se
			,ReturnData rd) throws ServiceException {
		byte result = 0;
		String message = "";
		String detail ="";
		String level ="info";
		
		OptLogEntity opt = new OptLogEntity();
		opt.setOptFrom("CenterService");
		opt.setAppId(datamap.get("appId"));
		if(null!=datamap.get("appid")){
			opt.setAppId(datamap.get("appid"));
		}
		String userAccount = "";
		if(null != datamap.get("account"))
		{
			userAccount = datamap.get("account");
		}
		else if(null != datamap.get("platformUserName")  && "".equals(userAccount))
		{
			userAccount = datamap.get("platformUserName");
		}
		else if(null != datamap.get("userid")  && "".equals(userAccount))
		{
			userAccount = datamap.get("userid");
		}
		opt.setUserAccount(userAccount);
		opt.setOptType(optType);
		opt.setOptTime(new Date());
		if(!"".equals(serverIp))
		{
			opt.setServerIp(serverIp);
		}
		if(null!=datamap.get("serverIp")&&!"".equals(datamap.get("serverIp"))){
			if(null!=opt.getServerIp()&&!"".equals(opt.getServerIp())){
				
			}else{
				opt.setServerIp(datamap.get("serverIp"));
			}
		}
		if( null != se)
		{
		message = se.getErrorDesc();
	    detail = se.getDetail();
		}
		opt.setOptResult(result);
		opt.setLevel(level);
		String inParmStr = datamap.toString();
//		if(inParmStr.length()>1000)
//		{
//			inParmStr = inParmStr.substring(0,1000)+"... ...";
//		}
		opt.setInParam(inParmStr);
		String outParmStr = "";
		if(null != rd)
		{
			outParmStr = rd.toString();
		}
//		if(outParmStr.length()>1000)
//		{
//			outParmStr = outParmStr.substring(0,1000)+"... ...";
//		}
		opt.setOutParam(outParmStr);
		opt.setMessage(message);
//		if(detail.length()>4500)
//		{
//			opt.setDetail(detail.substring(0,4500)+"... ...");
//		}
//		else
//		{
			opt.setDetail(detail);
//		}
		try {
			optLogDao.save(opt);
		} catch (Exception e) {
			e.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
	}
	
	//获取本机IP
    public static String getLocalIP() {
        String ip = "";
        try {
            Enumeration<?> e1 = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
            while (e1.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) e1.nextElement();
                if (!ni.getName().equals("eth0")) {
                    continue;
                } else {
                    Enumeration<?> e2 = ni.getInetAddresses();
                    while (e2.hasMoreElements()) {
                        InetAddress ia = (InetAddress) e2.nextElement();
                        if (ia instanceof Inet6Address)
                            continue;
                        ip = ia.getHostAddress();
                    }
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return ip;
    }
    
	@Override
	public void log(Map<String, String> datamap,String optType,ServiceException se
			,String rdStr) throws ServiceException {
		byte result = 0;
		String message = "";
		String detail ="";
		String level ="info";
		
		OptLogEntity opt = new OptLogEntity();
		opt.setOptFrom(datamap.get("optFrom"));
		opt.setAppId(datamap.get("appId"));
		if(null!=datamap.get("appid")){
			opt.setAppId(datamap.get("appid"));
		}
		String userAccount = "";
		if(null != datamap.get("account"))
		{
			userAccount = datamap.get("account");
		}
		else if(null != datamap.get("platformUserName")  && "".equals(userAccount))
		{
			userAccount = datamap.get("platformUserName");
		}
		else if(null != datamap.get("userid")  && "".equals(userAccount))
		{
			userAccount = datamap.get("userid");
		}
		opt.setUserAccount(userAccount);
		opt.setOptType(optType);
		opt.setOptTime(new Date());
		if(!"".equals(serverIp))
		{
			opt.setServerIp(serverIp);
		}
		if(null!=datamap.get("serverIp")&&!"".equals(datamap.get("serverIp"))){
			if(null!=opt.getServerIp()&&!"".equals(opt.getServerIp())){
				
			}else{
				opt.setServerIp(datamap.get("serverIp"));
			}
		}
		if( null != se)
		{
		message = se.getErrorDesc();
	    detail = se.getDetail();
		}
		opt.setOptResult(result);
		opt.setLevel(level);
		String inParmStr = datamap.toString();
//		if(inParmStr.length()>1000)
//		{
//			inParmStr = inParmStr.substring(0,1000)+"... ...";
//		}
		opt.setInParam(inParmStr);
		String outParmStr = "";
		outParmStr = rdStr;
//		if(outParmStr.length()>1000)
//		{
//			outParmStr = outParmStr.substring(0,1000)+"... ...";
//		}
		opt.setOutParam(outParmStr);
		opt.setMessage(message);
//		if(detail.length()>4500)
//		{
//			opt.setDetail(detail.substring(0,4500)+"... ...");
//		}
//		else
//		{
			opt.setDetail(detail);
//		}
		try {
			optLogDao.save(opt);
		} catch (Exception e) {
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
					ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
		}
	}

}
