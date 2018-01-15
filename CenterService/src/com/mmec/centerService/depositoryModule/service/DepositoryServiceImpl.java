package com.mmec.centerService.depositoryModule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

import com.google.gson.Gson;
import com.mmec.centerService.contractModule.service.DownloadService;
import com.mmec.centerService.contractModule.service.impl.ContractServiceImpl;
import com.mmec.centerService.userModule.service.LogService;
import com.mmec.centerService.userModule.service.MyAttnService;
import com.mmec.centerService.vpt.service.VptService;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.FeeRMIServices.Iface;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.CacheUtil;
import com.mmec.util.ConstantUtil;

@ContextConfiguration({ "/applicationContext.xml" })

@Component("depositoryIface")
/**
 * 中央承载存证接口
 * @author Administrator
 *
 */
public class DepositoryServiceImpl implements com.mmec.thrift.service.DepositoryRMIServices.Iface {
	
	@Autowired
	private EvidenceService  evidenceService;
	
	/**
	 * 记录日志服务
	 */
	@Autowired
	private LogService logService;	
	
	/**
	 * 请求拦截服务
	 */
	@Autowired
	private VptService vptService;	
	
	private Logger log = Logger.getLogger(DepositoryServiceImpl.class);
	
	/**
	 * 用户检查,方法暂时空置
	 */
	public ReturnData userCheck(Map<String,String> map){
		ReturnData rd=rd=new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1],ConstantUtil.RETURN_SUCC[2],"");
		return rd;
	};
	
	/**
	 * 用户注册,方法暂时空置
	 */
	public ReturnData registerUser(Map<String,String> map){
		ReturnData rd=rd=new ReturnData(ConstantUtil.RETURN_SUCC[0],ConstantUtil.RETURN_SUCC[1],ConstantUtil.RETURN_SUCC[2],"");
		return rd;
	};
	
	/**
	 * 保存存证信息
	 */
	public ReturnData uploadEvidence(Map<String,String> map){
		ReturnData rd=new ReturnData();
		ServiceException retException = null;
		//告警阀值
		boolean judge = vptService.dealRequest(map, Thread.currentThread() .getStackTrace()[1].getMethodName());
		try{
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}else{
				if(creatEvSynchronousLock(map))
				{
					rd.setRetCode(ConstantUtil.REPEAT_UPLOAD_EVIDENCE[0]);
					rd.setDesc(ConstantUtil.REPEAT_UPLOAD_EVIDENCE[1]);
					rd.setDescEn(ConstantUtil.REPEAT_UPLOAD_EVIDENCE[2]);
					log.info("repeat upload:"+map.toString());
				}
				else
				{
					rd=evidenceService.saveEvidence(map);
				}
			}
//			return rd;	
		}catch(ServiceException e){
			retException=e;
			e.printStackTrace();
			rd=new ReturnData(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),"");
//			return rd;	
		}catch(TException e){
			e.printStackTrace();
			rd=new ReturnData(ConstantUtil.SIGN_FAILURE[0],ConstantUtil.SIGN_FAILURE[1]
					,ConstantUtil.SIGN_FAILURE[2],"");
//			return rd;	
		}finally{
			try{
				logService.log(map,"uploadEvidence", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		return rd;
	}
	
	/**
	 * 查看单个存证信息
	 */
	public ReturnData evidenceDetail(Map<String,String> map){
		ReturnData rd=new ReturnData();
		ServiceException retException = null;
		//告警阀值
		boolean judge = vptService.dealRequest(map, Thread.currentThread() .getStackTrace()[1].getMethodName());
		try{
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}else{
				rd=evidenceService.evidenceDetail(map);
			}
//		return rd;
		}catch(ServiceException e){
			retException=e;
			e.printStackTrace();
			rd=new ReturnData(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),"");
			
		}finally{
			try{
				logService.log(map,"evidenceDetail", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		return rd;
	}
	
	/**
	 * 下载证据
	 */
	public ReturnData downloadEvidence(Map<String,String> map){
		ReturnData rd=new ReturnData();
		ServiceException retException = null;
		//告警阀值
		boolean judge = vptService.dealRequest(map, Thread.currentThread() .getStackTrace()[1].getMethodName());
		try{
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}else{
				rd=evidenceService.downloadEvidence(map);
			}
		}catch(ServiceException e){
			retException=e;
			e.printStackTrace();
			rd=new ReturnData(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),"");
		}finally{
			try{
				logService.log(map,"downloadEvidence", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		return rd;
	}
	
	/**
	 * 分页查看存证信息
	 * @param args
	 */
	public ReturnData queryEvidenceByPage(Map<String,String> map){
		ReturnData rd=new ReturnData();
		ServiceException retException = null;
		//告警阀值
//		boolean judge = vptService.dealRequest(map, Thread.currentThread() .getStackTrace()[1].getMethodName());
		boolean judge=true;
		try{
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}else{
				rd=evidenceService.pageEvidence(map);
			}
		}catch(ServiceException e){
			retException=e;
			e.printStackTrace();
			rd=new ReturnData(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),"");
		}finally{
			try{
				logService.log(map,"queryEvidenceByPage", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		return rd;
	}
	
	/**
	 * 查看存证详细--云签
	 */
	public ReturnData evidenceDetailForYunSign(Map<String,String> map){
		ReturnData rd=new ReturnData();
		ServiceException retException = null;
		//告警阀值
		boolean judge = vptService.dealRequest(map, Thread.currentThread() .getStackTrace()[1].getMethodName());
		try{
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}else{
				rd=evidenceService.evidenceDetailForYunSign(map);
			}
//		return rd;
		}catch(ServiceException e){
			retException=e;
			e.printStackTrace();
			rd=new ReturnData(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),"");
//			return rd;
		}finally{
			try{
				logService.log(map,"evidenceDetail", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		return rd;
	}
	
	/**
	 * 查看存证报告
	 * @param args
	 */
	public ReturnData queryEvidenceReport(Map<String,String> map){
		ReturnData rd=new ReturnData();
		ServiceException retException = null;
		//告警阀值
		boolean judge = vptService.dealRequest(map, Thread.currentThread() .getStackTrace()[1].getMethodName());
		try{
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}else{
				rd=evidenceService.queryEvidenceReport(map);
			}
		}catch(ServiceException e){
			
		}finally{
			try{
				logService.log(map,"evidenceDetail", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		return rd;
	}
	
	
	/**
	 * 控制签署同步锁
	 * @param datamap
	 * @return
	 */
	public synchronized boolean creatEvSynchronousLock(Map<String, String> datamap)
	{
		String checkStr = "evidence#"+datamap.get("appid")+"#"+datamap.get("orderid");
		CacheUtil cacheUtil = new CacheUtil();
//		cacheUtil.getSignAll();
		//判断当前合同是否在签署
		if(null != (cacheUtil.getEvidenceKey(checkStr)))
		{
			return true;			
		}
		else
		{
			cacheUtil.setEvidenceKey(checkStr);
			return false;
		}		
	}
	
	public static void main(String []args){
		Gson g=new Gson();
		List<String> list=new ArrayList<String>();
		list.add("1");
		list.add("2");
		System.out.println(g.toJson(list));
		
		List<String> list1=g.fromJson(g.toJson(list), List.class);
		System.out.println(g.toJson(list1));
	}
}
