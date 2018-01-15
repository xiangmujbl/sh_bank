package com.mmec.centerService.feeModule.service;

import java.util.List;

import com.mmec.centerService.feeModule.entity.UserServiceEntity;
import com.mmec.exception.ServiceException;

public interface UserService{
	
	/**
	 * 保存一条用户-服务记录code码
	 */
	public String checkUserService (int userid,String paycode,int paytype)throws ServiceException;
	
	/**
	 * 给某个用户-服务增加次数
	 */
	public String addUserServiceTimes(int userid,String paycode,int times);
	
	/**
	 * 给某个用户-服务减少次数
	 */
	public String reduceUserServiceTimes(int userid,String paycode,int times) throws ServiceException;
	
	/**
	 * 查询某个用户的服务
	 */
	public List<UserServiceEntity> queryByUserId(int userid);
	
	/**
	 * 查询某个用户的服务-服务次数
	 */
	public UserServiceEntity queryByUserIdAndPayCode(int userid,String paycode);
	
	/**
	 * 查询某个服务的用户-用户服务次数
	 */
	public List<UserServiceEntity> queryByPayCode(String paycode);
}