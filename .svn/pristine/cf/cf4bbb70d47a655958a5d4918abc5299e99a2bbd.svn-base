package com.mmec.business.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.mmec.business.bean.SyncTaskBean;

/**
 * 同步定时任务
 */
public interface SyncTaskRepository
		extends PagingAndSortingRepository<SyncTaskBean, Integer>, JpaSpecificationExecutor<SyncTaskBean> {

	@Transactional
	@Modifying
	@Query("UPDATE SyncTaskBean c SET c.syncNum=0,c.status = 0,c.time=?,c.syncTime=? WHERE c.status =2 and c.platformId = ? and c.callbackName=?")
	public int updateSyncTaskStatus(String time, String syncTime, String platformId, String callbackName);

	@Transactional
	@Modifying
	@Query("UPDATE SyncTaskBean c SET c.status = 1 WHERE c.callbackName =? and c.orderId = ? and c.platformId = ?")
	public int updateSyncTaskStatus2(String callbackName, String orderId, String platformId);

	@Transactional
	@Modifying
	@Query("UPDATE SyncTaskBean c SET c.syncTime = ?,c.syncNum=? WHERE c.id =?")
	public int updateSyncTaskTime(String syncTime, int syncNum, int id);

	@Transactional
	@Modifying
	@Query("UPDATE SyncTaskBean c SET c.status = 2 WHERE c.time <= ? and c.status = 0")
	public int updateSyncTaskStop(String time);

	@Query("select c from SyncTaskBean c where c.status=0 and c.platformId=? and c.callbackName = ? and c.url=? and c.syncTime <=?")
	public List<SyncTaskBean> findSyncTaskByName(String appId, String callbackName, String url, String dateStr);

}
