package com.mmec.centerService.feeModule.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.feeModule.dao.PayServiceDao;
import com.mmec.centerService.feeModule.dao.UserAccountDao;
import com.mmec.centerService.feeModule.entity.ContractDeductRecordEntity;
import com.mmec.centerService.feeModule.entity.PayServiceEntity;
import com.mmec.centerService.feeModule.entity.UserAccountEntity;
import com.mmec.centerService.feeModule.service.ContractDeductRecordService;
import com.mmec.centerService.feeModule.service.PayService;
import com.mmec.centerService.feeModule.service.UserAccountService;
import com.mmec.centerService.feeModule.service.UserService;
import com.mmec.centerService.userModule.dao.IdentityDao;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.CheckUtil;
import com.mmec.util.ConstantUtil;
import com.mmec.util.JSONUtil;

@Service("userAccountService")
public class UserAccountServiceImpl implements UserAccountService{
	
	private static Logger log=Logger.getLogger(UserAccountServiceImpl.class);
	
	@Autowired
	private UserAccountDao userAccountDao;
	
	@Autowired
	private IdentityDao identityDao;
	
	@Autowired
	private PayServiceDao payServiceDao;
	
	@Autowired
	private PayService payService;
	
	@Autowired
	private ContractDeductRecordService contractDeductRecordService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 合同扣次
	 */
	public void  reduce_times(int userid,int times,String paycode,String payid) throws ServiceException{
		if(null==payService.queryByPayCode(paycode)){
			throw new ServiceException(ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[0], ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[0]);
		}
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
		cd.setPayId(payid);
		contractDeductRecordService.saveRecord(cd);
	}
	
	/**
	 * 用户账户查询/插入
	 */
	public String checkUserAccount(int userid,BigDecimal money,String paycode)throws ServiceException{
		//userid对应的用户记录不存在---抛出异常
		IdentityEntity i=identityDao.findById(userid);
		if(null==i){
			log.info("充值:"+String.valueOf(userid)+"用户不存在");
			throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],
					ConstantUtil.RETURN_USER_NOTEXIST[1],ConstantUtil.RETURN_USER_NOTEXIST[2]);
		}
		PayServiceEntity ps=payServiceDao.findByTypeCode(paycode);
		if(null==ps){
			log.info("充值:"+paycode+"服务码不存在");
			throw new ServiceException(ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[0],
					ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[1],ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[2]);
		}
		//userid对应的账户记录不存在---追加该记录
		if(null==userAccountDao.findByUseridAndPaycode(userid,paycode)){
		UserAccountEntity ua=new UserAccountEntity();
		ua.setBanlance(money);
		ua.setUserid(userid);
		ua.setPaycode(paycode);
		userAccountDao.save(ua);
		}
		return null;
	}
	
	/**
	 * 增加用户金额
	 */
	public String addMoney(int userid,BigDecimal money,String paycode) throws ServiceException{
		BigDecimal m=money.setScale(2, BigDecimal.ROUND_HALF_UP);
		UserAccountEntity ua=userAccountDao.findByUseridAndPaycode(userid,paycode);
		BigDecimal moneyInAccount=ua.getBanlance();
		//如果添加金额和账户金额的添加值过大---抛出异常
		if(!CheckUtil.moneyaddCheck(moneyInAccount, m).equals("success")){
			throw new ServiceException(ConstantUtil.RETURN_MONEY_NOT_LEGAL[0],
					ConstantUtil.RETURN_MONEY_NOT_LEGAL[1],ConstantUtil.RETURN_MONEY_NOT_LEGAL[2]);
		}
		userAccountDao.UpdateUserAccount(moneyInAccount.add(m), userid,paycode);
		log.info("充值:"+String.valueOf(userid)+"金额:"+m.toString()+"服务:"+paycode+"成功");
		return null;
	}
	
	/**
	 * 减少用户金额
	 */
	public String reduceMoney(int userid,BigDecimal money,String paycode) throws ServiceException{
		BigDecimal moneyToReduce=money.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal moneyInAccount=userAccountDao.findByUseridAndPaycode(userid,paycode).getBanlance();
		//如果余额不足---抛出异常
		if(moneyInAccount.subtract(moneyToReduce).compareTo(ConstantUtil.ZERO_MONEY)<0){
			throw new ServiceException(ConstantUtil.RETURN_AMNT_NOT_ENOUGH[0],
					"目前剩余"+moneyInAccount.toString()+"元,不足以支付"+moneyToReduce+"元",ConstantUtil.RETURN_AMNT_NOT_ENOUGH[2]);
		}
		userAccountDao.UpdateUserAccount(moneyInAccount.subtract(moneyToReduce), userid,paycode);
		return null;
	}
	
	/**
	 * 查询用户金额
	 */
	public UserAccountEntity queryMoney(int userid,String paycode){
		log.info("entry method queryMoney,params:userid"+String.valueOf(userid));
		UserAccountEntity uabean=userAccountDao.findByUseridAndPaycode(userid,paycode);
		return uabean;
	}
	
	
	/**
	 * 查询用户列表集合
	 */
	public List<UserAccountEntity> queryUserAccountList(int userid){
		return userAccountDao.findByUserid(userid);
	}
}