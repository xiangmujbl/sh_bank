package com.mmec.business.service;

import com.mmec.business.bean.UserBean;
import com.mmec.thrift.service.ReturnData;

public interface BangdingCertService extends BaseService {

	//public UserBean findUserDataByUcid(String appid, String ucid);

	public ReturnData certBund(String appid, String ucid, String certNum, String certContent, String type,
			String beginTime, String endTime, String subjectItem, String signature, String data, String certSubject,
			String requestIp);

	public ReturnData certUnbund(String appid, String ucid, String certId, String certNum, String requestIp);

	public ReturnData certQuery(String appid, String ucid, String certNum, String certContent);
}
