package com.mmec.centerService.feeModule.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mmec.centerService.feeModule.dao.SmsRecordDao;
import com.mmec.centerService.feeModule.entity.SmsRecordEntity;
import com.mmec.centerService.feeModule.service.SmsRecordService;


@Service("smsRecordService")
public class SmsRecordServiceImpl implements SmsRecordService{
	
	private static Logger log=Logger.getLogger(SmsRecordServiceImpl.class);

	@Autowired
	private SmsRecordDao smsRecordDao;
	
	@Override
	@Transactional
	public void saveSmsRecord(SmsRecordEntity record) {
		smsRecordDao.save(record);
	}
}