package com.mmec.business.service;

import java.util.Map;

import com.mmec.business.bean.UserBean;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.Result;

public interface UserService {

	public ReturnData registerUser(String appId, UserBean user, String requestIp, Map map);
	
	public ReturnData registerUserTUNIU(String appId, UserBean user, String ip,Map imgMap);

	public ReturnData changePwd(String appId, String pwd, String newPwd, String userId, String requestIp);

	public ReturnData changeUserAdmin(String appId, String userId, String requestIp);

	public ReturnData userQuery(String optFrom, String appId, String userId);

	public ReturnData userQueryByMobile(String appId, String mobile);
	
	public ReturnData userQueryByUserId(String optFrom, String appId, String userId);

	public ReturnData platformQuery(String appId);

	public ReturnData listAttn(String optFrom, String appId, String userId, String param);

	public String userUpdate(String appId, String userId, String info);

	public Result isAdminUser(String appId, String userId);
	
	/////////6.06///////////
	public Result isAdminAuth(String appId, String authorUserId);
	/////////6.06//////////
}
