package com.mmec.centerService.userModule.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.mmec.css.security.Coder;
import com.mmec.util.AES256Util;


/**
 * The persistent class for the c_identity database table.
 * 
 */
@Entity
@Table(name="c_identity")
public class IdentityEntity implements Serializable {
	private static final long serialVersionUID = 1L;
 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String account;

	@Column(name="binded_id")
	private int bindedId;

	private String email;

	@Column(name="is_admin")
	private byte isAdmin;

	@Column(name="is_authentic")
	private byte isAuthentic;

	
	@Column(name="moblie")
	private String mobile;

	@Column(name="parent_id")
	private int parentId;

	private String password;

	@Column(name="platform_user_name")
	private String platformUserName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="regist_time")
	private Date registTime;

	private String reseve1;

	@Column(name="business_admin")
	private String businessAdmin;
	
	private byte source;

	private byte status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="status_time")
	private Date statusTime;

	@Column(name="stop_info")
	private String stopInfo;

	@Column(name="is_mobile_verified")
	private byte isMobileVerified;
	
	@Column(name="is_email_verified")
	private byte isEmailVerified;
	
	private byte type;

	private String uuid;

	@Column(name="wx_openid")
	private String wxOpenid;

	//bi-directional many-to-one association to CompanyInfoEntity
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="company_id")
	private CompanyInfoEntity CCompanyInfo;

	//bi-directional many-to-one association to CustomInfoEntity
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="custom_id")
	private CustomInfoEntity CCustomInfo;

	//bi-directional many-to-one association to PlatformEntity
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="platform_id")
	private PlatformEntity CPlatform;

	//bi-directional many-to-one association to SigningRoomEntity
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="srm_id")
	private SigningRoomEntity CSigningRoom;

	public IdentityEntity() {
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getBindedId() {
		return this.bindedId;
	}

	public void setBindedId(int bindedId) {
		this.bindedId = bindedId;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte getIsAdmin() {
		return this.isAdmin;
	}

	public void setIsAdmin(byte isAdmin) {
		this.isAdmin = isAdmin;
	}

	public byte getIsAuthentic() {
		return this.isAuthentic;
	}

	public void setIsAuthentic(byte isAuthentic) {
		this.isAuthentic = isAuthentic;
	}

	public int getParentId() {
		return this.parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getPassword() {
		String aesPassword = password;
		try {
			byte[] data= AES256Util.getInstance().decrypt(Coder.decryptBASE64( this.password)); 
			aesPassword = new String(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(aesPassword).replace("\r\n","");
	}

	public void setPassword(String password) {
		String aesPassword = password;
		try {
			  byte[] data=AES256Util.getInstance().encrypt(password.getBytes()); 
			  aesPassword = Coder.encryptBASE64(data).replace("\r\n","");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.password = aesPassword;
	}

	public String getPlatformUserName() {
		return this.platformUserName;
	}

	public void setPlatformUserName(String platformUserName) {
		this.platformUserName = platformUserName;
	}

	public Date getRegistTime() {
		return this.registTime;
	}

	public void setRegistTime(Date registTime) {
		this.registTime = registTime;
	}

	public String getReseve1() {
		return this.reseve1;
	}

	public void setReseve1(String reseve1) {
		this.reseve1 = reseve1;
	}

	public byte getSource() {
		return this.source;
	}

	public void setSource(byte source) {
		this.source = source;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Date getStatusTime() {
		return this.statusTime;
	}

	public void setStatusTime(Date statusTime) {
		this.statusTime = statusTime;
	}

	public String getStopInfo() {
		return this.stopInfo;
	}

	public void setStopInfo(String stopInfo) {
		this.stopInfo = stopInfo;
	}

	public byte getType() {
		return this.type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getWxOpenid() {
		return this.wxOpenid;
	}

	public void setWxOpenid(String wxOpenid) {
		this.wxOpenid = wxOpenid;
	}

	public CompanyInfoEntity getCCompanyInfo() {
		return this.CCompanyInfo;
	}

	public void setCCompanyInfo(CompanyInfoEntity CCompanyInfo) {
		this.CCompanyInfo = CCompanyInfo;
	}

	public CustomInfoEntity getCCustomInfo() {
		return this.CCustomInfo;
	}

	public void setCCustomInfo(CustomInfoEntity CCustomInfo) {
		this.CCustomInfo = CCustomInfo;
	}

	public PlatformEntity getCPlatform() {
		return this.CPlatform;
	}

	public void setCPlatform(PlatformEntity CPlatform) {
		this.CPlatform = CPlatform;
	}

	public SigningRoomEntity getCSigningRoom() {
		return this.CSigningRoom;
	}

	public void setCSigningRoom(SigningRoomEntity CSigningRoom) {
		this.CSigningRoom = CSigningRoom;
	}

	public byte getIsMobileVerified() {
		return isMobileVerified;
	}

	public void setIsMobileVerified(byte isMobileVerified) {
		this.isMobileVerified = isMobileVerified;
	}

	public byte getIsEmailVerified() {
		return isEmailVerified;
	}

	public void setIsEmailVerified(byte isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	//判断数据类是否合法  与传入参数相关
	public String isBeanLegal()
	{
		if(null == account || "".equals(account))
		{
			return "IdentityEntity account is null";
		}
		if(null == password || "".equals(password))
		{
			return "IdentityEntity password is null";
		}
		if(null == platformUserName || "".equals(platformUserName))
		{
			return "IdentityEntity platformUserName is null";
		}
		//邮箱不做非空校验
		/*
		if(null == email || "".equals(email))
		{
			return "IdentityEntity email is null";
		}
		*/
		if(null == mobile || "".equals(mobile))
		{
			return "IdentityEntity mobile is null";
		}
		
		return "";
	}

	public String getBusinessAdmin() {
		return businessAdmin;
	}

	public void setBusinessAdmin(String businessAdmin) {
		this.businessAdmin = businessAdmin;
	}
}