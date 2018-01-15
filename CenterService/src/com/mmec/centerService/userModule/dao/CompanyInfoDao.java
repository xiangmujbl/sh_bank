package com.mmec.centerService.userModule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmec.centerService.userModule.entity.CompanyInfoEntity;

public interface CompanyInfoDao extends JpaRepository<CompanyInfoEntity,Integer> {
	//根据营业执照号 查询公司
	public List<CompanyInfoEntity> findByBusinessLicenseNo(String businessLicenseNo);	
	//根据主键ID 查询公司
	public CompanyInfoEntity findById(int id);
	

	//根据公司名称 查询公司
	public List<CompanyInfoEntity> findByCompanyName(String companyName);	
	
	//修改企业名称
	@Modifying
	@Query(value="update CompanyInfoEntity set companyName = :companyName where id = :id")
	public int updateCompanyInfoEntityCompanyName(@Param("companyName")String companyName, @Param("id")int id);
	
	//修改工商执照号
	@Modifying
	@Query(value="update CompanyInfoEntity set businessLicenseNo = :businessLicenseNo where id = :id")
	public int updateCompanyInfoEntityBusinessLicenNo(@Param("businessLicenseNo")String businessLicenseNo, @Param("id")int id);
}
