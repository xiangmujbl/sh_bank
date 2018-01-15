package com.mmec.centerService.contractModule.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.contractModule.entity.SmsTemplateEntity;

public interface MessageDao extends JpaRepository<SmsTemplateEntity,Integer>{
	@Query("SELECT s FROM SmsTemplateEntity s WHERE s.id =:id ")
	public SmsTemplateEntity querySmsTemplate(@Param("id") int id);	
	
	public SmsTemplateEntity findByOperateTypeAndMessageType(@Param("operateType") String operateType,@Param("messageType")int messageType);
}
