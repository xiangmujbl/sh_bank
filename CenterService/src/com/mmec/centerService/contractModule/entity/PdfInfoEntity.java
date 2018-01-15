package com.mmec.centerService.contractModule.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="c_pdf_info")
public class PdfInfoEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="contract_id")
	private int contractId;

	@Column(name="serial_num")
	private String serialNum;
	
	@Column(name="plateform_id")
	private int plateformId;

	@Column(name="app_id")
	private String appId;
	
	@Column(name="ucid")
	private String ucid;
	
	@Column(name="user_id")
	private int userId;
	
	@Column(name="number")
	private String number;

	@Column(name="special_character")
	private String specialCharacter;

	@Column(name="orderid")
	private String orderId;
	
	@Column(name="sign_ui_type")
	private int signUiType;
	
	/**
	 * 被授权人的id
	 */
	@Column(name="author_id")
	private int authorId;
	
	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public int getSignUiType() {
		return signUiType;
	}

	public void setSignUiType(int signUiType) {
		this.signUiType = signUiType;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public int getPlateformId() {
		return plateformId;
	}

	public void setPlateformId(int plateformId) {
		this.plateformId = plateformId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getUcid() {
		return ucid;
	}

	public void setUcid(String ucid) {
		this.ucid = ucid;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getSpecialCharacter() {
		return specialCharacter;
	}

	public void setSpecialCharacter(String specialCharacter) {
		this.specialCharacter = specialCharacter;
	}
}
