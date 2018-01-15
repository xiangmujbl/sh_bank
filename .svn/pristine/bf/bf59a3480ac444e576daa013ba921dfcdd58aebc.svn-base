/*
 * 用户类远程接口实现类
 * 
 */
package com.mmec.centerService.userModule.service;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mmec.centerService.vpt.service.VptService;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;
import com.mmec.thrift.service.UserRMIServices.Iface;
import com.mmec.util.CacheUtil;
import com.mmec.util.ConstantUtil;

import net.sf.ehcache.Cache;

@Component("userIface")
public class UserRMIServiceImpl   implements Iface
{
	private static Logger  log = Logger.getLogger(UserRMIServiceImpl.class);
	@Autowired
	private PlatformService platformService;	
	@Autowired
	private UserService userService;	
	@Autowired
	private UkeyService ukeyService;	
	@Autowired
	private SealService sealService;	
	@Autowired
	private LogService logService;	
	@Autowired
	private WXService wXService;	
	@Autowired
	private MyAttnService myAttnService;	
	@Autowired
	private VptService vptService;	
	
	@Override
	@Transactional
	//用户登录  根据来源不同  分别匹配不同的登录方式
	public ReturnData userLogin(Map<String,String> datamap)
			throws TException {
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="userLogin";
		try {
			//证书登录
			if(ConstantUtil.FROM_CERT.equals(datamap.get("optFrom")))
			{
				optType = "certUserLogin";
				String  retString = userService.certUserLogin(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
			//MMEC登录
			else if(ConstantUtil.FROM_MMEC.equals(datamap.get("optFrom")))
			{
				optType = "platformUserLogin";
				String  retString = userService.platformUserLogin(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
			//个人/企业用户登录
			else if(ConstantUtil.FROM_YUNSIGN.equals(datamap.get("optFrom")))
			{
				optType = "yunsignUserLogin";
				String  retString = userService.yunsignUserLogin(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
			else
			{
				rd.setRetCode(ConstantUtil.RETURN_FAIL_PARAMERROR[0]);
				rd.setDesc(ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"操作来源不匹配");
				rd.setDescEn(ConstantUtil.RETURN_FAIL_PARAMERROR[2]+"optFrom  not  match.");
			}			
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("userLogin," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	//用户注册
	@Override
	public ReturnData userRegister(Map<String,String> datamap)
			throws TException {
		//权限控制
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="userRegister";
		String  retString = "";
		
		if(userRepeatSubmit(datamap,1))
		{
			rd.setRetCode(ConstantUtil.RETURN_REPEAT_REGISTER[0]);
			rd.setDesc(ConstantUtil.RETURN_REPEAT_REGISTER[1]);
			rd.setDescEn(ConstantUtil.RETURN_REPEAT_REGISTER[2]);
		}
		else
		{
			//验证请求是否达到阈值
			boolean judge = vptService.dealRequest(datamap, optType);
			try {
				if(!judge)
				{
					rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
					rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
					rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
				}
				else
				{
					//云签注册
					if(ConstantUtil.FROM_YUNSIGN.equals(datamap.get("optFrom")))
					{
						 optType = "saveYunsignIdentity";
						 userService.saveYunsignIdentity(datamap);
						 rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
						rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
						rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
						rd.setPojo(retString);
					}
					// 对接用户注册
					else
					{
						optType = "saveIdentity";
						//9.5  最新要求  不管猎聘网的需求了  注册还是按照之前的  进行校验
//						
//						//手机号码为空 则是固话注册
//						if(null == datamap.get("mobile") || "".equals(datamap.get("mobile")))
//						{
//							userService.saveTelIdentity(datamap);
//						}
////						//姓名、身份证号为空 也开一个单独注册
////						else if(null == datamap.get("identityCard") || "".equals(datamap.get("identityCard")))
////						{
////							userService.saveOtherIdentity(datamap);
////						}
//						else
//						{
							String retStr = userService.saveIdentity(datamap);
							if("duplicateRegister".equals(retStr))
							{
								rd.setRetCode(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_EXIST[0]);
								rd.setDesc(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_EXIST[1]);
								rd.setDescEn(ConstantUtil.RETURN_USER_ACCOUNT_PLATUSERID_EXIST[2]);
							}
							else
							{
								rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
								rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
								rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
								rd.setPojo(retString);
							}
//						}
					}
				}
			} catch (ServiceException e) {
//				retException = new ServiceException(e);
				rd.setRetCode(e.getErrorCode());
				rd.setDesc(e.getErrorDesc());
				rd.setDescEn(e.getErrorDescEn());
			}
			finally
			{
				try {
					logService.log(datamap,optType, retException, rd);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
				Cache cache = CacheUtil.mmecCache;
				String checkStr = datamap.get("appId")+"@"+datamap.get("platformUserName");				
				cache.remove(checkStr);
			}
			
		}
		//日志记录 
		log.info("userRegister," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	//更新用户信息
	@Override
	public ReturnData userUpdate(Map<String,String> datamap) throws TException {
		// TODO Auto-generated method stub
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="userUpdate";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				if(ConstantUtil.FROM_MMEC.equals(datamap.get("optFrom")))
				{
					//信息补全
					if("completeInfo".equals(datamap.get("optType")))
					{
						optType = "updateUserPassword";
						String  retString = userService.completeInfo(datamap);
						rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
						rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
						rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
						rd.setPojo(retString);
					}
					else
					{
						optType = "updateCustomInfo";
						String retString = datamap.get("optType");
						rd.setRetCode(ConstantUtil.RETURN_NORIGHT_ERROR[0]);
						rd.setDesc(ConstantUtil.RETURN_NORIGHT_ERROR[1]);
						rd.setDescEn(ConstantUtil.RETURN_NORIGHT_ERROR[2]);
						rd.setPojo(retString);
					}
				}
				else
				{
					//密码修改
					if("changePassword".equals(datamap.get("optType")))
					{
						optType = "updateUserPassword";
						String  retString = userService.updateUserPassword(datamap);
						rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
						rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
						rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
						rd.setPojo(retString);
					}
					//信息补全
					else if("completeInfo".equals(datamap.get("optType")))
					{
						optType = "updateUserPassword";
						String  retString = userService.completeInfo(datamap);
						rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
						rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
						rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
						rd.setPojo(retString);
					}
					//手机号码修改
					else if("changeMobile".equals(datamap.get("optType")))
					{
						optType = "updateUserMobile";
						String  retString = userService.updateUserMobile(datamap);
						rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
						rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
						rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
						rd.setPojo(retString);
					}
					//邮箱修改
					else if("changeEmail".equals(datamap.get("optType")))
					{
						optType = "updateUserEmail";
						String  retString = userService.updateUserEmail(datamap);
						rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
						rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
						rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
						rd.setPojo(retString);
					}
					//企业用户资料变更
					else if("changeCompany".equals(datamap.get("optType")))
					{
						String  retString = "";
						if(ConstantUtil.FROM_MMEC.equals(datamap.get("optFrom")))
						{
							optType = "updateCompanyInfo";
							retString = userService.updateCompanyInfo(datamap);
						}
						else if(ConstantUtil.FROM_YUNSIGN.equals(datamap.get("optFrom")))
						{
							optType = "updateYunsignCompanyInfo";
							retString = userService.updateYunsignCompanyInfo(datamap);
						}
						rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
						rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
						rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
						rd.setPojo(retString);
					}
					//个人用户资料变更
					else if("changeCustom".equals(datamap.get("optType")))
					{
						String  retString = "";
						if(ConstantUtil.FROM_MMEC.equals(datamap.get("optFrom")))
						{
							optType = "updateCustomInfo";
							retString = userService.updateCustomInfo(datamap);
							rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
							rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
							rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
							rd.setPojo(retString);
						}
						else if(ConstantUtil.FROM_YUNSIGN.equals(datamap.get("optFrom")))
						{
							optType = "updateYunsignCustomInfo";
							retString = userService.updateYunsignCustomInfo(datamap);
							rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
							rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
							rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
							rd.setPojo(retString);
						}
						else
						{
							rd.setRetCode(ConstantUtil.RETURN_FAIL_PARAMERROR[0]);
							rd.setDesc(ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"操作来源不匹配");
							rd.setDescEn(ConstantUtil.RETURN_FAIL_PARAMERROR[2]+"optFrom  not  match.");
						}
					}
					//企业管理员变更
					else if("changeAdmin".equals(datamap.get("optType")))
					{
						optType = "updateUserAdmin";
						String  retString = userService.updateUserAdmin(datamap);
						rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
						rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
						rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
						rd.setPojo(retString);
					}
					//子账号绑定
					else if("bandMajorAccount".equals(datamap.get("optType")))
					{
						optType = "bandingMajorAccount";
						String  retString = userService.bandingMajorAccount(datamap);
						rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
						rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
						rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
						rd.setPojo(retString);
					}
					else
					{
						rd.setRetCode(ConstantUtil.RETURN_FAIL_PARAMERROR[0]);
						rd.setDesc(ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"操作类型不匹配");
						rd.setDescEn(ConstantUtil.RETURN_FAIL_PARAMERROR[2]+"optType  not  match.");
					}
				}
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info(optType+":"+"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	//用户激活
	@Override
	public ReturnData userActivat(Map<String,String> datamap)
			throws TException {
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="userActivat";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = userService.userActivat(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("userActivat," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	//用户注销
	@Override
	public ReturnData userLogOut(Map<String,String> datamap)
			throws TException {
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="userLogOut";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = userService.userLogOut(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("userLogOut," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	//用户资料查询
	@Override
	@Transactional
	public ReturnData userQuery(Map<String,String> datamap)
			throws TException {
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="userQuery";
		try {
			String  retString = userService.userQuery(datamap);
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("userQuery," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}
	
	@Override
	public ReturnData userQueryByMobile(Map<String,String> datamap)
			throws TException {
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="userQueryByMobile";
		try {
			String  retString = userService.userQueryByMobile(datamap);
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("userQueryByMobile," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}
	//证书注册
	@Override
	public ReturnData certRegister(Map<String,String> datamap)
			throws TException {
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="certRegister";
		
		if(userRepeatSubmit(datamap,2))
		{
			rd.setRetCode(ConstantUtil.RETURN_REPEAT_REGISTER[0]);
			rd.setDesc(ConstantUtil.RETURN_REPEAT_REGISTER[1]);
			rd.setDescEn(ConstantUtil.RETURN_REPEAT_REGISTER[2]);
		}
		else
		{
			//验证请求是否达到阈值
			boolean judge = vptService.dealRequest(datamap, optType);
			try {
				if(!judge)
				{
					rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
					rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
					rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
				}
				else
				{
					optType = "saveUkey";
					String  retString = ukeyService.saveUkey(datamap);
					rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
					rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
					rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
					rd.setPojo(retString);
				}
			} catch (ServiceException e) {
				retException = new ServiceException(e);
				rd.setRetCode(e.getErrorCode());
				rd.setDesc(e.getErrorDesc());
				rd.setDescEn(e.getErrorDescEn());
			}
			finally
			{
				try {
					logService.log(datamap,optType, retException, rd);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		}
		//日志记录 
		log.info("certRegister," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	//证书更新
	@Override
	public ReturnData certActive(Map<String,String> datamap)
			throws TException {
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="certActive";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				optType = "updateUkey";
				String  retString = ukeyService.updateUkey(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("certUpdate," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	//证书查询
	@Override
	@Transactional
	public ReturnData certQuery(Map<String,String> datamap)
			throws TException {
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType = "queryUkey";
		try {
			String  retString = ukeyService.queryUkey(datamap);
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("certQuery," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	//证书解绑
	@Override
	public ReturnData certUnbund(Map<String,String> datamap)
			throws TException {
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="certUnbund";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = ukeyService.unbundUkey(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("certUnbund," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	//第三方平台注册
	@Override
	public ReturnData platformRegister(Map<String,String> datamap) throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="platformRegister";
		//1 用户注册  2 证书注册 3 平台注册
		if(userRepeatSubmit(datamap,3))
		{
			rd.setRetCode(ConstantUtil.RETURN_REPEAT_REGISTER[0]);
			rd.setDesc(ConstantUtil.RETURN_REPEAT_REGISTER[1]);
			rd.setDescEn(ConstantUtil.RETURN_REPEAT_REGISTER[2]);
		}
		else
		{
			//验证请求是否达到阈值
			boolean judge = vptService.dealRequest(datamap, optType);
			try {
				if(!judge)
				{
					rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
					rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
					rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
				}
				else
				{
					String  retString = platformService.platformRegister(datamap);
					rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
					rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
					rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
					rd.setPojo(retString);
				}
			} catch (ServiceException e) {
				retException = new ServiceException(e);
				rd.setRetCode(e.getErrorCode());
				rd.setDesc(e.getErrorDesc());
				rd.setDescEn(e.getErrorDescEn());
			}
			finally
			{
				try {
					logService.log(datamap,optType, retException, rd);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		}
		//日志记录 
		log.info("platformRegister," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	//第三方平台校验
	@Override
	public ReturnData platformVerify(Map<String,String> datamap)
			throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="platformCheck";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = platformService.platformCheck(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("platformVerify," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}
    
	//第三方平台查询
	@Override
	@Transactional
	public ReturnData platformQuery(Map<String,String> datamap)
			throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="platformQuery";
		try {
			String  retString = platformService.platformQuery(datamap);
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("platformQuery," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}
	    
   //第三方平台校验
	@Override
	@Transactional
	public ReturnData getAllUser(Map<String,String> datamap)
			throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="queryAllUser";
		try {
			String  retString = userService.queryAllUser(datamap);
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("getAllUser," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	@Transactional
	public ReturnData getCustomByMobile(Map<String, String> datamap)
			throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="getCustomByMobile";
		try {
			String  retString = userService.getCustomByMobile(datamap);
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}

		}
		Date end2 = new Date();

		//日志记录 
		log.info("getCustomByMobile," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	@Transactional
	public ReturnData getCompanyByEmail(Map<String, String> datamap)
			throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="getCompanyByEmail";
		try {
			String  retString = userService.getCompanyByEmail(datamap);
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("getCompanyByEmail," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	public ReturnData openSigningRoom(Map<String, String> datamap)
			throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="openSigningRoom";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = userService.openSigningRoom(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("openSigningRoom," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	@Transactional
	public ReturnData querySigningRoom(Map<String, String> datamap)
			throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="querySigningRoom";
		try {
			String  retString = userService.querySigningRoom(datamap);
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("querySigningRoom," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	public ReturnData closeSigningRoom(Map<String, String> datamap)
			throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="closeSigningRoom";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = userService.closeSigningRoom(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("closeSigningRoom," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	public ReturnData addChildAccount(Map<String, String> datamap)
			throws TException {
		// TODO Auto-generated method stub
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="addChildAccount";

		//1 用户注册  2 证书注册 3 平台注册  子账号
		if(userRepeatSubmit(datamap,4))
		{
			rd.setRetCode(ConstantUtil.RETURN_REPEAT_REGISTER[0]);
			rd.setDesc(ConstantUtil.RETURN_REPEAT_REGISTER[1]);
			rd.setDescEn(ConstantUtil.RETURN_REPEAT_REGISTER[2]);
		}
		else
		{
			//验证请求是否达到阈值
			boolean judge = vptService.dealRequest(datamap, optType);
			try {
				if(!judge)
				{
					rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
					rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
					rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
				}
				else
				{
					String  retString = userService.addChildAccount(datamap);
					rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
					rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
					rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
					rd.setPojo(retString);
				}
			} catch (ServiceException e) {
				retException = new ServiceException(e);
				rd.setRetCode(e.getErrorCode());
				rd.setDesc(e.getErrorDesc());
				rd.setDescEn(e.getErrorDescEn());
			}
			finally
			{
				try {
					logService.log(datamap,optType, retException, rd);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		}
		//日志记录 
		log.info("closeSigningRoom," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	public ReturnData updateChildAccount(Map<String, String> datamap)
			throws TException {
		// TODO Auto-generated method stub
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="updateChildAccount";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = userService.updateChildAccount(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("updateChildAccount," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	public ReturnData stopChildAccount(Map<String, String> datamap)
			throws TException {
		// TODO Auto-generated method stub
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="stopChildAccount";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = userService.stopChildAccount(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("stopChildAccount," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	@Transactional
	public ReturnData queryChildAccount(Map<String, String> datamap)
			throws TException {
		// TODO Auto-generated method stub
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="queryChildAccount";
		try {
			String  retString = userService.queryChildAccount(datamap);
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("queryChildAccount," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	@Transactional
	public ReturnData certLogin(Map<String, String> datamap) throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="certLogin";
		try {
			String  retString = userService.certUserLogin(datamap);
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("certLogin," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}
	//TODO
	/**
	 * 校验证书
	 */
	@Override
	@Transactional
	public ReturnData checkCert(Map<String, String> datamap) throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="checkCert";
		try {
			datamap.put("shbank","true");
			String  retString = userService.checkCert(datamap);
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("certLogin," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}
	
	@Override
	public ReturnData addSeal(Map<String, String> datamap) throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="addSeal";
		//1 用户注册  2 证书注册 3 平台注册  4子账号  5 图章
		if(userRepeatSubmit(datamap,5))
		{
			rd.setRetCode(ConstantUtil.RETURN_REPEAT_REGISTER[0]);
			rd.setDesc(ConstantUtil.RETURN_REPEAT_REGISTER[1]);
			rd.setDescEn(ConstantUtil.RETURN_REPEAT_REGISTER[2]);
		}
		else
		{
			//验证请求是否达到阈值
			boolean judge = vptService.dealRequest(datamap, optType);
			try {
				if(!judge)
				{
					rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
					rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
					rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
				}
				else
				{
					String  retString = sealService.addSeal(datamap);
					rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
					rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
					rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
					rd.setPojo(retString);
				}
			} catch (ServiceException e) {
				retException = new ServiceException(e);
				rd.setRetCode(e.getErrorCode());
				rd.setDesc(e.getErrorDesc());
				rd.setDescEn(e.getErrorDescEn());
			}
			finally
			{
				try {
					logService.log(datamap,optType, retException, rd);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		}
		//日志记录 
		log.info("addSeal," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	@Transactional
	public ReturnData querySeal(Map<String, String> datamap) throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="querySeal";
		try {
			String  retString = sealService.querySeal(datamap);
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("querySeal," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	public ReturnData delSeal(Map<String, String> datamap) throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="delSeal";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = sealService.delSeal(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("delSeal," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	public ReturnData platformApply(Map<String, String> datamap)
			throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="platformApply";
		//1 用户注册  2 证书注册 3 平台注册  4子账号  5 图章 6 平台申请
		if(userRepeatSubmit(datamap,6))
		{
			rd.setRetCode(ConstantUtil.RETURN_REPEAT_REGISTER[0]);
			rd.setDesc(ConstantUtil.RETURN_REPEAT_REGISTER[1]);
			rd.setDescEn(ConstantUtil.RETURN_REPEAT_REGISTER[2]);
		}
		else
		{
			//验证请求是否达到阈值
			boolean judge = vptService.dealRequest(datamap, optType);
			try {
				if(!judge)
				{
					rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
					rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
					rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
				}
				else
				{
					String  retString = platformService.platformApply(datamap);
					rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
					rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
					rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
					rd.setPojo(retString);
				}
			} catch (ServiceException e) {
				retException = new ServiceException(e);
				rd.setRetCode(e.getErrorCode());
				rd.setDesc(e.getErrorDesc());
				rd.setDescEn(e.getErrorDescEn());
			}
			finally
			{
				try {
					logService.log(datamap,optType, retException, rd);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		}
		//日志记录 
		log.info("platformApply," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	public ReturnData platformApplyCheck(Map<String, String> datamap)
			throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="platformApplyCheck";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = platformService.platformApplyCheck(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("platformApplyCheck," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	@Transactional
	public ReturnData queryNonYunSignPlatform(Map<String, String> datamap)
			throws TException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	@Transactional
	public ReturnData platformApplyQuery(Map<String, String> datamap)
			throws TException {
		ReturnData rd = new ReturnData();
		ServiceException retException = null;
		String optType ="platformApplyQuery";
		try {
			String  retString = platformService.platformApplyList(datamap);
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("platformApplyQuery," +"param:"+datamap.toString()+"\n"+rd.toString());
		
		return rd;
	}

	@Override
	@Transactional
	public ReturnData bangdingAccountList(Map<String, String> datamap)
			throws TException {
		// TODO Auto-generated method stub
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="bangdingAccountList";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = userService.queryBangAccountList(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("bangdingAccountList," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	@Transactional
	public ReturnData platformCallbackQuery(Map<String, String> datamap)
			throws TException {
		// TODO Auto-generated method stub
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="platformCallbackQuery";
		try {
			String  retString = platformService.platformCallbackQuery(datamap);
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("platformCallbackQuery," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	@Transactional
	public ReturnData bangindWx(Map<String, String> datamap) throws TException {
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="bangindWx";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = wXService.bundingWX(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("bundingWX," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}
	
	@Override
	@Transactional
	public ReturnData unbundWx(Map<String, String> datamap) throws TException {
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="unbundWx";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = wXService.unbundWX(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("unbundWx," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}
	

	@Override
	@Transactional
	public ReturnData getCompanyAccountByMobile(Map<String, String> datamap)
			throws TException {
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="getCompanyAccountByMobile";
		try {
			String  retString = userService.getCompanyAccountByMobile(datamap);
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("getCompanyAccountByMobile," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	@Transactional
	public ReturnData getCustomAccountByEmail(Map<String, String> datamap)
			throws TException {
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="getCustomAccountByEmail";
		try {
			String  retString = userService.getCustomAccountByEmail(datamap);
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("getCustomAccountByEmail," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}
	
	@Override
	@Transactional
	public ReturnData addMyAttn(Map<String, String> datamap)
			throws TException {
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="addMyAttn";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = myAttnService.addAttn(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("addMyAttn," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}
	
	@Override
	@Transactional
	public ReturnData delMyAttn(Map<String, String> datamap)
			throws TException {
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="delMyAttn";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				String  retString = myAttnService.delAttn(datamap);
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
				rd.setPojo(retString);
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("delMyAttn," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}
	
	@Override
	@Transactional
	public ReturnData listMyAttn(Map<String, String> datamap)
			throws TException {
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="listMyAttn";
		try {
			String  retString = myAttnService.listAttn(datamap);
			rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("listMyAttn," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	//1 用户注册  2 证书注册 3 平台注册  4子账号  5 图章 6 平台申请
	public synchronized boolean userRepeatSubmit(Map<String, String> datamap,int type)
	{
		log.info("用户注册缓存使用userRepeatSubmit==="+datamap.toString()+",type==="+type);
		
		String checkStr = "";
		//1 用户注册 
		if(1 == type)
		{
//			checkStr = datamap.get("appId")+"@"+datamap.get("account")+"@"+datamap.get("platformUserName");
			checkStr = datamap.get("appId")+"@"+datamap.get("platformUserName");
		}
		// 2 证书注册 3 平台注册  4子账号  5 图章 6 平台申请
		else if(2 == type)
		{
//			checkStr = datamap.get("appId")+"@"+datamap.get("account")+"@"+datamap.get("platformUserName")+"@"+datamap.get("certNum");
			checkStr = datamap.get("appId")+"@"+datamap.get("platformUserName")+"@"+datamap.get("certNum");
		}
		// 3平台注册
		else if(3 == type)
		{
			checkStr = datamap.get("serialNum")+"@"+datamap.get("businessLicenseNo")+"@"+datamap.get("companyName");
		}
		// 4 子账号 
		else if(4 == type)
		{
			checkStr = datamap.get("cAccount")+"@"+datamap.get("cIdNumber")+"@"+datamap.get("cMobile")+"@"+datamap.get("cEmail");
		}
		// 5图章 
		else if(5 == type)
		{
			checkStr = datamap.get("appId")+"@"+datamap.get("sealName")+"@"+datamap.get("userAccount")+"@"+datamap.get("sealPath");
		}
		//6 平台申请
		else if(6 == type)
		{
			checkStr = datamap.get("companyName")+"@"+datamap.get("email")+"@"+datamap.get("mobile")+"@"+datamap.get("contacts");
		}
		else
		{
			return false;
		}
		//判断是否重复注册
		if(null != (new CacheUtil().get(checkStr)))
		{
			return true;
		}
		else
		{
			new CacheUtil().set(checkStr);
			return false;
		}
	}

	@Override
	public ReturnData listCompanyMember(Map<String, String> datamap)
			throws TException
	{
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="listCompanyMember";
		try {
			String  retString = userService.getCustomMembersByCompanyName(datamap);
			//如果查询结果为空 返回空置
			if("".equals(retString))
			{
				rd.setRetCode(ConstantUtil.RETURN_RESULT_EMPTY[0]);
				rd.setDesc(ConstantUtil.RETURN_RESULT_EMPTY[1]);
				rd.setDescEn(ConstantUtil.RETURN_RESULT_EMPTY[2]);
			}
			else
			{
				rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
				rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
				rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			}
			rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("listCompanyMember," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Transactional
	@Override
	public ReturnData changeAppAdmin(Map<String, String> datamap)
			throws TException
	{
		// TODO Auto-generated method stub
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="changeAppAdmin";
		//验证请求是否达到阈值
		boolean judge = vptService.dealRequest(datamap, optType);
		try {
			if(!judge)
			{
				rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
				rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
				rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
			}
			else
			{
				if(ConstantUtil.FROM_MMEC.equals(datamap.get("optFrom")))
				{
					optType = "changeAppAdmin";
					String  retString = userService.changeAppAdmin(datamap);
					rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
					rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
					rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
					rd.setPojo(retString);
				}
				else
				{
					optType = "changeAppAdmin";
					String retString = datamap.get("optType");
					rd.setRetCode(ConstantUtil.RETURN_NORIGHT_ERROR[0]);
					rd.setDesc(ConstantUtil.RETURN_NORIGHT_ERROR[1]);
					rd.setDescEn(ConstantUtil.RETURN_NORIGHT_ERROR[2]);
					rd.setPojo(retString);
				}
			}
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("changeAppAdmin:"+"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}

	@Override
	public ReturnData queryUserExamineStatus(Map<String, String> datamap)throws TException {
		
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType="queryUserExamineStatus";
		String retString;
		try {
			retString=userService.queryUserExamineStatus(datamap);
			 rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			 rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			 rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			 rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("changeAppAdmin:"+"param:"+datamap.toString()+"\n"+rd.toString());
		
		return rd;
	}

	@Override
	public ReturnData synchronizationUserInfo(Map<String, String> datamap)throws TException {
		
		
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType="synchronizationUserInfo";
		
		
		
		String retString;
		try {
			retString=userService.synchronizationUserInfo(datamap);
			
			
			 rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
			 rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
			 rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
			 rd.setPojo(retString);
		} catch (ServiceException e) {
			retException = new ServiceException(e);
			rd.setRetCode(e.getErrorCode());
			rd.setDesc(e.getErrorDesc());
			rd.setDescEn(e.getErrorDescEn());
		}
		finally
		{
			try {
				logService.log(datamap,optType, retException, rd);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		//日志记录 
		log.info("changeAppAdmin:"+"param:"+datamap.toString()+"\n"+rd.toString());
		
		return rd;
	
	}
	
	
	//用户注册
	@Override
	public ReturnData userRegisterTUNIU(Map<String,String> datamap)throws TException {
		//权限控制
		ReturnData rd =new ReturnData();
		ServiceException retException = null;
		String optType ="userRegisterTUNIU";
		String  retString = "";
		
		if(userRepeatSubmit(datamap,1))
		{
			rd.setRetCode(ConstantUtil.RETURN_REPEAT_REGISTER[0]);
			rd.setDesc(ConstantUtil.RETURN_REPEAT_REGISTER[1]);
			rd.setDescEn(ConstantUtil.RETURN_REPEAT_REGISTER[2]);
		}
		else
		{
			//验证请求是否达到阈值
			boolean judge = vptService.dealRequest(datamap, optType);
			try {
				if(!judge)
				{
					rd.setRetCode(ConstantUtil.RETURN_VPT_MAX[0]);
					rd.setDesc(ConstantUtil.RETURN_VPT_MAX[1]);
					rd.setDescEn(ConstantUtil.RETURN_VPT_MAX[2]);
				}
				else
				{
					//途牛注册
					
					 optType = "saveTUNIUIdentity";
					 userService.saveIdentityTUNIU(datamap);
					 rd.setRetCode(ConstantUtil.RETURN_SUCC[0]);
					rd.setDesc(ConstantUtil.RETURN_SUCC[1]);
					rd.setDescEn(ConstantUtil.RETURN_SUCC[2]);
					rd.setPojo(retString);
					
				}
			} catch (ServiceException e) {
				
//					retException = new ServiceException(e);
				rd.setRetCode(e.getErrorCode());
				rd.setDesc(e.getErrorDesc());
				rd.setDescEn(e.getErrorDescEn());
			}
			finally
			{
				try {
					logService.log(datamap,optType, retException, rd);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
				Cache cache = CacheUtil.mmecCache;
				String checkStr = datamap.get("appId")+"#"+datamap.get("platformUserName");
				cache.remove(checkStr);
			}
			
		}
		//日志记录 
		log.info("userRegister," +"param:"+datamap.toString()+"\n"+rd.toString());
		return rd;
	}
	
	
}
