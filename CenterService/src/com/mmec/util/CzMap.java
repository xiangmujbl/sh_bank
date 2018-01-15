package com.mmec.util;

/**
 * 存证数据
 * @author Administrator
 *
 */
public class CzMap {
	
	public String id;
	
	public String title;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 电子证据提交方
	 */
	public String submiter;
	
	/**
	 * 存管时间//提交时间
	 */
	public String uploadtime;
	
	/**
	 * 提交的身份信息
	 */
	//企业名称-name  营业执照号-card  邮箱-info
	//个人-name  身份证号码-card  手机号-info
	//list<Map>转String
	//public String parter;
	
	
	/**
	 * 文件名称
	 */
	public String filename;
	
	/**
	 * 存证编号
	 */
	public String serial;
	
	
	/**
	 * 提交方信息
	 */
	//企业名称-name 工商营业执照号-card 法定代表人-legalname  身份证号-card   手机号-phone  类型-2
	//个人名称-name  身份证号-card 手机号-phone  类型-1
	//Map转String 先读类型再区分读企业还是个人
	public String submiterInfo;
	

	/**
	 * 截图地址
	 */
	//list<src_str> 转String
	public String filelist;
	
	public String fileListMap;
	/**
	 * 参与者
	 */
	public String parterInfo;
	
	/**
	 * 更新时间
	 */
	public String updatetime;
	
	/**
	 * 下载次数
	 */
	public String downloadtimes;
	
	public String signData;
	
	public String getSignData() {
		return signData;
	}

	public void setSignData(String signData) {
		this.signData = signData;
	}

	public String getDownloadtimes() {
		return downloadtimes;
	}

	public void setDownloadtimes(String downloadtimes) {
		this.downloadtimes = downloadtimes;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getParterInfo() {
		return parterInfo;
	}

	public void setParterInfo(String parterInfo) {
		this.parterInfo = parterInfo;
	}

	public String getSubmiter() {
		return submiter;
	}

	public void setSubmiter(String submiter) {
		this.submiter = submiter;
	}

	public String getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(String uploadtime) {
		this.uploadtime = uploadtime;
	}
//
//	public String getParter() {
//		return parter;
//	}
//
//	public void setParter(String parter) {
//		this.parter = parter;
//	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getSubmiterInfo() {
		return submiterInfo;
	}

	public void setSubmiterInfo(String submiterInfo) {
		this.submiterInfo = submiterInfo;
	}

	public String getFilelist() {
		return filelist;
	}

	public void setFilelist(String filelist) {
		this.filelist = filelist;
	}

	public String getFileListMap() {
		return fileListMap;
	}

	public void setFileListMap(String fileListMap) {
		this.fileListMap = fileListMap;
	}
	
}