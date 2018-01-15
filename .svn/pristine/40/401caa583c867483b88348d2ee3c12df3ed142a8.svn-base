package com.mmec.centerService.userModule.service.impl;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mmec.centerService.userModule.dao.PlatformDao;
import com.mmec.centerService.userModule.dao.UserAuthDao;
import com.mmec.centerService.userModule.entity.AuthEntity;
import com.mmec.centerService.userModule.entity.PlatformApplyRecordEntity;
import com.mmec.centerService.userModule.service.UserAuthService;
import com.mmec.exception.ServiceException;
import com.mmec.util.ConstantUtil;
@Service("userAuthServiceImpl")
public class UserAuthServiceImpl extends UserBaseService implements UserAuthService {
	@Autowired
	private UserAuthDao userAuthDao;
	
	@Override
	public String queryUserAuth(Map<String, String> datamap)
			throws ServiceException {
		// 审批编号
		String optFrom = datamap.get("optFrom");
		int userId;
		try {
			userId = Integer.parseInt(datamap.get("userId"));
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			//抛出异常   参数异常
			throw new ServiceException(ConstantUtil.RETURN_FAIL_PARAMERROR[0],
					ConstantUtil.RETURN_FAIL_PARAMERROR[1]+"用户账号异常", ConstantUtil.RETURN_FAIL_PARAMERROR[2]+" userId is notCorrect!");

		}
		String retStr = "";
		if(ConstantUtil.FROM_MMEC.equals(optFrom))
		{
			 try {
				List<AuthEntity> datalist = userAuthDao.queryMMECAuthByUserId(userId);
				//组装成JS对象输出
				JSONArray jsonArray = new JSONArray();
				for(AuthEntity i : datalist)
				{
					jsonArray.add(Bean2JSON(i));
				}
				JSONObject jo = new JSONObject();
				jo.put("list", jsonArray);
				retStr = jo.toString();
			} catch (Exception e) {
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
		}
		else if(ConstantUtil.FROM_YUNSIGN.equals(optFrom))
		{
			try {
				retStr = userAuthDao.queryYSChildAuthByUserId(userId);
			} catch (Exception e) {
				throw new ServiceException(ConstantUtil.RETURN_DB_ERROR[0],
						ConstantUtil.RETURN_DB_ERROR[1], ConstantUtil.RETURN_DB_ERROR[2]+e.getMessage());
			}
		}
		return null;
	}

}
