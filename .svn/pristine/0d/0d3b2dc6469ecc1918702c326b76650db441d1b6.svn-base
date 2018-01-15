package com.mmec.centerService.userModule.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * The persistent class for the c_custom_info database table.
 * 
 */
@Entity
@Table(name="c_custom_info")
public class CustomInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String address;

	private String duty;

	@Column(name="identity_card")
	private String identityCard;
	
	//证件类型： 1 身份证 2港澳通行证 3 台湾通行证 4护照 5军官证 6 士兵证
	@Column(name="card_type")
	private String cardType;
	
	@Column(name="name")
	private String userName;

	private String nickname;

	@Column(name="phone_num")
	private String phoneNum;

	@Column(name="app_id")
	private String appId;
	
	private String reseve1;

	private String reseve2;

	//bi-directional many-to-one association to AttachmentEntity
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="proxy_idImg_A")
	private AttachmentEntity CAttachmentIdA;

	//bi-directional many-to-one association to AttachmentEntity
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="proxy_idImg_B")
	private AttachmentEntity CAttachmentIdB;

	public AttachmentEntity getCAttachmentIdA() {
		return CAttachmentIdA;
	}

	public void setCAttachmentIdA(AttachmentEntity cAttachmentIdA) {
		CAttachmentIdA = cAttachmentIdA;
	}

	public AttachmentEntity getCAttachmentIdB() {
		return CAttachmentIdB;
	}

	public void setCAttachmentIdB(AttachmentEntity cAttachmentIdB) {
		CAttachmentIdB = cAttachmentIdB;
	}
	//bi-directional many-to-one association to AttachmentEntity

	public CustomInfoEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDuty() {
		return this.duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getIdentityCard() {
		return this.identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhoneNum() {
		return this.phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getReseve1() {
		return this.reseve1;
	}

	public void setReseve1(String reseve1) {
		this.reseve1 = reseve1;
	}

	public String getReseve2() {
		return this.reseve2;
	}

	public void setReseve2(String reseve2) {
		this.reseve2 = reseve2;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	//判断数据类是否合法  与传入参数相关
	public String isBeanLegal()
	{
		if(null == userName || "".equals(userName))
		{
			return "CustomInfoEntity userName is null";
		}
		if(null == identityCard || "".equals(identityCard))
		{
			return "CustomInfoEntity identityCard is null";
		}
		return "";
	}

	public String getCardType()
	{
		return cardType;
	}

	public void setCardType(String cardType)
	{
		this.cardType = cardType;
	}

}