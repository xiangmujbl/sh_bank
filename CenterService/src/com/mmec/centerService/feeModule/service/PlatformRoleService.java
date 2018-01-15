package com.mmec.centerService.feeModule.service;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.userModule.entity.AuthEntity;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;
import com.mmec.util.pdf.CertBean;


public interface PlatformRoleService{
	/**
	 * 根据c_identity表id查询权限
	 */
	public List<AuthEntity> queryAuth(int userid) throws ServiceException;
}