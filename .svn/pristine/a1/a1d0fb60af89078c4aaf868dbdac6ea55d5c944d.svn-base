package com.mmec.centerService.userModule.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.contractModule.entity.ContractPathEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.SealEntity;

public interface SealInfoDao  extends JpaRepository<SealEntity,Integer> {
	//根据主键ID 查询图章信息
	public SealEntity findBySealId(int sealId);
	//根据主键ID 查询图章信息
	public SealEntity findBySealNum(String sealNum);
	
	//根据图章编号和userId查询图章信息
	@Query(value="select s from SealEntity s where s.relatedId = :relatedId and s.sealNum =:sealNum and s.isActive = 0")
	public SealEntity querySealBySealNumAndUserId(@Param("relatedId")int relatedId,@Param("sealNum")String sealNum);
	
	@Modifying
	@Query(value="update SealEntity set isActive = 1 where sealId = :sealId and sealType = :sealType and relatedId = :relatedId")
	public int delSealInfo(@Param("sealId")int sealId,@Param("sealType")byte sealType,@Param("relatedId")int relatedId);

	//根据手机号码 查询图章信息
	@Query(value="select s from SealEntity s where s.relatedId = :relatedId and s.sealType =:sealType and s.isActive = 0")
	public List<SealEntity> querySealList(@Param("relatedId")int relatedId,@Param("sealType")byte sealType);
	
	//根据userId查询图章信息
	@Query(value="select s from SealEntity s  where  s.relatedId =:relatedId ")
	public List<SealEntity> querySealNum(@Param("relatedId")int relatedId);

}
