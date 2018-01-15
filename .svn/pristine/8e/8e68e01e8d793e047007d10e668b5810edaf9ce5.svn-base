package com.mmec.centerService.depositoryModule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mmec.centerService.depositoryModule.entity.EvidenceBindAttachmentEntity;

public interface EvidenceBindAttachmentDao extends JpaRepository<EvidenceBindAttachmentEntity,Integer>{
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	@Query("SELECT c FROM EvidenceBindAttachmentEntity  c WHERE c.id = ?")
	public EvidenceBindAttachmentEntity findEvidenceById(int id);
	
	@Query("SELECT c FROM EvidenceBindAttachmentEntity  c WHERE c.evidence_id= ?")
	public List<EvidenceBindAttachmentEntity> findEvidenceByEvidenceId(int evidence_id);
}