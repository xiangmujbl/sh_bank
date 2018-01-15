package com.mmec.centerService.depositoryModule.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.mmec.centerService.depositoryModule.entity.EvidenceEntity;


public interface EvidenceDao extends PagingAndSortingRepository<EvidenceEntity,Integer>
{
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	@Query("SELECT c FROM EvidenceEntity c WHERE c.id = ?")
	public EvidenceEntity findEvidenceById(int id);
	
	/**
	 * 条件过滤 分页查询
	 * @param spec
	 * @param pageable
	 * @return
	 */
	public Page<EvidenceEntity>findAll(Specification<EvidenceEntity> spec,Pageable pageable);
	
	/**
	 * 根据流水查询
	 */
	@Query("SELECT c FROM EvidenceEntity c WHERE c.serial = ?")
	public EvidenceEntity findEvidenceBySerial(String serial);
	
	
	/**
	 * 根据流水查询
	 */
	@Query("SELECT c FROM EvidenceEntity c WHERE c.appid = ? and c.order=?")
	public EvidenceEntity findEvidenceByAppIdAndOrderId(String appid,String order);
	
}
