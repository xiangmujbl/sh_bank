package com.mmec.centerService.contractModule.service;

import java.util.List;
import java.util.Map;

import com.mmec.exception.ServiceException;
import com.mmec.thrift.service.ReturnData;

public interface DownloadService {
	public ReturnData zipDownload(Map<String, String> datamap) throws ServiceException;
	public ReturnData baoquanDownload(String serialNum) throws ServiceException;
	public ReturnData pdfDownload(Map<String, String> datamap) throws ServiceException;
	public ReturnData internetFinanceDownload(Map<String, String> datamap) throws ServiceException;
	public List<String> imgPath(String path);
}
