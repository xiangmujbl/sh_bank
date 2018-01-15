package com.mmec.centerService.userModule.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mmec.centerService.userModule.entity.AttachmentEntity;

public interface AttachmentInfoDao  extends JpaRepository<AttachmentEntity,Integer> {
	
	//平台用户根据 平台申请信息 通过平台编码 查询出 平台申请信息中的 申请人身份证附件
	@Query(value="select p.CAttachment from PlatformApplyEntity p inner join p.CPlatform where p.CPlatform.id = :id")
	public AttachmentEntity queryAttachmentEntityByPlatformId(int platformId);
}

