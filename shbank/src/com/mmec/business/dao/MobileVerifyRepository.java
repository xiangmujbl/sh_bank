package com.mmec.business.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.mmec.business.bean.MobileVerifyBean;

/**
 * 短信验证码关联数据持久化接口
 * 
 * @author Administrator
 *
 */
public interface MobileVerifyRepository
		extends PagingAndSortingRepository<MobileVerifyBean, Integer>, JpaSpecificationExecutor<MobileVerifyBean> {

	@Query(" SELECT c FROM MobileVerifyBean c WHERE c.orderId = ? ORDER BY c.sendTime DESC")
	public List<MobileVerifyBean> getMobileVerify(String orderId);

	@Query("SELECT c FROM MobileVerifyBean c WHERE c.appId = ? and c.orderId = ? and c.userId = ? and type = ? ORDER BY c.sendTime DESC")
	public List<MobileVerifyBean> getValidateCode(String appId, String orderId, String ucid, String type);
	@Query("SELECT c FROM MobileVerifyBean c WHERE c.appId = ? and c.orderId = ?  and type = ? and (c.sendTime between ? and ?) ORDER BY c.sendTime DESC")
	public List<MobileVerifyBean> getValidateCode1(String appId, String orderId, String type,String startTime,String endTime);
}
