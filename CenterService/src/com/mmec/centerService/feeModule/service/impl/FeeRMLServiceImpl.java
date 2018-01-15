/*
 * 计费类远程接口实现类
 * optType:'query','update','delete','add'
 */
package com.mmec.centerService.feeModule.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.mmec.centerService.feeModule.entity.ContractDeductRecordEntity;
import com.mmec.centerService.feeModule.entity.OrderRecordEntity;
import com.mmec.centerService.feeModule.entity.PayServiceEntity;
import com.mmec.centerService.feeModule.entity.UserAccountEntity;
import com.mmec.centerService.feeModule.entity.UserServiceEntity;
import com.mmec.centerService.feeModule.service.ContractDeductRecordService;
import com.mmec.centerService.feeModule.service.FeeRMLService;
import com.mmec.centerService.feeModule.service.InvoiceInfoService;
import com.mmec.centerService.feeModule.service.OrderRecordService;
import com.mmec.centerService.feeModule.service.PayService;
import com.mmec.centerService.feeModule.service.UserAccountService;
import com.mmec.centerService.feeModule.service.UserService;
import com.mmec.centerService.userModule.entity.InvoiceInfoEntity;
import com.mmec.centerService.userModule.service.LogService;
import com.mmec.centerService.vpt.service.VptService;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.CertUtil;
import com.mmec.util.CheckUtil;
import com.mmec.util.ConstantUtil;
import com.mmec.util.DeductRecord;
import com.mmec.util.JSONUtil;
import com.mmec.util.OrderTradingShield;
/**
 * 备忘  查询消费记录明细的时候要带合同标题,查询的时候也有合同标题
 * @author Administrator
 *
 */
@Component("feeIface")
public class FeeRMLServiceImpl  implements  FeeRMLService
{	
	private static Logger log=Logger.getLogger(FeeRMLServiceImpl.class);
	
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PayService payService;
	
	@Autowired
	private ContractDeductRecordService contractDeductRecordService;
	
	@Autowired
	private OrderRecordService orderRecordService;
	
	@Autowired
	private InvoiceInfoService invoiceInfoService;
	
	@Autowired
	private LogService logService;	
	
	/**
	 * 请求拦截服务
	 */
	@Autowired
	private VptService vptService;	
	
