package com.mmec.centerService.contractModule.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.contractModule.entity.SmsTemplateEntity;
import com.mmec.centerService.contractModule.entity.SmsTransEntity;

public interface SmsTransDao extends JpaRepository<SmsTransEntity,Integer>{
	@Query("SELECT s FROM SmsTransEntity s WHERE s.appId =:appId ")
	public SmsTransEntity querySmsTrans(@Param("appId") String appId);	
	//查询短信记录
	@Query(value="SELECT count(c.id)  FROM SmsRecordEntity c where c.optfrom=? and  c.receiveResult=?")
	public Long querySmsCodeCount(String optFrom,String receive_result);
}
