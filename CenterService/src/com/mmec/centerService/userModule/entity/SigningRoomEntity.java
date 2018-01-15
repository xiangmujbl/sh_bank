package com.mmec.centerService.userModule.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the c_signing_room database table.
 * 
 */
@Entity
@Table(name="c_signing_room")
public class SigningRoomEntity implements Serializable {
	private static final long serialVersionUID = 1L;
 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="company_name")
	private String companyName;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateline;

	private String email;

	private String linkname;

	private String linktel;

	private int platform;

	private byte status;

	@Column(name="user_id")
	private int userId;

	public SigningRoomEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getDateline() {
		return this.dateline;
	}

	public void setDateline(Date dateline) {
		this.dateline = dateline;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLinkname() {
		return this.linkname;
	}

	public void setLinkname(String linkname) {
		this.linkname = linkname;
	}

	public String getLinktel() {
		return this.linktel;
	}

	public void setLinktel(String linktel) {
		this.linktel = linktel;
	}

	public int getPlatform() {
		return this.platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	//判断数据类是否合法  与传入参数相关
	public String isBeanLegal()
	{
		if(null == linkname || "".equals(linkname))
		{
			return "SigningRoomEntity businessLicenseNo is null";
		}
		if(null == linktel || "".equals(linktel))
		{
			return "SigningRoomEntity linktel is null";
		}
		
		return "";
	}
}