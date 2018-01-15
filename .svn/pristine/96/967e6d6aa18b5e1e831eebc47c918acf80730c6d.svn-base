package com.mmec.centerService.feeModule.dao;

import java.util.Date;
import java.util.List;

import org.icepdf.core.pobjects.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.mmec.centerService.feeModule.entity.OrderRecordEntity;

public interface OrderRecordDao extends JpaRepository<OrderRecordEntity,Integer> {

	/**
	 * 更改用户状态
	 * @param orderStatus
	 * @param id
	 * @return
	 */
	@Modifying
	@Query(value="update OrderRecordEntity set orderStatus =?,changeTime=? where id =?")
	public int UpdateOrderStatusById(int orderStatus,Date changeTime,int id);
	
	/**
	 * 根据用户ID查询订单记录
	 */
	@Query(value="select  a  from  OrderRecordEntity  a  where  a.userId=? order by a.id desc")
	public List<OrderRecordEntity> queryReocrdByUserId(int userid);
	
	/**
	 * 根据用户ID查询订单记录号
	 */
	@Query(value="select  count(a)  from  OrderRecordEntity  a  where  a.userId=?")
	public Long countRecordByUserId(int userid);
	
	/**
	 * 根据用户ID查询订单记录条数带orderType
	 */
	@Query(value="select  count(a)  from  OrderRecordEntity  a  where  a.userId=? and a.orderType=?")
	public Long countRecordByUserIdAndOrderType(int userid,String orderType);
	
	/**
	 * 根据用户ID分页查询订单
	 */
	@Query(value="select a from OrderRecordEntity  a  where  a.userId=? order by a.id desc")
	public List<OrderRecordEntity> queryReocrdByUserId(int userid,Pageable page);
	
	/**
	 * 根据用户ID分页查询订单带orderType
	 */
	@Query(value="select a from OrderRecordEntity  a  where  a.userId=? and a.orderType=? order by a.id desc")
	public List<OrderRecordEntity> queryReocrd(int userid,String orderType,Pageable page);
	
	/**
	 * 根据订单ID号查询记录
	 */
	@Query(value="select a from OrderRecordEntity  a  where a.orderId=? order by a.id desc")
	public OrderRecordEntity queryOrderByOrderId(String orderId);
	
	/**
	 * 更新订单信息
	 */
	@Modifying
	@Query(value="update OrderRecordEntity set orderStatus =?,changeTime=?,payplamOrderId=? where orderId =?")
	public int updateOrderRecordByOrderId(int orderStatus,Date changeTime,String payplamOrderId,String orderId);
	
	/**
	 * 查询买卖盾信息
	 */
	
}
