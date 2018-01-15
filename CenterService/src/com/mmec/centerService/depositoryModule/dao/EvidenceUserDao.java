package com.mmec.centerService.depositoryModule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mmec.centerService.depositoryModule.entity.EvidenceUserEntity;

public interface EvidenceUserDao extends JpaRepository<EvidenceUserEntity,Integer>{
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	@Query("SELECT c FROM EvidenceUserEntity  c WHERE c.id = ?")
	public EvidenceUserEntity findEvidenceUserById(int id);
	
	/**
	 * 根据UserId查询
	 * @param id
	 * @return
	 */
	@Query("SELECT c FROM EvidenceUserEntity  c WHERE c.userid = ?")
	public EvidenceUserEntity findEvidenceUserByUserId(int userid);
	
	
	/**
	 * 根据EvidenceId查询
	 */
	@Query("SELECT c FROM EvidenceUserEntity  c WHERE c.evidenceid = ?")
	public List<EvidenceUserEntity> findEvidenceUserByEvidenceId(int evidenceid);
}