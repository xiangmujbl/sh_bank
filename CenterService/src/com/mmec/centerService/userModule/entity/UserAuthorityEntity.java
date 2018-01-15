package com.mmec.centerService.userModule.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the c_auth database table.
 * 
 */
@Entity
@Table(name="c_user_authority")
public class UserAuthorityEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	/**
	 * 授权人编码
	 */
	@Column(name="user_id")
	private int userId;
	
	/**
	 * 被授权人编码
	 */
	@Column(name="author_id")
	private int authorId;

	/**
	 * 授权时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="auth_time")
	private Date authTime;
		
	/**
	 * 授权书编号
	 */
	@Column(name="auth_img_id")
	private int authImgId;

	/**
	 * 授权类型1 单次;2 多次
	 */
	@Column(name="auth_type")
	private int authType;

	/**
	 * 授权截止期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="auth_end_time")
	private Date authEndTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	private Date createTime;
	
	/**
	 * 合同编号(单次授权)
	 */
	@Column(name="serial_num")
	private String serialNum;

	@Column(name="platform_id")
	private int platformId;
	
	/**
	 * 授权合同主键
	 */
	@Column(name="auth_contract_id")
	private int authContractId;	
	
	public int getAuthContractId() {
		return authContractId;
	}

	public void setAuthContractId(int authContractId) {
		this.authContractId = authContractId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public Date getAuthTime() {
		return authTime;
	}

	public void setAuthTime(Date authTime) {
		this.authTime = authTime;
	}

	public int getAuthImgId() {
		return authImgId;
	}

	public void setAuthImgId(int authImgId) {
		this.authImgId = authImgId;
	}

	public int getAuthType() {
		return authType;
	}

	public void setAuthType(int authType) {
		this.authType = authType;
	}

	public Date getAuthEndTime() {
		return authEndTime;
	}

	public void setAuthEndTime(Date authEndTime) {
		this.authEndTime = authEndTime;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public int getPlatformId() {
		return platformId;
	}

	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}