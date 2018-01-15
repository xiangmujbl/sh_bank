package com.mmec.business.service;

import java.util.Map;

import com.mmec.thrift.service.ResultData;
import com.mmec.thrift.service.ReturnData;

/**
 * 此接口类的所有方法都是提供给内部系统用,不对外开发
 * @author Administrator
 *
 */
public interface InternelService {

	public ResultData serverSign(String dataSource);
	public ResultData getTimestamp(String contSerialNum, String certFingerprint);
	public ReturnData eventCertRequest(String customerType,String userName,String cardId,String code);
	public ReturnData serverCertRequest();
	public ReturnData customizeSign(String dataSource);
}
