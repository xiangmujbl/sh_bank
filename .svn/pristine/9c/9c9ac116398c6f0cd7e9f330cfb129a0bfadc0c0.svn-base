package com.mmec.centerService.feeModule.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mmec.centerService.feeModule.dao.InvoiceInfoDao;
import com.mmec.centerService.feeModule.dao.OrderRecordDao;
import com.mmec.centerService.feeModule.entity.OrderRecordEntity;
import com.mmec.centerService.feeModule.service.OrderRecordService;
import com.mmec.centerService.userModule.dao.UkeyInfoDao;
import com.mmec.centerService.userModule.entity.InvoiceInfoEntity;
import com.mmec.centerService.userModule.entity.UkeyInfoEntity;
import com.mmec.util.OrderTradingShield;

@Service("orderRecordService")
public class OrderRecordServiceImpl implements OrderRecordService{
	
	private static Logger log=Logger.getLogger(OrderRecordServiceImpl.class);
	
	@Autowired
	private OrderRecordDao orderRecordDao;
	
	@Autowired
	private UkeyInfoDao ukeyInfoDao;
	
	@Autowired
	private	InvoiceInfoDao invoiceInfoDao;
	
	/**
	 * 保存订单信息
	 */
	public String saveOrderRecord(OrderRecordEntity o){
		orderRecordDao.save(o);
		return null;
	}
	
	/**
	 * 更新订单状态
	 */
	public String updateOrderRecord(OrderRecordEntity o){
		orderRecordDao.updateOrderRecordByOrderId(o.getOrderStatus(),o.getChangeTime(),
				o.getPayplamOrderId(),o.getOrderId());
		return null;
	}
	
	/**
	 * 根据用户ID查询订单信息
	 */
	public List<OrderRecordEntity> queryOrderByUserId(int userid){
		return orderRecordDao.queryReocrdByUserId(userid);
	}
	
	/**
	 * 根据用户ID分页查询订单信息
	 */
	public List<OrderRecordEntity> queryOrderPageByUserId(int userid,Pageable page){
		return orderRecordDao.queryReocrdByUserId(userid, page);
	}
	
	
	/**
	 * 返回订单数目
	 */
	public Long countOrderByUserId(int userid){
		return orderRecordDao.countRecordByUserId(userid);
	}
	
	/**
	 * 根据订单ID查询订单信息
	 */
	public OrderRecordEntity queryOrderByOrderId(String orderId){
		return orderRecordDao.queryOrderByOrderId(orderId);
	}
	
	/**
	 * 查询订单信息带paycode带分页
	 */
	public List<OrderRecordEntity> queryOrderByPayCode(int userid,String paycode,Pageable page){
		return orderRecordDao.queryReocrd(userid, paycode, page);
		
	}
	
	/**
	 * 查询订单信息带paycode的count数目
	 */
	public Long queryOrderNumByPayCode(int userid,String paycode){
		return orderRecordDao.countRecordByUserIdAndOrderType(userid, paycode);
	}
	
	/**
	 * 查看带买卖盾的orderid信息
	 */
	public  OrderTradingShield queryTradingShieldOrder(String orderId) {
		OrderTradingShield  ot=new OrderTradingShield();
		OrderRecordEntity or=orderRecordDao.queryOrderByOrderId(orderId);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(null!=or){
			ot.setChangeTime(sdf.format(or.getChangeTime()));
			ot.setTradeType(String.valueOf(or.getTradeType()));
			ot.setCommodity(or.getCommodity());
			ot.setCreateTime(sdf.format(or.getCreateTime()));
			ot.setTime(String.valueOf(or.getTime()));
			ot.setInvoiceStatus(String.valueOf(or.getInvoiceStatus()));
			ot.setOperateId(String.valueOf(or.getOperateId()));
			ot.setOrderId(or.getOrderId());
			ot.setOrderStatus(String.valueOf(or.getOrderStatus()));
			ot.setOrderType(or.getOrderType());
			ot.setPackageId(String.valueOf(or.getPackageId()));
			ot.setPayMethod(String.valueOf(or.getPayMethod()));
			ot.setPayplamOrderId(or.getPayplamOrderId());
			ot.setPayPlatformId(String.valueOf(or.getPayPlatformId()));
			ot.setPayWay(or.getPayWay());
			ot.setPrice(String.valueOf(or.getPrice()));
			ot.setRefusalReason(or.getRefusalReason());
			ot.setRemark(or.getRemark());
			ot.setReseve1(or.getReseve1());
			ot.setUkeyId(String.valueOf(or.getUkeyId()));
			ot.setVerified(String.valueOf(or.getVerified()));
			ot.setUserId(String.valueOf(or.getUserId()));
			ot.setId(String.valueOf(or.getId()));
			if(or.getInvoiceId()!=0){
				InvoiceInfoEntity i=invoiceInfoDao.findOne(or.getInvoiceId()); 
				if(null!=i){
					ot.setTicketMailAddress(i.getMailAddress());
					ot.setTicketMailMethod(i.getMailMethod());
					ot.setTicketTitle(i.getTitle());
					ot.setTicketType(String.valueOf(i.getType()));
				}else{
					ot.setTicketMailAddress("");
					ot.setTicketMailMethod("");
					ot.setTicketTitle("");
					ot.setTicketType("");
				}
			}
			if(or.getOrderType().equals("tradingshield")){
			if(0!=or.getUkeyId()){
				//取买卖盾信息
				UkeyInfoEntity ukey=ukeyInfoDao.findOne(or.getUkeyId());
					if(null!=ukey){
						ot.setCertExpiringDate(sdf.format(ukey.getExpiringDate()));
						ot.setCertNum(ukey.getCertNum());
						ot.setCertStartingDate(sdf.format(ukey.getStartingDate()));
						ot.setActivateStatus(String.valueOf(ukey.getStatus()));
						ot.setCertBindDate(sdf.format(ukey.getBindTime()));
						ot.setCertId(String.valueOf(ukey.getId()));
						ot.setCertSubject(ukey.getSubject());
					}else{
						ot.setCertExpiringDate("");
						ot.setCertNum("");
						ot.setCertStartingDate("");
						ot.setActivateStatus("");
						ot.setCertBindDate("");
						ot.setCertId("");
						ot.setCertSubject("");
					}
				}
			}
		}
		return ot;
	}
	
