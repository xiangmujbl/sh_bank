package com.mmec.centerService.userModule.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.mmec.centerService.userModule.dao.UpgradeDao;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.centerService.userModule.entity.UpgradeEntity;
import com.mmec.centerService.userModule.service.UpgradeService;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.ConstantUtil;

@Service("upgradeService")
public class UpgradeServiceImpl  extends UserBaseService  implements UpgradeService {

	@Autowired
	private UpgradeDao upgradeDao;
	
	@Override
	public ReturnData upgradeQuery(Map<String, String> datamap) throws ServiceException {
		//判断平台是否存在
		ReturnData returnData = null;
		String appId = (String) datamap.get("appId");
		PlatformEntity platformEntity = checkPlatform(appId);
		UpgradeEntity upgradeEntity = upgradeDao.findUpgrade(platformEntity.getAppId());
		String pojo = "";
		Map<String,String> retMap = new HashMap<String,String>();
		if(null != upgradeEntity)
		{
			retMap.put("oldAppId", upgradeEntity.getOldAppId());
			retMap.put("oldAppkey", upgradeEntity.getOldAppkey());
			retMap.put("upgradeTime", upgradeEntity.getOldAppkey());
			pojo = new Gson().toJson(retMap);
			returnData = new ReturnData(ConstantUtil.RETURN_SUCC[0], ConstantUtil.RETURN_SUCC[1], ConstantUtil.RETURN_SUCC[2], pojo);
		}
		else
		{
			returnData = new ReturnData(ConstantUtil.RETURN_RESULT_EMPTY[0], ConstantUtil.RETURN_RESULT_EMPTY[1], ConstantUtil.RETURN_RESULT_EMPTY[2], "");
		}
		return returnData;
	}

}
