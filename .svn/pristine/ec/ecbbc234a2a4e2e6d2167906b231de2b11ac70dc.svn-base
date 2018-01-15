package com.mmec.centerService.feeModule.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.mmec.centerService.contractModule.entity.ContractEntity;
import com.mmec.centerService.userModule.entity.IdentityEntity;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the c_contract_deduct_record database table.
 * 
 */
@Entity
@Table(name="c_contract_deduct_record")
public class ContractDeductRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="bill_num")
	private String billNum;
	
	@Column(name="consume_type")
	private byte consumeType;

	@Column(name="deduct_sum")
	private BigDecimal deductSum;

	@Column(name="deduct_times")
	private int deductTimes;

	@Column(name="pay_id")
	private String payId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_time")
	private Date updateTime;

	@Column(name="type_code")
	private String typecode;
	
	@Column(name="bq_text")
	private String bqtext;
	
	public String getBqtext() {
		return bqtext;
	}

	public void setBqtext(String bqtext) {
		this.bqtext = bqtext;
	}

	public String getTypecode() {
		return typecode;
	}

	public void setTypecode(String typecode) {
		this.typecode = typecode;
	}

	@Column(name="user_id")
	private int userid;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public ContractDeductRecordEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBillNum() {
		return this.billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public byte getConsumeType() {
		return this.consumeType;
	}

	public void setConsumeType(byte consumeType) {
		this.consumeType = consumeType;
	}

	public BigDecimal getDeductSum() {
		return this.deductSum;
	}

	public void setDeductSum(BigDecimal deductSum) {
		this.deductSum = deductSum;
	}

	public int getDeductTimes() {
		return this.deductTimes;
	}

	public void setDeductTimes(int deductTimes) {
		this.deductTimes = deductTimes;
	}


	public String getPayId() {
		return this.payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


}