	/**
	 * 查询买卖盾的订单状态
	 */
	public List<OrderTradingShield> queryOrderTradingShield(String paycode,int userid,Pageable page){
		List<OrderRecordEntity> li=orderRecordDao.queryReocrd(userid, paycode, page);
		List<OrderTradingShield> list=new ArrayList<OrderTradingShield>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(null!=li&&li.size()>0){
			for(int i=0;i<li.size();i++){
				OrderTradingShield ot=new OrderTradingShield();
				OrderRecordEntity or=li.get(i);
				ot.setTime(String.valueOf(or.getTime()));
				ot.setChangeTime(sdf.format(or.getChangeTime()));
				ot.setTradeType(String.valueOf(or.getTradeType()));
				ot.setCommodity(or.getCommodity());
				ot.setCreateTime(sdf.format(or.getCreateTime()));
				ot.setInvoiceStatus(String.valueOf(or.getInvoiceStatus()));
				ot.setOperateId(String.valueOf(or.getOperateId()));
				ot.setOrderId(or.getOrderId());
				ot.setOrderStatus(String.valueOf(or.getOrderStatus()));
				ot.setOrderType(or.getOrderType());
				ot.setPackageId(String.valueOf(or.getPackageId()));
				ot.setPayMethod(String.valueOf(or.getPayMethod()));
				ot.setPayplamOrderId(or.getPayplamOrderId());
				ot.setPayPlatformId(String.valueOf(or.getPayPlatformId()));
				ot.setPayWay(or.getPayWay());
				ot.setPrice(String.valueOf(or.getPrice()));
				ot.setRefusalReason(or.getRefusalReason());
				ot.setRemark(or.getRemark());
				ot.setReseve1(or.getReseve1());
				ot.setUkeyId(String.valueOf(or.getUkeyId()));
				ot.setVerified(String.valueOf(or.getVerified()));
				ot.setUserId(String.valueOf(or.getUserId()));
				ot.setId(String.valueOf(or.getId()));
				if(or.getInvoiceId()!=0){
				InvoiceInfoEntity in=invoiceInfoDao.findOne(or.getInvoiceId());
					if(null!=in){
						ot.setTicketMailAddress(in.getMailAddress());
						ot.setTicketMailMethod(in.getMailMethod());
						ot.setTicketTitle(in.getTitle());
						ot.setTicketType(String.valueOf(in.getType()));
					}else{
						ot.setTicketMailAddress("");
						ot.setTicketMailMethod("");
						ot.setTicketTitle("");
						ot.setTicketType("");
					}
				}else{
					ot.setTicketMailAddress("");
					ot.setTicketMailMethod("");
					ot.setTicketTitle("");
					ot.setTicketType("");
				}
				if(or.getOrderType().equals("tradingshield")){
					if(0!=or.getUkeyId()){
						//取买卖盾信息
						UkeyInfoEntity ukey=ukeyInfoDao.findOne(or.getUkeyId());
							if(null!=ukey){
								ot.setCertExpiringDate(sdf.format(ukey.getExpiringDate()));
								ot.setCertNum(ukey.getCertNum());
								ot.setCertStartingDate(sdf.format(ukey.getStartingDate()));
								ot.setActivateStatus(String.valueOf(ukey.getStatus()));
								ot.setCertBindDate(sdf.format(ukey.getBindTime()));
								ot.setCertId(String.valueOf(ukey.getId()));
								ot.setCertSubject(ukey.getSubject());
							}else{
								ot.setCertExpiringDate("");
								ot.setCertNum("");
								ot.setCertStartingDate("");
								ot.setActivateStatus("");
								ot.setCertBindDate("");
								ot.setCertId("");
								ot.setCertSubject("");
							}
						}else{
							ot.setCertExpiringDate("");
							ot.setCertNum("");
							ot.setCertStartingDate("");
							ot.setActivateStatus("");
							ot.setCertBindDate("");
							ot.setCertId("");
							ot.setCertSubject("");
						}
					}
				list.add(ot);
			}
			return list;
		}else{
			return null;
		}
		
	}
}
