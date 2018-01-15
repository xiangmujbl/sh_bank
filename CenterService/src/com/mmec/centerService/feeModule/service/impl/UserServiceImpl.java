package com.mmec.centerService.feeModule.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mmec.centerService.feeModule.dao.ContractDeductRecordDao;
import com.mmec.centerService.feeModule.dao.PayServiceDao;
import com.mmec.centerService.feeModule.dao.UserServiceDao;
import com.mmec.centerService.feeModule.entity.UserServiceEntity;
import com.mmec.centerService.feeModule.service.UserService;
import com.mmec.centerService.userModule.dao.IdentityDao;
import com.mmec.exception.ServiceException;
import com.mmec.util.ConstantUtil;
import com.mmec.util.JSONUtil;

@Service("userService")
public class UserServiceImpl implements UserService{
	
	private static Logger log=Logger.getLogger(UserServiceImpl.class);
	
	@Autowired
	private  UserServiceDao userServiceDao;
	
	@Autowired
	private  IdentityDao identityDao;
	
	@Autowired
	private  PayServiceDao payServiceDao; 
	
	@Autowired
	private ContractDeductRecordDao contractDeductRecordDao;
	
	/**
	 * 保存一条用户-服务记录--次
	 */
	public String checkUserService(int userid,String paycode,int paytype) throws ServiceException{
		try{
			log.info("entry method checkUserService,params:userid"+String.valueOf(userid)+",paycode"+paycode);
			//userid对应的用户记录不存在
			if(null==identityDao.findById(userid)){
				throw new ServiceException(ConstantUtil.RETURN_USER_NOTEXIST[0],
						ConstantUtil.RETURN_USER_NOTEXIST[1],ConstantUtil.RETURN_USER_NOTEXIST[2]
						);
			}
			//userid对应的服务不存在
			if(null==payServiceDao.findByTypeCode(paycode)){
				throw new ServiceException(ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[0],
						paycode+ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[1],ConstantUtil.RETURN_PAY_CODE_NOT_EXIST[2]);
			}
			//userid和paycode对应的服务不存在---新增该条记录
			if(null==userServiceDao.findByUserIdAndPayCode(userid, paycode)){
				//新增一条记录
				UserServiceEntity us=new UserServiceEntity();
				us.setPayCode(paycode);
				us.setUserId(userid);
				us.setChargingTimes(0);
				us.setPayType((byte)paytype);
				userServiceDao.save(us);
				log.info("save a new Record of userService:"+us.toString());
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ConstantUtil.SAVE_USER_EXCEPTION[0],
				paycode+ConstantUtil.SAVE_USER_EXCEPTION[1],ConstantUtil.SAVE_USER_EXCEPTION[2]);
		}
		return null;
	}
	
	/**
	 * 给某个用户-服务增加次数
	 */
	public String addUserServiceTimes(int userid,String paycode,int chargetimes){
		log.info("entry method addUserServiceTimes,params:userid"+String.valueOf(userid)+",paycode"+paycode
				+",chargetimes"+String.valueOf(chargetimes));
		int times=userServiceDao.findByUserIdAndPayCode(userid, paycode).getChargingTimes();
		userServiceDao.updateUserServiceTimes((times+chargetimes),userid, paycode);
		log.info("addUserServiceTimes success");
		return null;
	}
	
	/**
	 * 给某个用户-服务减少次数
	 */
	public String  reduceUserServiceTimes(int userid,String paycode,int chargetimes)throws ServiceException{
		log.info("entry method reduceUserServiceTimes,params:userid"+String.valueOf(userid)+",paycode"+paycode
				+",chargetimes"+String.valueOf(chargetimes));
		int times=userServiceDao.findByUserIdAndPayCode(userid, paycode).getChargingTimes();
		//用户次数不够---抛出异常
		if((times-chargetimes)<0){
			throw new ServiceException(ConstantUtil.RETURN_TIMES_NOT_ENOUGH[0],"目前剩余"+String.valueOf(times)+"次,不足以支付"+String.valueOf(chargetimes)+"次"
					,ConstantUtil.RETURN_TIMES_NOT_ENOUGH[2]);		
			}
		userServiceDao.updateUserServiceTimes((times-chargetimes),userid, paycode);
		log.info("reduceUserServiceTimes success");
		return null;
	}
	
	/**
	 * 查询某个用户的服务-服务次数
	 */
	public UserServiceEntity queryByUserIdAndPayCode(int userid,String paycode){
		log.info("entry method queryByUserIdAndPayCode,params:userid"+String.valueOf(userid)+",paycode"+paycode);
		UserServiceEntity us=userServiceDao.findByUserIdAndPayCode(userid, paycode);
		return us;
	}
	
	/**
	 * 查询某个用户的服务
	 */
	public List<UserServiceEntity> queryByUserId(int userid){
		log.info("entry method queryByUserId,params:userid"+String.valueOf(userid));
		List<UserServiceEntity> userlist=new ArrayList<UserServiceEntity>();
		userlist=userServiceDao.findByUserId(userid);
		return userlist;
	}
	
	/**
	 *根据paycode查询
	 */
	public List<UserServiceEntity> queryByPayCode(String payCode){
		log.info("entry method queryByPayCode,params:paycode"+payCode);
		List<UserServiceEntity> userlist=new ArrayList<UserServiceEntity>();
		userlist=userServiceDao.findByPayCode(payCode);
		return userlist;
	}
}
