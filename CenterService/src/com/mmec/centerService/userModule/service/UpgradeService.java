package com.mmec.centerService.userModule.service;

import java.util.Map;

import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;

public interface UpgradeService {
	public ReturnData upgradeQuery(Map<String, String> datamap) throws ServiceException;
}
