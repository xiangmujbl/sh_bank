package com.mmec.centerService.userModule.service;

import java.util.Map;

import com.mmec.exception.ServiceException;

public interface UserService {
	//云签用户登录
	public String yunsignUserLogin(Map<String,String> datamap)throws ServiceException;
	//证书登录
	public String certUserLogin(Map<String,String> datamap)throws ServiceException;
	//校验证书
	public String checkCert(Map<String,String> datamap)throws ServiceException;
	//平台用户登录
	public String platformUserLogin(Map<String,String> datamap)throws ServiceException;
	//新增 云签 用户
	public String saveYunsignIdentity(Map<String,String> datamap)throws ServiceException;
	//新增 固话 用户
	public String saveTelIdentity(Map<String,String> datamap)throws ServiceException;
	//新增 对接 用户
	public String saveIdentity(Map<String,String> datamap)throws ServiceException;
	//新增 对接 资料不全用户
	public String saveOtherIdentity(Map<String,String> datamap)throws ServiceException;
	//密码修改
	public String updateUserPassword(Map<String,String> datamap)throws ServiceException;
	//手机号码修改
	public String updateUserMobile(Map<String,String> datamap)throws ServiceException;
	//邮箱修改
	public String updateUserEmail(Map<String,String> datamap)throws ServiceException;
	//对接企业用户资料变更
	public String updateCompanyInfo(Map<String,String> datamap)throws ServiceException;
	//云签企业用户资料变更
	public String updateYunsignCompanyInfo(Map<String,String> datamap)throws ServiceException;
	//对接个人用户资料变更
	public String updateCustomInfo(Map<String,String> datamap)throws ServiceException;
	//云签企业用户资料变更
	public String updateYunsignCustomInfo(Map<String,String> datamap)throws ServiceException;
	//企业管理员变更
	public String updateUserAdmin(Map<String,String> datamap)throws ServiceException;
	//子账号绑定
	public String bandingMajorAccount(Map<String,String> datamap)throws ServiceException;
	//用户查询
	public String userQuery(Map<String,String> datamap)throws ServiceException;
	//根据手机号查询用户
	public String userQueryByMobile(Map<String,String> datamap)throws ServiceException;
	//用户激活
	public String userActivat(Map<String,String> datamap)throws ServiceException;
	//用户注销
	public String userLogOut(Map<String,String> datamap)throws ServiceException;	
	//根据手机号码查询个人用户是否存在
	public String getCustomByMobile(Map<String,String> datamap)throws ServiceException;	
	//根据邮箱查询企业用户是否存在
	public String getCompanyByEmail(Map<String,String> datamap)throws ServiceException;	
	//查看企业管理员列表  ---  测试分页
	public String queryAllUser(Map<String,String> datamap)throws ServiceException;	
	//开通签约室
	public String openSigningRoom(Map<String,String> datamap)throws ServiceException;	
	//关闭签约室
	public String closeSigningRoom(Map<String,String> datamap)throws ServiceException;	
	//查询签约室信息
	public String querySigningRoom(Map<String,String> datamap)throws ServiceException;	
	//添加子账号
	public String addChildAccount(Map<String,String> datamap)throws ServiceException;	
	//修改子账号
	public String updateChildAccount(Map<String,String> datamap)throws ServiceException;	
	//停用子账号
	public String stopChildAccount(Map<String,String> datamap)throws ServiceException;
	//查询子账号列表
	public String queryChildAccount(Map<String,String> datamap)throws ServiceException;	
	//查询带绑定用户列表
	public String queryBangAccountList(Map<String,String> datamap)throws ServiceException;	

	//根据手机号码查询云签 企业用户
	public String getCompanyAccountByMobile(Map<String,String> datamap)throws ServiceException;
	//根据邮箱查询云签 个人用户
	public String getCustomAccountByEmail(Map<String,String> datamap)throws ServiceException;
	//根据公司名称精确匹配公司员工
	public String getCustomMembersByCompanyName(Map<String,String> datamap)throws ServiceException;
	//变更企业管理员账号
	public String changeAppAdmin(Map<String,String> datamap)throws ServiceException;
	//补全信息
	public String completeInfo(Map<String,String> datamap)throws ServiceException;
	
	//查询用户审批状态
	public String queryUserExamineStatus(Map<String,String> datamap)throws ServiceException;
	
	public String synchronizationUserInfo(Map<String,String> datamap)throws ServiceException;
	
	//新增途牛保存接口
	public String saveIdentityTUNIU(Map<String,String> datamap) throws ServiceException;
	
}
