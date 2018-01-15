package com.mmec.centerService.contractModule.service;

import java.util.Map;

import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;

public interface SignContractService {
	public ReturnData serverCertZipSign(Map<String, String> datamap,ContractEntity contract,IdentityEntity identity,PlatformEntity platformEntity) throws ServiceException;
	public ReturnData serverCertPdfSign(Map<String, String> datamap,ContractEntity contract,IdentityEntity identity,PlatformEntity platformEntity) throws ServiceException;
	public ReturnData hardCertZipSign(Map<String, String> datamap,ContractEntity contract,IdentityEntity identity,PlatformEntity platformEntity) throws ServiceException;
	public ReturnData hardCertPdfSign(Map<String, String> datamap,ContractEntity contract,IdentityEntity identity,PlatformEntity platformEntity) throws ServiceException;
	public ReturnData eventCertPdfSign(Map<String, String> datamap,ContractEntity contract,IdentityEntity identity,PlatformEntity platformEntity) throws ServiceException;
	public ReturnData eventCertZipSign(Map<String, String> datamap,ContractEntity contract,IdentityEntity identity,PlatformEntity platformEntity) throws ServiceException;
}
