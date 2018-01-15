package com.mmec.aps.service;

import com.mmec.thrift.service.CertKey;
import com.mmec.thrift.service.Result;

/**
 * 查询证书状态接口
 * 
 * @author Administrator
 *
 */
public interface QueryCertStatusService {
	/**
	 * 
	 * @return Result
	 */
	public Result excute(CertKey certKey);
}
