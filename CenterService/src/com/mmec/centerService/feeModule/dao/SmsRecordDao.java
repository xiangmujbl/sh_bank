package com.mmec.centerService.feeModule.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Map;

import com.mmec.centerService.feeModule.entity.SmsRecordEntity;

public interface SmsRecordDao extends JpaRepository<SmsRecordEntity,Integer> {
	/**
	 * 统计云签/对接的短信发送数目
	 */
	@Query(value="select count(c.id) from SmsRecordEntity c where c.optfrom = ? ")
	public  Long  querySmsNumByOptform(String optfrom);
}
