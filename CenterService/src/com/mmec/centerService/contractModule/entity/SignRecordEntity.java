package com.mmec.centerService.contractModule.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.mmec.centerService.userModule.entity.IdentityEntity;
import com.mmec.centerService.contractModule.entity.SmsInfoEntity;
import com.mmec.centerService.userModule.entity.UkeyInfoEntity;

import java.util.Date;


/**
 * The persistent class for the c_sign_record database table.
 * 
 */
@Entity
@Table(name="c_sign_record")
public class SignRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="current_sha1")
	private String currentSha1;

	private String mark;

	@Column(name="orignal_filename")
	private String orignalFilename;

	@Column(name="prev_sha1")
	private String prevSha1;

	@Column(name="sign_mode")
	private byte signMode;

	@Column(name="sign_status")
	private byte signStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="sign_time")
	private Date signTime;

	private String signdata;

	@Column(name="sign_timestamp")
	private long signTimestamp;

	//bi-directional many-to-one association to SmsInfoEntity
	@OneToOne
	@JoinColumn(name="sms_serial_num")
	private SmsInfoEntity CSmsInfo;

	//bi-directional many-to-one association to ContractEntity
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="contract_id")
	private ContractEntity CContract;

	//bi-directional many-to-one association to IdentityEntity
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="singer_id")
	private IdentityEntity CIdentity;

	//bi-directional many-to-one association to UkeyInfoEntity
	@ManyToOne
	@JoinColumn(name="cert_id")
	private UkeyInfoEntity CUkeyInfo;
	
	@Column(name="sign_information")
	private String signInformation;
	
	@Column(name="sha1_digest")
	private String sha1Digest;
	
	@Column(name="pass_encoded")
	private String passEncoded;
	
	@Column(name="password")
	private String password;
	
	@Column(name="alias")
	private String alias;
	
	@Column(name="certificate_path")
	private String certificatePath;
	
	@Column(name="author_id")
	private int authorId;
	
	public String getPassEncoded() {
		return passEncoded;
	}

	public void setPassEncoded(String passEncoded) {
		this.passEncoded = passEncoded;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getCertificatePath() {
		return certificatePath;
	}

	public void setCertificatePath(String certificatePath) {
		this.certificatePath = certificatePath;
	}

	public String getSha1Digest() {
		return sha1Digest;
	}

	public void setSha1Digest(String sha1Digest) {
		this.sha1Digest = sha1Digest;
	}

	public String getSignInformation() {
		return signInformation;
	}

	public void setSignInformation(String signInformation) {
		this.signInformation = signInformation;
	}

	@Column(name="sign_type")
	private byte signType;
	
	public byte getSignType() {
		return signType;
	}

	public void setSignType(byte signType) {
		this.signType = signType;
	}

	public SignRecordEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCurrentSha1() {
		return this.currentSha1;
	}

	public void setCurrentSha1(String currentSha1) {
		this.currentSha1 = currentSha1;
	}

	public String getMark() {
		return this.mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getOrignalFilename() {
		return this.orignalFilename;
	}

	public void setOrignalFilename(String orignalFilename) {
		this.orignalFilename = orignalFilename;
	}

	public String getPrevSha1() {
		return this.prevSha1;
	}

	public void setPrevSha1(String prevSha1) {
		this.prevSha1 = prevSha1;
	}

	public byte getSignMode() {
		return this.signMode;
	}

	public void setSignMode(byte signMode) {
		this.signMode = signMode;
	}

	public byte getSignStatus() {
		return this.signStatus;
	}

	public void setSignStatus(byte signStatus) {
		this.signStatus = signStatus;
	}

	public Date getSignTime() {
		return this.signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public String getSigndata() {
		return this.signdata;
	}

	public void setSigndata(String signdata) {
		this.signdata = signdata;
	}

	public SmsInfoEntity getCSmsInfo() {
		return this.CSmsInfo;
	}

	public void setCSmsInfo(SmsInfoEntity CSmsInfo) {
		this.CSmsInfo = CSmsInfo;
	}

	public ContractEntity getCContract() {
		return this.CContract;
	}

	public void setCContract(ContractEntity CContract) {
		this.CContract = CContract;
	}

	public IdentityEntity getCIdentity() {
		return this.CIdentity;
	}

	public void setCIdentity(IdentityEntity CIdentity) {
		this.CIdentity = CIdentity;
	}

	public UkeyInfoEntity getCUkeyInfo() {
		return this.CUkeyInfo;
	}

	public void setCUkeyInfo(UkeyInfoEntity CUkeyInfo) {
		this.CUkeyInfo = CUkeyInfo;
	}
	
	public long getSignTimestamp() {
		return signTimestamp;
	}

	public void setSignTimestamp(long signTimestamp) {
		this.signTimestamp = signTimestamp;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}
	
}