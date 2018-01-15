package com.mmec.centerService.userModule.dao;
/*
 * 证书操作相关
 */
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.userModule.entity.PlatformEntity;
import com.mmec.centerService.userModule.entity.UkeyInfoEntity;

public interface UkeyInfoDao extends JpaRepository<UkeyInfoEntity,Integer> {
	//根据证书编号获取证书信息
	public UkeyInfoEntity findByCertNum(String certNum);
	//根据证书内容获取证书信息
	public UkeyInfoEntity findByCertContent(String certContent);
	//根据证书编号和证书内容获取证书信息
	public UkeyInfoEntity findByCertNumAndCertContentAndStatus(String certNum,String certContent,byte status);
	//根据证书编号和证书内容获取证书信息
	public UkeyInfoEntity findById(int certId);
	
	//修改证书使用状态  0 停用  1 使用
	@Modifying
	@Query(value="update UkeyInfoEntity u set u.status = 0 where u.id = :certId")
	public int updateIdentityState(@Param("certId")int certId);
	
	//根据appid和平台用户名称 查询用户是否已经存在
	@Query(value="select u from UkeyInfoEntity u inner join u.CIdentity where u.certNum = :certNum and u.certContent = :certContent  and u.status=1  and  u.CIdentity = :identity")
	public UkeyInfoEntity queryUkeyInfoByIdentityAndCert(@Param("certNum")String certNum,@Param("certContent")String certContent,@Param("identity")IdentityEntity identity);

	//根据appid和平台用户名称 查询用户是否已经存在
	@Query(value="select u from UkeyInfoEntity u inner join u.CIdentity where u.CIdentity = :identity")
	public List<UkeyInfoEntity> queryUkeyInfoByIdentity(@Param("identity")IdentityEntity identity);
	
	//根据appid和平台用户名称 查询用户是否已经存在
	@Query(value="select u from UkeyInfoEntity u inner join u.CIdentity where u.CIdentity = :identity and u.status = :status")
	public List<UkeyInfoEntity> queryUkeyInfoByIdentityAndStatus(@Param("identity")IdentityEntity identity,@Param("status")byte status);
}
