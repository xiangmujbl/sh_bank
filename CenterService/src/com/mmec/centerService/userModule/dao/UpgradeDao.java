package com.mmec.centerService.userModule.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import com.mmec.centerService.userModule.entity.UpgradeEntity;

public interface UpgradeDao extends PagingAndSortingRepository<UpgradeEntity,Integer>{
	@Query(value="SELECT i FROM UpgradeEntity i WHERE i.newAppId = :appId ")
	public UpgradeEntity findUpgrade(@Param("appId") String appId);
}
