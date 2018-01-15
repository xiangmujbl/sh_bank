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


/**
 * The persistent class for the c_platform database table.
 * 
 */
@Entity
@Table(name="c_platform")
public class PlatformEntity implements Serializable {
	private static final long serialVersionUID = 1L;
 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String address;

	@Column(name="app_id")
	private String appId;

	@Column(name="app_secret_key")
	private String appSecretKey;

	@Column(name="business_license_no")
	private String businessLicenseNo;

	@Column(name="company_name")
	private String companyName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_time")
	private Date createTime;

	private String email;

	@Column(name="link_name")
	private String linkName;

	@Column(name="link_tel")
	private String linkTel;

	private String program;

	private byte status;

	private byte type;

    //平台下载合同FTP账号
	@Column(name="ftp_name")
	private String ftpName;
    //平台下载合同FTP密码
	@Column(name="ftp_pwd")
	private String ftpPwd;
    //平台下载合同FTP路径
	@Column(name="ftp_path")
	private String ftpPath;
	
	//bi-directional many-to-one association to AttachmentEntity
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="business_image_id")
	private AttachmentEntity CAttachment;

	//是否启用短信服务  0 不启用 1 启用
	@Column(name="is_sms_use")
	private int isSmsUse;
	//是否pdf方式签署  0 非pdf签署 ,1 pdf签署
	@Column(name="is_pdfsign")
	private int isPdfSign;
    //平台下载合同FTP密码
	@Column(name="default_role_id")
	private String defaultRoleId;
    //平台下载合同FTP路径
	@Column(name="admin_role_id")
	private String adminRoleId;
    //是否创建云签账号
	@Column(name="is_ys_creates")
	private String isYunsignCreate ;
	
	//返回类型  空 无 1 单通道 2 双通道  。 双通道才会配置证书
	@Column(name="call_type")
	private String callType;
	
	////////8.07//////////
	//0 不校验 1 校验
	@Column(name="is_check_platform")
	private String isCheckPlatform;
	
	public String getIsCheckPlatform() {
		return isCheckPlatform;
	}

	public void setIsCheckPlatform(String isCheckPlatform) {
		this.isCheckPlatform = isCheckPlatform;
	}
	/////////8.07//////////
	
	
	public String getIsYunsignCreate() {
		return isYunsignCreate;
	}

	public void setIsYunsignCreate(String isYunsignCreate) {
		this.isYunsignCreate = isYunsignCreate;
	}

	public int getIsPdfSign() {
		return isPdfSign;
	}

	public void setIsPdfSign(int isPdfSign) {
		this.isPdfSign = isPdfSign;
	}

	public int getIsSmsUse()
	{
		return isSmsUse;
	}

	public void setIsSmsUse(int isSmsUse)
	{
		this.isSmsUse = isSmsUse;
	}

	public PlatformEntity() {
	}

	public String getFtpName() {
		return ftpName;
	}

	public void setFtpName(String ftpName) {
		this.ftpName = ftpName;
	}

	public String getFtpPwd() {
		return ftpPwd;
	}

	public void setFtpPwd(String ftpPwd) {
		this.ftpPwd = ftpPwd;
	}

	public String getFtpPath() {
		return ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
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

	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecretKey() {
		return this.appSecretKey;
	}

	public void setAppSecretKey(String appSecretKey) {
		this.appSecretKey = appSecretKey;
	}

	public String getBusinessLicenseNo() {
		return this.businessLicenseNo;
	}

	public void setBusinessLicenseNo(String businessLicenseNo) {
		this.businessLicenseNo = businessLicenseNo;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLinkName() {
		return this.linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkTel() {
		return this.linkTel;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	public String getProgram() {
		return this.program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public byte getType() {
		return this.type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public AttachmentEntity getCAttachment() {
		return this.CAttachment;
	}

	public void setCAttachment(AttachmentEntity CAttachment) {
		this.CAttachment = CAttachment;
	}

	//判断数据类是否合法  与传入参数相关
	public String isBeanLegal()
	{
		if(null == appId || "".equals(appId))
		{
			return "PlatformEntity appId is null";
		}
		if(null == appSecretKey || "".equals(appSecretKey))
		{
			return "PlatformEntity appSecretKey is null";
		}
		if(null == businessLicenseNo || "".equals(businessLicenseNo))
		{
			return "PlatformEntity businessLicenseNo is null";
		}
		if(null == ftpName || "".equals(ftpName))
		{
			return "PlatformEntity ftpName is null";
		}
		if(null == ftpPwd || "".equals(ftpPwd))
		{
			return "PlatformEntity ftpPwd is null";
		}
		if(null == ftpPath || "".equals(ftpPath))
		{
			return "PlatformEntity ftpPath is null";
		}
		return "";
	}

	public String getDefaultRoleId()
	{
		return defaultRoleId;
	}

	public void setDefaultRoleId(String defaultRoleId)
	{
		this.defaultRoleId = defaultRoleId;
	}

	public String getAdminRoleId()
	{
		return adminRoleId;
	}

	public void setAdminRoleId(String adminRoleId)
	{
		this.adminRoleId = adminRoleId;
	}

	public String getCallType()
	{
		return callType;
	}

	public void setCallType(String callType)
	{
		this.callType = callType;
	}
	
	
	
}