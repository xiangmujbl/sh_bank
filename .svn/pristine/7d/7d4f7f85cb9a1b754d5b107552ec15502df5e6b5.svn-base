package com.mmec.centerService.contractModule.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.contractModule.entity.SignRecordEntity;
import com.mmec.centerService.contractModule.entity.SmsInfoEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;

public interface SignRecordDao extends JpaRepository<SignRecordEntity,Integer>{
	/*
	 * 查询所有签署人记录,不管有没有签署
	 */
	@Query(" SELECT s FROM SignRecordEntity s WHERE s.CContract = :CContract AND s.signType != 1 ORDER BY s.signTimestamp DESC ")
	public List<SignRecordEntity> querySignRecordByContractId(@Param("CContract")ContractEntity CContract);
	
	/**
	 * 查出授权代签的签署记录
	 * @param CContract
	 * @return
	 */
	@Query(" SELECT s FROM SignRecordEntity s WHERE s.CContract = :CContract AND s.signType != 1 AND s.authorId != 0 ORDER BY s.signTimestamp DESC ")
	public List<SignRecordEntity> queryAuthoritySignRecordByContractId(@Param("CContract")ContractEntity CContract);
	
	@Query(" SELECT s FROM SignRecordEntity s WHERE s.CContract = :CContract AND s.signdata != '' ORDER BY s.signTimestamp DESC ")
	public List<SignRecordEntity> findSignRecordByContractId(@Param("CContract")ContractEntity CContract);
	
	/*
	 * 将非服务组未签署的记录都查出来
	 */
	@Query(" SELECT s FROM SignRecordEntity s WHERE s.CContract = :CContract AND s.signType != 1 AND s.signdata = '' ORDER BY s.signTimestamp DESC ")
	public List<SignRecordEntity> findCustomSignRecordByContractId(@Param("CContract")ContractEntity CContract);
	/*
	 * 查询客户组已经签署记录
	 */
	@Query(" SELECT s FROM SignRecordEntity s WHERE s.CContract = :CContract AND s.signType != 1 AND s.signdata != '' ORDER BY s.signTimestamp DESC ")
	public List<SignRecordEntity> findCustomSignHasSignRecordByContractId(@Param("CContract")ContractEntity CContract);
	
	/*
	 * 将服务组签署的记录都查出来
	 */
	@Query("SELECT s FROM SignRecordEntity s WHERE s.CContract = :CContract AND s.signType = 1 AND s.signdata != '' ORDER BY s.signTimestamp DESC")
	public List<SignRecordEntity> findServiceSignRecordByContractId(@Param("CContract")ContractEntity CContract);
	//查询未签署过
	@Query(" SELECT s FROM SignRecordEntity s WHERE s.signStatus = 0 AND s.CContract = :CContract AND s.CIdentity = :CIdentity ")
	public SignRecordEntity findSignRecordByAppIdUcid(@Param("CContract")ContractEntity CContract,@Param("CIdentity")IdentityEntity CIdentity);
	
	//查询已签署过
	@Query(" SELECT s FROM SignRecordEntity s WHERE s.signStatus = 1 AND s.CContract = :CContract AND s.CIdentity = :CIdentity ")
	public SignRecordEntity findNoSignRecordByAppIdUcid(@Param("CContract")ContractEntity CContract,@Param("CIdentity")IdentityEntity CIdentity);
		
//	@Query(" SELECT s FROM SignRecordEntity s  WHERE s.authorId = 0 AND s.CContract = :CContract AND s.CIdentity = :CIdentity ")
//	public List<SignRecordEntity> findSignRecordByAppIdUcid(@Param("CContract")ContractEntity CContract,@Param("CIdentity")IdentityEntity CIdentity);

	/**
	 * 
	 * @param sha1Digest
	 * @param signInformation
	 * @param orignalFilename
	 * @param prevSha1
	 * @param currentSha1
	 * @param signTime
	 * @param signdata
	 * @param signStatus
	 * @param mark
	 * @param signMode:签署模式1,服务器签署 2,事件证书签署 3,硬件证书签署
	 * @param CSmsInfo
	 * @param signTimestamp
	 * @param signType 1表示服务组签名,2表示非服务组签名
	 * @param CContract
	 * @param CIdentity
	 * @return
	 */
	@Modifying
	@Query(" UPDATE SignRecordEntity d SET d.sha1Digest = :sha1Digest,d.signInformation = :signInformation, d.orignalFilename =:orignalFilename,d.prevSha1 = :prevSha1," +
			"d.currentSha1 = :currentSha1,d.signTime = :signTime,d.signdata = :signdata,d.signStatus = :signStatus,d.mark = :mark,d.signMode = :signMode,d.CSmsInfo = :CSmsInfo," +
			"d.signTimestamp = :signTimestamp,d.signType = :signType,d.passEncoded =:passEncoded,d.password=:password,d.alias=:alias,d.certificatePath=:certificatePath  " +
			"where d.signStatus = 0 AND d.CContract = :CContract AND d.CIdentity = :CIdentity ")
	public int updateSignRecord(@Param("sha1Digest") String sha1Digest,
			@Param("signInformation") String signInformation,
			@Param("orignalFilename") String orignalFilename,
			@Param("prevSha1") String prevSha1,
			@Param("currentSha1") String currentSha1,
			@Param("signTime") Date signTime,
			@Param("signdata") String signdata,
			@Param("signStatus") byte signStatus, 
			@Param("mark") String mark,
			@Param("signMode") byte signMode,
			@Param("CSmsInfo") SmsInfoEntity CSmsInfo,
			@Param("signTimestamp") long signTimestamp,
			@Param("signType") byte signType,
			@Param("CContract") ContractEntity CContract,
			@Param("CIdentity") IdentityEntity CIdentity,
			@Param("passEncoded") String passEncoded,
			@Param("password") String password,
			@Param("alias") String alias,
			@Param("certificatePath") String certificatePath
			);
	@Modifying
	@Query(" UPDATE SignRecordEntity d SET d.signTime = :signTime,d.signStatus = :signStatus  where d.CContract = :CContract and d.CIdentity = :CIdentity ")
	public int updateSignRecordStatus(@Param("signTime") Date signTime,@Param("signStatus") byte signStatus, @Param("CContract") ContractEntity CContract,@Param("CIdentity") IdentityEntity CIdentity);
	
	/**
	 * @param signType 1表示服务组签名,2表示非服务组签名
	 * @param signTime
	 * @param signStatus
	 * @param CContract
	 * @param CIdentity
	 * @return
	 */
	@Modifying
	@Query(" UPDATE SignRecordEntity d SET d.signTime = :signTime,d.signStatus = :signStatus, d.signdata = :signdata,d.mark = :mark,"
			+ "d.signType = :signType,d.authorId = :authorId where d.CContract = :CContract and d.CIdentity = :CIdentity ")
	public int updateAuthorSignRecordStatus(@Param("signTime") Date signTime, @Param("signStatus") byte signStatus,
			@Param("CContract") ContractEntity CContract, @Param("CIdentity") IdentityEntity CIdentity,
			@Param("signdata")String signdata,@Param("mark") String mark,@Param("signType") byte signType,@Param("authorId") int authorId);

	@Modifying
	@Query(" DELETE SignRecordEntity d where d.CContract = :CContract")
	public int deleteSignRecord(@Param("CContract") ContractEntity CContract);
}
