package com.mmec.centerService.userModule.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the c_auth database table.
 * 
 */
@Entity
@Table(name="c_auth")
public class AuthEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="auth_desc")
	private String authDesc;

	@Column(name="auth_name")
	private String authName;

	@Column(name="auth_num")
	private String authNum;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_time")
	private Date updateTime;
	
	//auth_category
	@Column(name="category_code")
	private String authcategory;
	
	public String getAuthcategory() {
		return authcategory;
	}

	public void setAuthcategory(String authcategory) {
		this.authcategory = authcategory;
	}

	public AuthEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthDesc() {
		return this.authDesc;
	}

	public void setAuthDesc(String authDesc) {
		this.authDesc = authDesc;
	}

	public String getAuthName() {
		return this.authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public String getAuthNum() {
		return this.authNum;
	}

	public void setAuthNum(String authNum) {
		this.authNum = authNum;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}