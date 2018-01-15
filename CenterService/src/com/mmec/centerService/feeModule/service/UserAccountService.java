package com.mmec.centerService.feeModule.service;

import java.math.BigDecimal;
import java.util.List;

import com.mmec.centerService.feeModule.entity.UserAccountEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;

public interface UserAccountService{
	/**
	 * 增加用户金额
	 */
	public String addMoney(int userid,BigDecimal money,String paycode)throws ServiceException;
	
	/**
	 * 减少用户金额
	 */
	public String reduceMoney(int userid,BigDecimal money,String paycode)throws ServiceException;
	
	/**
	 * 检查用户记录
	 */
	public String checkUserAccount(int userid,BigDecimal money,String paycode)throws ServiceException;
	
	/**
	 * 查询用户账户
	 */
	public UserAccountEntity queryMoney(int userid,String paycode);
	
	/**
	 * 查询用户账户集合
	 */
	public List<UserAccountEntity> queryUserAccountList(int userid);
	
	/**
	 * 减少次数
	 */
	public void  reduce_times(int userid,int times,String paycode,String payid) throws ServiceException;
	
}
