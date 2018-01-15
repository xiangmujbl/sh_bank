package com.mmec.business.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.mmec.business.bean.LogoBean;
import com.mmec.business.bean.MobileVerifyBean;

public interface LogoRepository extends PagingAndSortingRepository<LogoBean, Integer>, JpaSpecificationExecutor<LogoBean>  {
	@Query(" SELECT c FROM LogoBean c WHERE c.appId = ? ORDER BY Id DESC")
	public List<LogoBean> queryLogoPath(String appId);
}
