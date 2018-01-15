package com.mmec.centerService.feeModule.service.impl;

import java.util.Map;

import org.apache.thrift.TException;

import com.mmec.centerService.feeModule.entity.IdAuthConfigEntity;
import com.mmec.centerService.feeModule.service.IdAuthJudgeService;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.AuthFeeRMIServices.Iface;
import com.mmec.thrift.service.ReturnData;


public class AuthFeeRMIServiceImpl implements Iface {

	private IdAuthJudgeService idAuthJudgeService;
	
	@Override
	public ReturnData enterpriseAuthFee(Map<String, String> datamap) throws TException {
		ReturnData returnData = new ReturnData();
		IdAuthConfigEntity idAuthConfigEntity = new IdAuthConfigEntity();
		try{
			idAuthJudgeService.isAuthFee(datamap);
			idAuthConfigEntity  = idAuthJudgeService.isAuthFee(datamap);
			idAuthJudgeService.updateTimes(idAuthConfigEntity);
		}catch(ServiceException e)
		{
			returnData = new ReturnData(e.getErrorCode(), e.getErrorDesc(), e.getErrorDescEn(), "");
		}
		finally {
			try {
				idAuthJudgeService.authLog(datamap, idAuthConfigEntity);
			} catch (ServiceException e) {
				
				e.printStackTrace();
			}
		}
		return returnData;
	}

}