	/**
	 * 0查询用户余额 for 云签
	 * @param map 
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData queryUserAccountDetail(Map<String,String> map){
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
			Gson gson = new Gson();
			//余额次数,充值总次数,历史扣除次数
			int banlance=0,rechargeSum=0,deductSum=0;
			//余额次数
			UserServiceEntity us= userService.queryByUserIdAndPayCode(Integer.valueOf(map.get("userid"))
					, map.get("paycode"));
			if(null!=us){
				banlance=us.getChargingTimes();
			}
			//充值总次数
//			List<ContractDeductRecordEntity> cdlist=contractDeductRecordService.queryType(
//					map.get("paycode"), Integer.valueOf(map.get("userid")), (byte)6);
//			if(null!=cdlist&&cdlist.size()>0){
//			for(int i=0;i<cdlist.size();i++){
//				rechargeSum=rechargeSum+cdlist.get(i).getDeductTimes();
//			}
//			}
			
			//历史扣除次数
			//扣次
			List<ContractDeductRecordEntity> cdlist=contractDeductRecordService.queryType(
					map.get("paycode"), Integer.valueOf(map.get("userid")), (byte)3);
			
			if(null!=cdlist&&cdlist.size()>0){
			for(int i=0;i<cdlist.size();i++){
				deductSum=deductSum+cdlist.get(i).getDeductTimes();
			}
			}
			
			//退次
			int tuici=0;
			List<ContractDeductRecordEntity> alist=contractDeductRecordService.queryType(
					map.get("paycode"), Integer.valueOf(map.get("userid")), (byte)5);
			if(null!=alist&&alist.size()>0){
				for(int i=0;i<alist.size();i++){
					tuici=tuici+alist.get(i).getDeductTimes();
				}
			}
			
			//后台冲次
			int backchongci=0;
			List<ContractDeductRecordEntity> blist=contractDeductRecordService.queryType(
					map.get("paycode"), Integer.valueOf(map.get("userid")), (byte)2);
			if(null!=blist&&blist.size()>0){
				for(int i=0;i<blist.size();i++){
					backchongci=backchongci+blist.get(i).getDeductTimes();
				}
			}
			
			//云签冲次
			int yunsignci=0;
			List<ContractDeductRecordEntity> clist=contractDeductRecordService.queryType(
					map.get("paycode"), Integer.valueOf(map.get("userid")), (byte)6);
			if(null!=clist&&clist.size()>0){
				for(int i=0;i<clist.size();i++){
					yunsignci=yunsignci+clist.get(i).getDeductTimes();
				}
			}
			
			
			//历史扣除次数
			deductSum=deductSum-tuici;
			
			//充值总次数
			rechargeSum=backchongci+yunsignci;
			
			Map resmap=new HashMap();
			//余额次数
			resmap.put("banlance", banlance);
			//充值总次数
			resmap.put("rechargeSum", rechargeSum);
			//历史扣除次数
			resmap.put("deductSum", deductSum);
			rd=new ReturnData("0000","success","",gson.toJson(resmap));
			}
		}catch(Exception e){
			e.printStackTrace();
			retException = new ServiceException(e);
			rd= new ReturnData(ConstantUtil.RETURN_SYSTEM_ERROR[0],
					ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],
					"");
		}finally{
			try{
				logService.log(map,"queryUserAccountDetail", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		 return rd;
	}
	
	
	/**
	 * 1
	 * 查询用户余额List
	 * @param 用户ID
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData queryUserAccount(int userid){
		ReturnData rd=new ReturnData();
		ServiceException retException = null;
		Map<String,String> mymap=new HashMap<String,String>();
		mymap.put("userid", String.valueOf(userid));
		//告警阀值
		boolean judge = vptService.dealRequest(mymap, Thread.currentThread() .getStackTrace()[1].getMethodName());
		try{
		if(!judge)
		{
			rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
			rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
			rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
		}else{
		Gson gson=new Gson();
		log.info("entry method queryUserAccount,params:"+String.valueOf(userid));
		//获取服务代码集合
		List<PayServiceEntity> pslist=payService.queryAll();
		for(int i=0;i<pslist.size();i++){
		userAccountService.checkUserAccount(userid, ConstantUtil.ZERO_MONEY,pslist.get(i).getTypeCode());
		}
		List<UserAccountEntity> pojo=userAccountService.queryUserAccountList(userid);
		rd=new ReturnData("0000","success","",gson.toJson(pojo));
		}
		}catch(ServiceException e){
			e.printStackTrace();
			retException=new ServiceException(e);
			rd=new ReturnData(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),"");
		}finally{
			try{
				Map<String,String> map=new HashMap<String,String>();
				map.put("userid", String.valueOf(userid));
				logService.log(map,"queryUserAccount", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		return rd;
	}
	
	
	/**
	 * 2
	 * 给账户 充值+paycode   /兼退费接口
	 * @param map(userid--用户ID,money--金额)  /退费的时候多传(paycode--"标记此次退费的服务编码" payid--"标记此次退费的服务对应的流水号"(如果不传paycode,此字段无效))
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData addMoney(Map<String,String> map){
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
		log.info("entry method addMoney");
		if(!CheckUtil.checkMap(map,"userid,money,paycode","userid,money,paycode").getRetCode().equals("0000")){
			return CheckUtil.checkMap(map,"userid,money,paycode","userid,money,paycode");
		}
		String useridStr=map.get("userid");
		String moneyStr=map.get("money");
		String paycode=map.get("paycode");
		if(!CheckUtil.intCheck(useridStr).equals("success")){
			return new ReturnData("9999",CheckUtil.intCheck(useridStr),"","");
		}
		if(!CheckUtil.moneyCheck(moneyStr).equals("success")){
			return new ReturnData("9999",CheckUtil.moneyCheck(moneyStr),"","");
		}
		BigDecimal money=new BigDecimal(moneyStr);
		int userid=Integer.valueOf(useridStr);
		userAccountService.checkUserAccount(userid, ConstantUtil.ZERO_MONEY,paycode);
		userAccountService.addMoney(userid, money,paycode);
		ContractDeductRecordEntity cd=new ContractDeductRecordEntity();
		cd.setBillNum(String.valueOf(System.currentTimeMillis()));
		cd.setUserid(userid);
		cd.setConsumeType((byte)0);
		cd.setUpdateTime(new Date());
		cd.setDeductSum(money);
		if(null!=map.get("refund")){
			cd.setConsumeType((byte)4);
		}
		if(null!=map.get("payid")){
			cd.setPayId(map.get("payid"));
		}
		contractDeductRecordService.saveRecord(cd);
		rd=new ReturnData("0000","success","","");
		}
		}catch(ServiceException e){
			e.printStackTrace();
			retException=new ServiceException(e);
			rd=new ReturnData(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),"");
		}finally{
			try{
				logService.log(map,"addMoney", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		return rd;
	}


	/**
	 * 3
	 * 给账户扣费
	 * @param map(userid--用户ID,money--金额,paycode--扣费购买的服务编码(必须传paycode,每一份扣费都要查到扣费的原因,到底是购买什么服务,而且paycode要做校验)
	 * ,payid--选传)
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData reduceMoney(Map<String,String> map){
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
		log.info("entry method reduceMoney,params:"+map.toString());
		if(!CheckUtil.checkMap(map,"userid,money,paycode","userid,money,paycode").getRetCode().equals("0000")){
			 rd=CheckUtil.checkMap(map,"userid,money,paycode","userid,money,paycode");
			 return rd;
		}
		String useridStr=map.get("userid");
		String moneyStr=map.get("money");
		String paycode=map.get("paycode");
		if(!CheckUtil.intCheck(useridStr).equals("success")){
			rd=new ReturnData("9999",CheckUtil.intCheck(useridStr),"","");
			return rd;
		}
		if(!CheckUtil.moneyCheck(moneyStr).equals("success")){
			rd=new ReturnData("9999",CheckUtil.moneyCheck(moneyStr),"","");
			return rd;
		}
		if(null==payService.queryByPayCode(map.get("paycode"))){
			rd=new ReturnData(ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[0],
					map.get("paycode")+ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[1],
					ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[2],"");
			return rd;
		}
		BigDecimal money=new BigDecimal(moneyStr);
		int userid=Integer.valueOf(useridStr);
		userAccountService.checkUserAccount(userid, ConstantUtil.ZERO_MONEY,paycode);
		userAccountService.reduceMoney(userid, money,paycode);
		ContractDeductRecordEntity cd=new ContractDeductRecordEntity();
		cd.setBillNum(String.valueOf(System.currentTimeMillis()));
		cd.setUserid(userid);
		cd.setConsumeType((byte)1);
		cd.setUpdateTime(new Date());
		cd.setDeductSum(money);
		cd.setTypecode(map.get("paycode"));
		if(null!=map.get("payid")){
			cd.setPayId(map.get("payid"));
		}
		contractDeductRecordService.saveRecord(cd);
		rd=new ReturnData("0000","success","","");
		}
		}catch(ServiceException e){
			e.printStackTrace();
			retException=new ServiceException(e);
			rd=new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");
		}finally{
			try{
				logService.log(map,"reduceMoney", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		return rd;
	}
	
	
	/**
	 * 4
	 * 查询用户的服务次数
	 * @param map(userid--用户ID,paycode--服务编码)
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData queryUserServe(Map<String,String> map){
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
		log.info("entry method queryUserServe,params:"+map.toString());
		if(!CheckUtil.checkMap(map, "userid,paycode","userid,paycode").getRetCode().equals("0000")){
			rd=CheckUtil.checkMap(map, "userid,paycode","userid,paycode");
			return rd;
		}
		String useridStr=map.get("userid");
		String paycode=map.get("paycode");
		if(null==payService.queryByPayCode(map.get("paycode"))){
			rd=new ReturnData(ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[0],
					map.get("paycode")+ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[1],
					ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[2],"");
			return rd;
		}
		if(!CheckUtil.intCheck(useridStr).equals("success")){
			rd=new ReturnData("9999",CheckUtil.intCheck(useridStr),"","");
			return rd;
		}
		int userid=Integer.valueOf(useridStr);
		userService.checkUserService(userid, paycode,1);
		UserServiceEntity us=userService.queryByUserIdAndPayCode(userid, paycode);
		rd=new ReturnData("0000","success",JSONUtil.Obj2String(us),String.valueOf(us.getChargingTimes()));
		}}catch(ServiceException e){
			e.printStackTrace();
			retException=new ServiceException(e);
			rd=new ReturnData(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),"");
		}finally{
			try{
				logService.log(map,"queryUserServe", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		return rd;
	}
	
	/**
	 * 5
	 * 给用户某个服务冲次数   /兼退次接口
	 * @param map(userid--用户ID,paycode--服务编码,times--充值次数,money--充值金额)
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData addServeTimes(Map<String,String> map){
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
		log.info("entry method addServeTimes,params:"+map.toString());
		if(!CheckUtil.checkMap(map,"userid,paycode,times,money","userid,paycode,times,money").getRetCode().equals("0000")){
			rd=CheckUtil.checkMap(map, "userid,paycode,times,money","userid,paycode,times,money");
			return rd;
		}
		String useridStr=map.get("userid");
		String paycode=map.get("paycode");
		String timeStr=map.get("times");
		String moneyStr=map.get("money");
		if(!CheckUtil.intCheck(useridStr).equals("success")){
			rd=new ReturnData("9999",CheckUtil.intCheck(useridStr),"","");
			return rd;
		}
		if(!CheckUtil.naturalnumberCheck(timeStr).equals("success")){
			rd=new ReturnData("9999",CheckUtil.naturalnumberCheck(timeStr),"","");
			return rd;
		}
		if(!CheckUtil.moneyCheck(moneyStr).equals("success")){
			rd=new ReturnData("9999",CheckUtil.moneyCheck(moneyStr),"","");
			return rd;
		}
		if(null==payService.queryByPayCode(map.get("paycode"))){
			rd=new ReturnData(ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[0],
					map.get("paycode")+ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[1],
					ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[2],"");
			return rd;
		}
		int userid=Integer.valueOf(useridStr);
		int times=Integer.valueOf(timeStr);
		BigDecimal money=new BigDecimal(moneyStr);
		userAccountService.checkUserAccount(userid, ConstantUtil.ZERO_MONEY,paycode);
		userAccountService.reduceMoney(userid, money,paycode);
		userService.checkUserService(userid, paycode,1);
		userService.addUserServiceTimes(userid, paycode, times);
		ContractDeductRecordEntity cd=new ContractDeductRecordEntity();
		cd.setBillNum(String.valueOf(System.currentTimeMillis()));
		cd.setUserid(userid);
		cd.setConsumeType((byte)2);
		cd.setUpdateTime(new Date());
		cd.setDeductSum(money);
		cd.setDeductTimes(times);
		cd.setTypecode(paycode);
		if(null!=map.get("payid")){
			cd.setPayId(map.get("payid"));
			cd.setConsumeType((byte)5);
		}
		contractDeductRecordService.saveRecord(cd);
		rd=new ReturnData("0000","success","","");
		}}
		catch(ServiceException e){
			e.printStackTrace();
			retException=new ServiceException(e);
			rd=new ReturnData(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),"");
		}finally{
			try{
				logService.log(map,"addServeTimes", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		return rd;
	}
	
	
	/**
	 * 6
	 * 给用户某个服务扣次数
	 * @param map(userid--用户id,paycode--服务编码,times--扣费次数,payid--扣费服务对应的流水号)
	 * 如果是
	 */
	@Override
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData reduceServeTimes(Map<String,String> map){
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
		Gson gson=new Gson();
		log.info("entry method reduceServeTimes,params:"+map.toString());
		if(!CheckUtil.checkMap(map, "userid,paycode,times,payid","userid,paycode,times,payid").getRetCode().equals("0000")){
			rd=CheckUtil.checkMap(map, "userid,paycode,times,payid","userid,paycode,times,payid");
			return rd;
		}
		String useridStr=map.get("userid");
		String paycode=map.get("paycode");
		String timeStr=map.get("times");
		if(!CheckUtil.intCheck(useridStr).equals("success")){
			rd= new ReturnData("9999",CheckUtil.intCheck(useridStr),"","");
			return rd;
		}
		if(!CheckUtil.naturalnumberCheck(timeStr).equals("success")){
			rd= new ReturnData("9999",CheckUtil.naturalnumberCheck(timeStr),"","");
			return rd;
		}
		if(null==payService.queryByPayCode(map.get("paycode"))){
			rd=new ReturnData(ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[0],
					map.get("paycode")+ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[1],
					ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[2],"");
			return rd;
		}
		int userid=Integer.valueOf(useridStr);
		int times=Integer.valueOf(timeStr);
		userService.checkUserService(userid, paycode,1);
		userService.reduceUserServiceTimes(userid, paycode, times);
		ContractDeductRecordEntity cd=new ContractDeductRecordEntity();
		cd.setBillNum(String.valueOf(System.currentTimeMillis()));
		cd.setUserid(userid);
		cd.setConsumeType((byte)3);
		cd.setUpdateTime(new Date());
		cd.setDeductTimes(times);
		cd.setTypecode(paycode);
		cd.setDeductSum(ConstantUtil.ZERO_MONEY);
		cd.setPayId(map.get("payid"));
		if(null==map.get("bqYears")&&null==map.get("bqStartDate")&&null==map.get("bqEndDate")){
			
		}else{
		Map<String,String> bqtext=new HashMap<String,String>();
		if(null!=map.get("bqYears"))
			bqtext.put("bqYears", map.get("bqYears"));
		if(null!=map.get("bqStartDate"))
			bqtext.put("bqStartDate", map.get("bqStartDate"));
		if(null!=map.get("bqEndDate"))
			bqtext.put("bqEndDate", map.get("bqEndDate"));
		cd.setBqtext(gson.toJson(JSON.toJSON(bqtext)));
		}
		contractDeductRecordService.saveRecord(cd);
		rd=new ReturnData("0000","success","","");
		}}catch(ServiceException e){
			e.printStackTrace();
			retException=new ServiceException(e);
			rd=new ReturnData(e.getErrorCode(),e.getErrorDesc(),e.getErrorDescEn(),"");
		}finally{
			try{
				logService.log(map,"reduceServeTimes", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		return rd;
	}
	
	/**
	 * 7
	 * 查询当前服务种类
	 * @param map(paycode--服务编码)
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData queryPayServe(Map<String,String> map){
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
		Gson gson=new Gson();
		log.info("entry method queryPayServe,params:"+map.toString());
		if(null!=map.get("queryall")){
			List<PayServiceEntity> li=payService.queryAll();
			if(null!=li&&li.size()>0){
				rd=new ReturnData("0000","success","",gson.toJson(li));
			}else{
				rd=new ReturnData("0000","success","","");
			}
			return rd;
		}
		if(!CheckUtil.checkMap(map, "paycode", "paycode").getRetCode().equals("0000")){
			rd=CheckUtil.checkMap(map, "paycode", "paycode");
			return rd;
		}
		PayServiceEntity ps=payService.queryByPayCode(map.get("paycode"));
		if(null==ps){
			rd=new ReturnData(ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[0],
					map.get("paycode")+ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[1],
					ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[2],"");
		}else{
			rd=new ReturnData("0000","success","",JSONUtil.Obj2String(ps));
		}
		}}catch(Exception e){
			e.printStackTrace();
			retException=new ServiceException(e);
			rd=new ReturnData(ConstantUtil.RETURN_SYSTEM_ERROR[0],
					ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],"");
		}finally{
			try{
				logService.log(map,"queryPayServe", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		return rd;
	}
	
	/**
	 * 8
	 * 增加一个服务种类
	 * @param map(typecode--服务编码,typename--服务名称,typecontractname--服务对应的表名称(比如合同服务会对应一张m_contract表来记录合同信息),typedesc--服务描述)
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData addPayServe(Map<String,String> map){
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
			log.info("entry method addPayServe,params:"+map.toString());
		if(!CheckUtil.checkMap(map,"typecode,typename,typedesc,typecontractname","typecode,typename,typedesc,typecontractname").getRetCode().equals("0000")){
			rd=CheckUtil.checkMap(map,"typecode,typename,typedesc,typecontractname","typecode,typename,typedesc,typecontractname");
			return rd;
		}
		if(null!=payService.queryByPayCode(map.get("typecode"))){
			rd=new ReturnData(ConstantUtil.PAYCODE_ALREADY_EXISTS[0],
					map.get("typecode")+ConstantUtil.PAYCODE_ALREADY_EXISTS[1],
					ConstantUtil.PAYCODE_ALREADY_EXISTS[2],"");
			return rd;
		}
		if(null==map.get("typename")){
			map.put("typename", "");
		}
		if(null==map.get("typedesc")){
			map.put("typedesc", "");
		}
		if(null==map.get("typecontractname")){
			map.put("typecontractname", "");
		}
		PayServiceEntity ps=new PayServiceEntity();
		ps.setTypeCode(map.get("typecode"));
		ps.setTypeContractname(map.get("typecontractname"));
		ps.setTypeName(map.get("typename"));
		ps.setTypeDesc(map.get("typedesc"));
		payService.savePayService(ps);
		rd=new ReturnData("0000","success","","");
		}}catch(ServiceException e){
			e.printStackTrace();
			retException=new ServiceException(e);
			rd= new ReturnData(e.getErrorCode(),e.getErrorDesc()
					,e.getErrorDescEn(),"");
		}finally{
			try{
				logService.log(map,"addPayServe", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		return rd;
	}
	
	/**
	 * 9
	 * 更新服务名称
	 * @param map()
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData updatePayServe(Map<String,String> map){
//		Gson gson=new Gson();
//		int userid=Integer.valueOf(map.get("userid"));
//		int pageNumber=Integer.valueOf(map.get("pagenumber"));
//		int pageSize=Integer.valueOf(map.get("pagesize"));
//		Pageable page= new PageRequest(pageNumber, pageSize);
//		String res=gson.toJson(contractDeductRecordService.queryRecord(userid, page));
		ReturnData d=new ReturnData("0000","success","","");
		return d;
	}
	
	/**
	 * map1为订单,map2为发票
	 * 保存订单接口
	 * @param map(对应的订单信息)
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData saveOrder(Map<String,String> ordermap,Map<String,String> ticketmap){
		ReturnData rd=new ReturnData();
		ServiceException retException = null;
		//告警阀值
	   boolean judge = vptService.dealRequest(ordermap, Thread.currentThread() .getStackTrace()[1].getMethodName());
		try{
		if(!judge)
		{
			rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
			rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
			rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
		}else{
			log.info("entry method saveOrder,params:ordermap"+ordermap.toString()+
					"ticketmap:"+ticketmap.toString());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			OrderRecordEntity or=new OrderRecordEntity();
			//处理订单map
			or.setCreateTime(sdf.parse(ordermap.get("createTime")));
			or.setChangeTime(sdf.parse(ordermap.get("createTime")));
			or.setInvoiceStatus(Integer.valueOf(ordermap.get("invoiceStatus")));
			or.setOrderId(ordermap.get("orderId"));
			or.setOrderStatus(Integer.valueOf(ordermap.get("orderStatus")));
			or.setOrderType(ordermap.get("orderType"));
			or.setPayMethod(Integer.valueOf(ordermap.get("payMethod")));
			or.setPayWay(ordermap.get("payWay"));
			or.setPrice(Integer.valueOf(ordermap.get("price")));
			or.setRemark(ordermap.get("remark"));
			or.setTime(Integer.valueOf(ordermap.get("time")));
			or.setCommodity(ordermap.get("commodity"));
			or.setTradeType(Integer.valueOf(ordermap.get("tradeType")));
			or.setUserId(Integer.valueOf(ordermap.get("userId")));
			or.setPackageId(Integer.valueOf(ordermap.get("packageId")));
			//检查订单是否已存在,已存在抛异常
			if(null!=orderRecordService.queryOrderByOrderId(ordermap.get("orderId"))){
				rd=new ReturnData(ConstantUtil.RETURN_ORDER_EXIST[0],ConstantUtil.RETURN_ORDER_EXIST[1],
						ConstantUtil.RETURN_ORDER_EXIST[2],"");
				return rd;
			}
			//保存发票信息
			if (1 !=Integer.valueOf(ordermap.get("invoiceStatus"))){
//				or.setInvoiceId(invoiceId);
				InvoiceInfoEntity ticket=new InvoiceInfoEntity();
				ticket.setType(Integer.valueOf(ticketmap.get("type")));
				ticket.setTitle(ticketmap.get("title"));
				ticket.setName(ticketmap.get("name"));
				ticket.setMobile(ticketmap.get("mobile"));
				ticket.setMailAddress(ticketmap.get("mailAddress"));
				ticket.setCompany(ticketmap.get("company"));
				ticket.setCode(ticketmap.get("code"));
				ticket.setRegisterAddress(ticketmap.get("registerAddress"));
				ticket.setRegisterPhone(ticketmap.get("registerPhone"));
				ticket.setBankName(ticketmap.get("bankName"));
				ticket.setBankAccount(ticketmap.get("bankAccount"));
				ticket.setContent(ticketmap.get("content"));
				ticket.setMailMethod(ticketmap.get("mailMethod"));
//				Persistence.createEntityManagerFactory("uumsJPA").createEntityManager().persist(ticket);
				invoiceInfoService.saveInvoiceInfo(ticket);
				or.setInvoiceId(ticket.getId());
			}
			orderRecordService.saveOrderRecord(or);
			//录入订单信息
			rd= new ReturnData("0000","success","","");
		}}catch(Exception e){
			e.printStackTrace();
			retException=new ServiceException(e);
			rd= new ReturnData(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],
					ConstantUtil.RETURN_SYSTEM_ERROR[2],"");
		}
		finally{
			try{
				logService.log(ordermap,"saveOrder", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		return rd;
	}
	
	
	/**
	 * 11
	 * 查询消费记录
	 * 描述:提供全检索和分页检索两种方式
	 * 
	 * 描述:全检索----全检索支持四种不同的检索条件检索:
	 * 1:查询某个用户下的所有消费记录(userid--用户主键)
	 * 2:查询某个服务的所有消费记录(paycode--服务编码)
	 * 3:查询某个用户消费某种服务的全部记录(userid--用户主键,paycode--服务编码))
	 * 4:查询某个服务的某个流水单号的消费记录(paycode--服务编码,payid--服务流水号(payid也可以不传))
	 * 5:查询所有的记录(只需要有"queryall"key即可)
	 * 
	 * 描述:分页检索方式----分页检索提供三种检索方式:
	 * 1:查询某个用户下的所有消费记录(userid--用户主键,pagesize--每页的记录条数,pagenumber--页码数(第一页记录从0开始))
	 * 2:查询某个服务的所有消费记录(paycode--服务编码,pagesize--每页的记录条数,pagenumber--页码数(第一页记录从0开始))
	 * 3:查询某个用户消费某种服务的全部记录(userid--用户主键,paycode--服务编码,pagesize--每页的记录条数,pagenumber--页码数(第一页记录从0开始)))
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData queryPayRecord(Map<String,String> map){
		log.info("1");
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
		log.info("entry method queryPayRecord,params:"+map.toString());
		Gson gson=new Gson();
		boolean flag=false;
		List<ContractDeductRecordEntity> list=null;
		Map<String,Object> resmap=new HashMap<String,Object>();
		if(null!=map.get("queryall")){
			rd=new ReturnData("0000","success","",gson.toJson(contractDeductRecordService.queryAll()));
			return rd;
		}
		if(null!=map.get("pagesize")&&null!=map.get("pagenumber")){
			if(!CheckUtil.intCheck(map.get("pagesize")).equals("success")){
				rd=new ReturnData("9999",map.get("pagesize")+CheckUtil.intCheck(map.get("pagesize")),"","");
				return rd;
			}
			if(!CheckUtil.intCheck(map.get("pagenumber")).equals("success")){
				rd=new ReturnData("9999",map.get("pagenumber")+CheckUtil.intCheck(map.get("pagenumber")),"","");
				return rd;
			}
			flag=true;
		}
		if(null==map.get("userid")&&null==map.get("paycode")){
			rd=new ReturnData("9999","参数错误userid和paycode为null","","");
			return rd;
		}
		if(null!=map.get("payid")&&null!=map.get("paycode")){
			list=contractDeductRecordService.queryRecord(map.get("paycode"), map.get("payid"));
			Long count=Long.valueOf(list.size());
			resmap.put("list",gson.toJson(list));
			resmap.put("count", CertUtil.pagesize(count, map.get("pagesize")));
			resmap.put("totalcount",String.valueOf(count));
			rd=new ReturnData("0000","success","",gson.toJson(resmap));
			return rd;
		}
		if(null==map.get("userid")&&null!=map.get("paycode")){
			log.info("2");
			if(null==payService.queryByPayCode(map.get("paycode"))){
				rd=new ReturnData("9901",map.get("paycode")+"对应的服务记录为空","","");
				return rd;
			}
			
			if(flag){
				PageRequest page=new PageRequest(Integer.valueOf(map.get("pagenumber")),
						Integer.valueOf(map.get("pagesize")));
				list=contractDeductRecordService.queryRecord(map.get("paycode"), page);
			}else{
				list=contractDeductRecordService.queryRecord(map.get("paycode"));
			}
			Long count=contractDeductRecordService.countRecord(map.get("paycode"));
			resmap.put("list",gson.toJson(list));
			resmap.put("count", CertUtil.pagesize(count, map.get("pagesize")));
			resmap.put("totalcount",String.valueOf(count));
			if(null==list||list.size()==0){
				rd=new ReturnData("0000","success","","");
				return rd;
			}else{
				rd=new ReturnData("0000","success","",gson.toJson(resmap));
				return rd;
			}
		}
		if(null==map.get("paycode")&&null!=map.get("userid")){
			log.info("3");
			if(!CheckUtil.intCheck(map.get("userid")).equals("success")){
				rd=new ReturnData("9999",map.get("userid")+CheckUtil.intCheck(map.get("userid")),"","");
				return rd;
			}
			if(flag){
				PageRequest page=new PageRequest(Integer.valueOf(map.get("pagenumber")),
						Integer.valueOf(map.get("pagesize")));
				list=contractDeductRecordService.queryRecord(map.get("userid"), page);
			}else{
				list=contractDeductRecordService.queryRecord(Integer.parseInt(map.get("userid")));
			}
			Long count=contractDeductRecordService.countRecord(Integer.parseInt(map.get("userid")));
			resmap.put("list",gson.toJson(list));
			resmap.put("count", CertUtil.pagesize(count, map.get("pagesize")));
			resmap.put("totalcount",String.valueOf(count));
			if(null==list||list.size()==0){
				rd=new ReturnData("0000","success","","");
				return rd;
			}else{
				rd=new ReturnData("0000","success","",gson.toJson(resmap));
				return rd;
			}
		}
		else {
			log.info("4");
			//根据userid和paycode查询
			if(null==payService.queryByPayCode(map.get("paycode"))){
				rd=new ReturnData("9901",map.get("paycode")+"对应的服务记录为空","","");
				return rd;
			}
			if(!CheckUtil.intCheck(map.get("userid")).equals("success")){
				rd=new ReturnData("9999",map.get("userid")+CheckUtil.intCheck(map.get("userid")),"","");
				return rd;
			}
			if(flag){
				PageRequest page=new PageRequest(Integer.valueOf(map.get("pagenumber")),
						Integer.valueOf(map.get("pagesize")));
			boolean queryall=false;
			if(null!=map.get("queryall")){
				queryall=true;
			}
//			list=contractDeductRecordService.queryRecord(map.get("paycode"), Integer.valueOf(map.get("userid")), page);
			
			if("tradingshield".equals(map.get("paycode"))){
				log.info("5");
				List<OrderTradingShield> li=orderRecordService.queryOrderTradingShield(map.get("paycode"), Integer.valueOf(map.get("userid")), page);
				Long count=contractDeductRecordService.countRecord(map.get("paycode"),Integer.valueOf(map.get("userid")));
				if(null!=li&&li.size()>0){
					resmap.put("list",gson.toJson(li));
				}else{
					resmap.put("list", "");
				}
				resmap.put("count", CertUtil.pagesize(count, map.get("pagesize")));
				resmap.put("totalcount",String.valueOf(count));
				if(null==li||li.size()==0){
					rd= new ReturnData("0000","success","","");
					return rd;
				}else{
					rd=new ReturnData("0000","success","",gson.toJson(resmap));
					return rd;
				}
			}else{
				log.info("6");
				List<DeductRecord> li=contractDeductRecordService.queryWithContractInfo(map.get("paycode"), Integer.valueOf(map.get("userid")), page,queryall);
				Long count=contractDeductRecordService.countRecord(map.get("paycode"),Integer.valueOf(map.get("userid")));
				resmap.put("list",gson.toJson(li));
				resmap.put("count", CertUtil.pagesize(count,map.get("pagesize")));
				resmap.put("totalcount",String.valueOf(count));
				if(null==li||li.size()==0){
					rd=new ReturnData("0000","success","","");
					return rd;
				}else{
					rd=new ReturnData("0000","success","",gson.toJson(resmap));
					return rd;
				}
			}
			//如果查询的是买卖盾牌的话
			}
			else{
			log.info("7");
			List<ContractDeductRecordEntity> li=contractDeductRecordService.queryRecord(map.get("paycode"), Integer.valueOf(map.get("userid")));
			Long count=contractDeductRecordService.countRecord(map.get("paycode"),Integer.valueOf(map.get("userid")));
			resmap.put("list",gson.toJson(li));
			resmap.put("count", CertUtil.pagesize(count, map.get("pagesize")));
			resmap.put("totalcount",String.valueOf(count));
			if(null==li||li.size()==0){
				rd=new ReturnData("0000","success","","");
				return rd;
			}else{
				rd= new ReturnData("0000","success","",gson.toJson(resmap));
				return rd;
			}
			}
			
		}
		}}catch(Exception e){
			e.printStackTrace();
			retException=new ServiceException(e);
			rd= new ReturnData(ConstantUtil.RETURN_SYSTEM_ERROR[0],
					ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],"");
		}finally{
			try{
				logService.log(map,"queryPayRecord", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		return rd;
	}
	
	/**
	 * 12
	 * 更新订单状态信息
	 * @param map
	 * @return
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData updateOrderStatus(Map<String,String> map){
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
		if(!CheckUtil.checkMap(map, "orderStatus,orderId,changeTime",
				"orderStatus,orderId,changeTime").getRetCode().equals("0000")){
			rd=CheckUtil.checkMap(map, "orderStatus,orderId,changeTime",
					"orderStatus,orderId,changeTime");
			return rd;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		OrderRecordEntity or=orderRecordService.queryOrderByOrderId(map.get("orderId"));
		//订单不存在
		if(null==or){
			rd=new ReturnData(ConstantUtil.RETURN_ORDER_NOT_EXIST[0],ConstantUtil.RETURN_ORDER_EXIST[1],
					ConstantUtil.RETURN_ORDER_EXIST[2],"");
			return rd;
		}
		//状态已为1的不允许修改
		if(or.getOrderStatus()==2){
			rd=new ReturnData(ConstantUtil.CHANGE_ORDERSTATUS_NOTALLOWED[0],ConstantUtil.CHANGE_ORDERSTATUS_NOTALLOWED[1],
					ConstantUtil.CHANGE_ORDERSTATUS_NOTALLOWED[2],"");
		}
		OrderRecordEntity o=new OrderRecordEntity();
		//充钱扣
		o.setOrderStatus(Integer.valueOf(map.get("orderStatus")));
		o.setChangeTime(sdf.parse(map.get("changeTime")));
		o.setOrderId(map.get("orderId"));
		o.setPayplamOrderId(map.get("payplamOrderId"));
		orderRecordService.updateOrderRecord(o);
		String ordertype=or.getOrderType();
		if(Integer.valueOf(map.get("orderStatus"))!=3){
		//合同
		if(ordertype.equals("contract")||ordertype.equals("baoquan")){
			userService.checkUserService(or.getUserId(), ordertype,1);
			userService.addUserServiceTimes(or.getUserId(), ordertype, or.getTime());
			ContractDeductRecordEntity cd=new ContractDeductRecordEntity();
			cd.setBillNum(String.valueOf(System.currentTimeMillis()));
			cd.setUserid(or.getUserId());
			cd.setConsumeType((byte)6);
			cd.setUpdateTime(new Date());
			cd.setDeductSum(new BigDecimal(or.getPrice()));
			cd.setDeductTimes(or.getTime());
			cd.setTypecode(or.getOrderType());
			contractDeductRecordService.saveRecord(cd);
		}
		//卖卖盾
		if(ordertype.equals("tradingshield")){
			ContractDeductRecordEntity cd=new ContractDeductRecordEntity();
			cd.setBillNum(String.valueOf(System.currentTimeMillis()));
			cd.setUserid(or.getUserId());
			cd.setConsumeType((byte)7);
			cd.setUpdateTime(new Date());
			cd.setDeductSum(new BigDecimal(or.getPrice()));
			cd.setDeductTimes(or.getTime());
			cd.setTypecode(or.getOrderType());
			contractDeductRecordService.saveRecord(cd);
		}
		}
		rd=new ReturnData("0000","success","","");
		}}catch(Exception e){
			e.printStackTrace();
			rd=new ReturnData(ConstantUtil.RETURN_SYSTEM_ERROR[0],
					ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],"");
		}finally{
			try{
				logService.log(map,"updateOrderStatus", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		return rd;
	}
	
	
	/**
	 * 13
	 * 分页查询订单信息
	 * @param map
	 * @return
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData queryOrder(Map<String,String> map){
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
		Gson gson=new Gson();
		String useridStr=map.get("userid");
		if(!CheckUtil.intCheck(useridStr).equals("success")){
			rd=new ReturnData("9999",CheckUtil.intCheck(useridStr),"","");
			return rd;
		}
		if(null!=map.get("pagesize")&&null!=map.get("pagenumber")){
			if(!CheckUtil.intCheck(map.get("pagesize")).equals("success")){
				rd=new ReturnData("9999",map.get("pagesize")+CheckUtil.intCheck(map.get("pagesize")),"","");
				return rd;
			}
			if(!CheckUtil.intCheck(map.get("pagenumber")).equals("success")){
				rd=new ReturnData("9999",map.get("pagenumber")+CheckUtil.intCheck(map.get("pagenumber")),"","");
				return rd;
			}
		}
		PageRequest page=new PageRequest(Integer.valueOf(map.get("pagenumber")),
				Integer.valueOf(map.get("pagesize")));
		//
		
		if(null!=map.get("paycode")){
			if("tradingshield".equals(map.get("paycode"))){
				Map<String,Object> resmap =new HashMap<String,Object>();
				List<OrderTradingShield> li=orderRecordService.queryOrderTradingShield(map.get("paycode"), Integer.valueOf(map.get("userid")), page);
				Long count=orderRecordService.queryOrderNumByPayCode(Integer.valueOf(map.get("userid")),map.get("paycode"));
				if(null!=li&&li.size()>0){
					resmap.put("list",gson.toJson(li));
				}else{
					resmap.put("list", "");
				}
				resmap.put("count", CertUtil.pagesize(count, map.get("pagesize")));
				resmap.put("totalcount", String.valueOf(count));
				if(null==li||li.size()==0){
					rd=new ReturnData("0000","success","","");
					return rd;
				}else{
					rd= new ReturnData("0000","success","",gson.toJson(resmap));
					return rd;
				}
			}else{
			List<OrderTradingShield> list=orderRecordService.queryOrderTradingShield(map.get("paycode"),Integer.valueOf(useridStr), page);
			Long count=orderRecordService.queryOrderNumByPayCode(Integer.valueOf(useridStr),map.get("paycode"));
			Map<String,Object> resmap=new HashMap<String,Object>();
			resmap.put("list", JSONUtil.Obj2String(list));
			resmap.put("count", CertUtil.pagesize(count, map.get("pagesize")));
			resmap.put("totalcount",String.valueOf(count));
			if(null==list||list.size()==0){
				rd= new ReturnData("0000","success","","");
				return rd;
			}else{
				
				rd=new ReturnData("0000","success","",gson.toJson(resmap));
				return rd;
			}
			}
		}else{
			List<OrderRecordEntity> list=new ArrayList<OrderRecordEntity>();
			list=orderRecordService.queryOrderPageByUserId(Integer.valueOf(useridStr), page);
			Long count=orderRecordService.countOrderByUserId(Integer.valueOf(useridStr));
			Map<String,Object> resmap=new HashMap<String,Object>();
			resmap.put("list", JSONUtil.Obj2String(list));
			resmap.put("count", CertUtil.pagesize(count, map.get("pagesize")));
			resmap.put("totalcount",String.valueOf(count));
			if(null==list||list.size()==0){
				rd=new ReturnData("0000","success","","");
				return rd;
			}else{
				
				rd=new ReturnData("0000","success","",gson.toJson(resmap));
				return rd;
			}
		}
		}}catch(Exception e){
			e.printStackTrace();
			retException=new ServiceException(e);
			rd=new ReturnData(ConstantUtil.RETURN_SYSTEM_ERROR[0],
					ConstantUtil.RETURN_SYSTEM_ERROR[1],ConstantUtil.RETURN_SYSTEM_ERROR[2],"");
		}finally{
			try{
				logService.log(map,"queryOrder", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		return rd;
	} 
	
	/**
	 * 查询买卖盾的信息
	 * @param map
	 * @return
	 */
	@Transactional(rollbackFor = ServiceException.class, propagation = Propagation.REQUIRED)
	public ReturnData queryTradingShield(Map<String,String> map){
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
			log.info("queryTradingShield:"+map.toString());
			Gson gson=new Gson();
			String orderId=map.get("orderId");
			OrderTradingShield o=orderRecordService.queryTradingShieldOrder(orderId);
			if(null!=o){
				rd= new ReturnData("0000","success","",gson.toJson(o));
				return rd;
			}else{
			rd=new ReturnData(ConstantUtil.ORDERID_IS_NULL[0],ConstantUtil.ORDERID_IS_NULL[1],
					ConstantUtil.ORDERID_IS_NULL[2],"");
			return rd;
			}
		}}catch(Exception e){
			e.printStackTrace();
			retException=new ServiceException(e);
			rd=new ReturnData(ConstantUtil.RETURN_SYSTEM_ERROR[0],ConstantUtil.RETURN_SYSTEM_ERROR[1],
					ConstantUtil.RETURN_SYSTEM_ERROR[2],"");
		}finally{
			try{
				logService.log(map,"queryTradingShield", retException, rd);
			}catch(ServiceException e){
				e.printStackTrace();
			}
		}
		return rd;
	}
}
