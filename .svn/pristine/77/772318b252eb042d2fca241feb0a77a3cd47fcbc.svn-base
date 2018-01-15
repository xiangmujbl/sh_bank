package com.mmec.centerService.depositoryModule.entity;

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

@Entity
@Table(name="c_evidence_bind_attachment")
public class EvidenceBindAttachmentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	/**
	 * 附件路径
	 */
	@Column(name="attachment_path")
	private String attachmentpath;
	
	/**
	 * 转图片路径
	 */
	@Column(name="toImgpath")
	private String toImgpath;
	
	public String getToImgpath() {
		return toImgpath;
	}

	public void setToImgpath(String toImgpath) {
		this.toImgpath = toImgpath;
	}

	public String getAttachmentpath() {
		return attachmentpath;
	}

	public void setAttachmentpath(String attachmentpath) {
		this.attachmentpath = attachmentpath;
	}
	
	/**
	 * c_evidence表ID
	 */
	@Column(name="evidence_id")
	private int evidence_id;
	
	/**
	 * 假删除标志
	 */
	@Column(name="del_flag")
	private String delflag;
	
	/**
	 * 附件更新时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updatetime")
	private Date updatetime;
	
	/**
	 * 备注
	 */
	@Column(name="remark")
	private String remark;
	
	/**
	 * 时间戳
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="uploadtime")
	private Date uploadtime;
	
	/**
	 * 下载次数
	 */
	@Column(name="downloadtimes")
	private int downloadtimes;
	
	/**
	 * 类型
	 */
	@Column(name="type")
	private String type;
	
	/**
	 * 文件hash值
	 * @return
	 */
	@Column(name="file_hash")
	private String filehash;
	
	/**
	 * 签名值
	 * @return
	 */
	@Column(name="signature")
	private String signature;
	
	/**
	 * 证书序列号
	 */
	@Column(name="certserial")
	private String certserial;

	
	public String getCertserial() {
		return certserial;
	}

	public void setCertserial(String certserial) {
		this.certserial = certserial;
	}

	public String getFilehash() {
		return filehash;
	}

	public void setFilehash(String filehash) {
		this.filehash = filehash;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getCert() {
		return cert;
	}

	public void setCert(String cert) {
		this.cert = cert;
	}

	public String getSigntimestamp() {
		return signtimestamp;
	}

	public void setSigntimestamp(String signtimestamp) {
		this.signtimestamp = signtimestamp;
	}

	/**
	 * 证书值
	 * @return
	 */
	@Column(name="cert")
	private String cert;
	
	/**
	 * 证书值
	 * @return
	 */
	@Column(name="signtimestamp")
	private String signtimestamp;

	
	public int getDownloadtimes() {
		return downloadtimes;
	}

	public void setDownloadtimes(int downloadtimes) {
		this.downloadtimes = downloadtimes;
	}
	


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public int getEvidence_id() {
		return evidence_id;
	}

	public void setEvidence_id(int evidence_id) {
		this.evidence_id = evidence_id;
	}


	public String getDelflag() {
		return delflag;
	}

	public void setDelflag(String delflag) {
		this.delflag = delflag;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(Date uploadtime) {
		this.uploadtime = uploadtime;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}