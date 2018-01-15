package com.mmec.centerService.contractModule.dao;

import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.mmec.centerService.contractModule.entity.ContractTemplateEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;

public interface ContractTemplateDao extends JpaRepository<ContractTemplateEntity,Integer>{
	
	/**
	 * 查询没有停用的模板
	 * @param templateNum
	 * @param CPlatform
	 * @return
	 */
	@Query("SELECT s FROM ContractTemplateEntity s WHERE s.status = 1 AND s.templateNum = :templateNum AND s.appId = :appId ")
	public ContractTemplateEntity findContractTemplateByTempNumber(@Param("templateNum") String templateNum,@Param("appId") String appId);
	
	//修改手机号码启/停用模板
	@Modifying
	@Query(value="update ContractTemplateEntity set status = :status where templateNum = :templateNum")
	public int updateTempleteStatus(@Param("status")byte status, @Param("templateNum")String templateNum);
		
	//根据状态查询平台下所有模板
	@Query("SELECT s FROM ContractTemplateEntity s WHERE s.CPlatform = :CPlatform and  s.status = :status ")
	public List<ContractTemplateEntity> queryContractTemplateListByAppIdAndStatus(@Param("CPlatform") PlatformEntity CPlatform,@Param("status") byte status);
		
	//根据状态查询平台下所有模板
	@Query("SELECT s FROM ContractTemplateEntity s WHERE s.CPlatform = :CPlatform ")
	public List<ContractTemplateEntity> queryContractTemplateListByAppId(@Param("CPlatform") PlatformEntity CPlatform);
		
	//根据状态查询用户创建的所有模板
	@Query("SELECT s FROM ContractTemplateEntity s inner join s.creator WHERE s.creator.bindedId = :bindedId and  s.status = :status ")
	public List<ContractTemplateEntity> queryContractTemplateListByUserIdAndStatus(@Param("bindedId")int bindedId,@Param("status") byte status);
		
	//根据状态查询用户创建的所有模板
	@Query("SELECT s FROM ContractTemplateEntity s inner join s.creator WHERE s.creator.bindedId = :bindedId")
	public List<ContractTemplateEntity> queryContractTemplateListByUserId(@Param("bindedId")int bindedId);
		
	//根据合同模版创建时间以及分页数查询
	
	@Query("SELECT c  FROM ContractTemplateEntity c where c.appId=? and c.status=? and c.creatTime  between ? and ? order by c.id desc")
	public List<ContractTemplateEntity> queryContractTemplateByPage(String appId,byte status,Date startime,Date endate,Pageable page);
	
	@Query(value="SELECT c  FROM ContractTemplateEntity  c where c.appId=? and c.status=? and TO_DAYS(c.creatTime) - TO_DAYS(?)<=0 order by c.id desc")
	public List<ContractTemplateEntity> queryContractTemplateByPage1(String appId,byte status,Date endate,Pageable page);
	
	@Query(value="SELECT c  FROM ContractTemplateEntity c where c.appId=? and c.creatTime  between ? and ? order by c.id desc")
	public List<ContractTemplateEntity> queryContractTemplateByPage2(String appId,Date startime,Date endate,Pageable page);
	
	@Query(value="SELECT c  FROM ContractTemplateEntity c where c.appId=? and TO_DAYS(c.creatTime) - TO_DAYS(?)<=0 order by c.id desc")
	public List<ContractTemplateEntity> queryContractTemplateByPage3(String appId,Date endate,Pageable page);
	
	@Query(value="SELECT count(c.id)  FROM ContractTemplateEntity c where c.appId=? and c.status=? and c.creatTime  between ? and ? order by c.id desc")
	public Long queryContractTemplateCount(String appId,byte status,Date startime,Date endate);
	
	@Query(value="SELECT count(c.id)  FROM ContractTemplateEntity c where c.appId=? and  c.creatTime  between ? and ? order by c.id desc")
	public Long queryContractTemplateCount1(String appId,Date startime,Date endate);
	
	@Query(value="SELECT count(c.id)  FROM ContractTemplateEntity c where c.appId=? and TO_DAYS(c.creatTime) - TO_DAYS(?)<=0 order by c.id desc")
	public Long queryContractTemplateCount2(String appId,Date endate);
	
	@Query(value="SELECT count(c.id)  FROM ContractTemplateEntity c where c.appId=? and c.status=? and TO_DAYS(c.creatTime) - TO_DAYS(?)<=0 order by c.id desc")
	public Long queryContractTemplateCount3(String appId,byte status,Date endate);
	
	//根据主键ID 查询模板信息
	public ContractTemplateEntity findById(int Id);
	//根据模板编码 查询模板信息
	public ContractTemplateEntity findByTemplateNum(String templateNum);
}
