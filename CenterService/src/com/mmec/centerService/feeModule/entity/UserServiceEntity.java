package com.mmec.centerService.feeModule.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the c_user_service database table.
 * 
 */
@Entity
@Table(name="c_user_service")
public class UserServiceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="charging_times")
	private int chargingTimes;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="expired_date")
	private Date expiredDate;

	@Column(name="pay_type")
	private byte payType;

	@Column(name="user_id")
	private int userId;

	@Column(name="pay_code")
	private String payCode;
	
	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}


	public UserServiceEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getChargingTimes() {
		return this.chargingTimes;
	}

	public void setChargingTimes(int chargingTimes) {
		this.chargingTimes = chargingTimes;
	}

	public Date getExpiredDate() {
		return this.expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public byte getPayType() {
		return this.payType;
	}

	public void setPayType(byte payType) {
		this.payType = payType;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}