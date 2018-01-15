package com.mmec.centerService.userModule.service;
/*
 * 图章
 */
import java.util.Map;

import com.mmec.exception.ServiceException;

public interface SealService {
	public String addSeal(Map<String,String> datamap)throws ServiceException;
	public String delSeal(Map<String,String> datamap)throws ServiceException;
	public String querySeal(Map<String,String> datamap)throws ServiceException;
}
