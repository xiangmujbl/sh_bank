package com.mmec.centerService.contractModule.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.contractModule.entity.ContractImgBean;
import com.mmec.centerService.userModule.entity.PlatformEntity;

public interface ContractDao extends JpaRepository<ContractEntity,Integer>{
	@Query("SELECT s FROM ContractEntity s WHERE s.serialNum = :serialNum")
	public ContractEntity findContractBySerialNum(@Param("serialNum")String serialNum);
	@Modifying
	@Query(" UPDATE ContractEntity s SET s.updateTime = :updateTime, s.operator = :operator, s.status = :status, s.finishtime = :finishtime WHERE s.serialNum = :serialNum ")
	public int updataContractStatus(@Param("updateTime") Date updateTime,
			@Param("operator") String operator, @Param("status") byte status,
			@Param("finishtime") Date finishtime,
			@Param("serialNum") String serialNum);
	
	@Query("SELECT s FROM ContractEntity s WHERE s.orderId = :orderId ")
	public ContractEntity queryContractByid(@Param("orderId") String orderId);
	/**
	 * @param isPdfSign 是否为pdf签署
	 * @param updateTime
	 * @param operator
	 * @param status
	 * @param finishtime
	 * @param serialNum
	 * @return
	 */
	@Modifying
	@Query(" UPDATE ContractEntity s SET s.updateTime = :updateTime, s.operator = :operator, s.status = :status, s.finishtime = :finishtime, s.isPdfSign = :isPdfSign WHERE s.serialNum = :serialNum ")
	public int updataContractStatus(@Param("updateTime") Date updateTime,
			@Param("operator") String operator, @Param("status") byte status,
			@Param("finishtime") Date finishtime,
			@Param("serialNum") String serialNum,@Param("isPdfSign") String isPdfSign);
			
	/**
	 * @param isShow 合同是否可见
	 * @param updateTime
	 * @param operator
	 * @param status
	 * @param finishtime
	 * @param serialNum
	 * @return
	 */
	@Modifying
	@Query(" UPDATE ContractEntity s SET s.updateTime = :updateTime, s.operator = :operator, s.status = :status, s.finishtime = :finishtime, s.isShow = :isShow WHERE s.serialNum = :serialNum ")
	public int updataContractStatus(@Param("updateTime") Date updateTime,
			@Param("operator") String operator, @Param("status") byte status,
			@Param("finishtime") Date finishtime,
			@Param("serialNum") String serialNum,@Param("isShow") byte isShow);
	
	@Query("SELECT s FROM ContractEntity s WHERE s.orderId = :orderId AND s.CPlatform = :CPlatform")
	public ContractEntity findContractByAppIAndOrderId(@Param("orderId") String orderId, @Param("CPlatform") PlatformEntity CPlatform);
	
//	@Query("SELECT s FROM ContractEntity s WHERE s.orderId = :orderId AND s.CPlatform = :CPlatform AND s.draftContractIsActivate = 1")
//	public ContractEntity findContractByAppIAndOrderId(@Param("orderId") String orderId, @Param("CPlatform") PlatformEntity CPlatform);
	
	@Modifying
	@Query(" UPDATE ContractEntity s SET s.updateTime = :updateTime,s.isDelete = :isDelete WHERE s.id = :id ")
	public int deleteContract(@Param("updateTime") Date updateTime,@Param("isDelete") byte isDelete,@Param("id") int id);
	

	//根据主键ID 查询用户信息
	public ContractEntity findById(int Id);
	
	//根据创建时间查找待转换图片的合同列表
	@Query("SELECT new com.mmec.centerService.contractModule.entity.ContractImgBean(s,cp,i) FROM ContractEntity s,ContractPathEntity cp,IdentityEntity i WHERE s = cp.CContract and s.creator = i.id and  s.turnImgStatus = :turnImgStatus")
	public List<ContractImgBean> findContractEntityBycreateTimeAndTurnStatus(@Param("turnImgStatus")int turnImgStatus);
	/**
	 * 
	 * @param updateTime
	 * @param serialNum
	 * @return
	 */
	@Modifying
	@Query(" UPDATE ContractEntity s SET s.updateTime = :updateTime, s.turnImgStatus = :turnImgStatus WHERE s.serialNum = :serialNum ")
	public int updateTurnContractStatus(@Param("updateTime") Date updateTime,@Param("turnImgStatus") int turnImgStatus, @Param("serialNum") String serialNum);

	/**
	 * 草稿箱更新合同
	 * @param serialNum
	 * @return
	 */
	@Modifying
	@Query(" UPDATE ContractEntity s SET s.updateTime = :updateTime, s.title = :title,s.keyword = :keyword,s.pname = :pname,"
			+ "s.price = :price,s.deadline = :deadline,s.otheruids = :otheruids,s.status = :status,s.signPlaintext = :signPlaintext,"
			+ "s.sha1 = :sha1 WHERE s.serialNum = :serialNum ")
	public int updateDraftContract(@Param("serialNum")String serialNum,@Param("title")String title,
			@Param("keyword")String keyword,@Param("pname")String pname,@Param("price") BigDecimal price,
			@Param("deadline") Date deadline,@Param("otheruids") String otheruids,@Param("status") byte status,
			@Param("updateTime") Date updateTime,@Param("sha1") String sha1,@Param("signPlaintext") String signPlaintext);
	
	//修改手机号码
	@Modifying
	@Query(value="update ContractEntity set videoFlag = :videoFlag where appId = :appId and orderId = :orderId ")
	public int updateConttractVideoFlag(@Param("videoFlag")String videoFlag, @Param("appId")String appId, @Param("orderId")String orderId);
	
}
