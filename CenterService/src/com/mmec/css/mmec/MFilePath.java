package com.mmec.css.mmec;

/**
 * 
 * 设置各个文件的路径，基本路径确认后生�? * @author liuy
 *
 */
public class MFilePath {
	/* 根据指定的根路径统一设置对应的文件路*/
	private  String basePath;
	/* 合同计算*/
	private String contractSHA1Path;
	/* 用户组签*/
	private String userGroupSignPath;
	/* 合同记录计算*/
	private String contractRecordSHA1Path;
	/* 服务组签*/
	private String serverSignPath;
	/* 上传路径位置 */
	private String loadFilePath;
	private String loadFilePathNoBase;
	
	public String getLoadFilePathNoBase() {
		return loadFilePathNoBase;
	}
	public void setLoadFilePathNoBase(String loadFilePathNoBase) {
		this.loadFilePathNoBase = loadFilePathNoBase;
	}
	public String getLoadFilePath() {
		return loadFilePath;
	}
	public void setLoadFilePath(String loadFilePath) {
		this.loadFilePath = loadFilePath;
	}
	public String getBasePath() {
		return basePath;
	}
	public void setBasePath(String basePath) {
		this.basePath = basePath;
		setContractRecordSHA1Path(basePath+"/ContractRecordSHA1.txt");
		setServerSignPath(basePath+"/ServerSign.sg");
		
		setContractSHA1Path(basePath+"/ContractRecord/ContractSHA1.txt");	
		setUserGroupSignPath(basePath+"/ContractRecord/UserGroupSign.sg");
		
		String loadFilePathp="/ContractRecord/Contract/";
		setLoadFilePath(basePath+loadFilePathp);
		setLoadFilePathNoBase(loadFilePathp);
	}
	public String getContractRecordSHA1Path() {
		return contractRecordSHA1Path;
	}
	public void setContractRecordSHA1Path(String contractRecordSHA1Path) {
		this.contractRecordSHA1Path = contractRecordSHA1Path;
	}
	public String getContractSHA1Path() {
		return contractSHA1Path;
	}
	public void setContractSHA1Path(String contractSHA1Path) {
		this.contractSHA1Path = contractSHA1Path;
	}
	public String getServerSignPath() {
		return serverSignPath;
	}
	public void setServerSignPath(String serverSignPath) {
		this.serverSignPath = serverSignPath;
	}
	public String getUserGroupSignPath() {
		return userGroupSignPath;
	}
	public void setUserGroupSignPath(String userGroupSignPath) {
		this.userGroupSignPath = userGroupSignPath;
	}

}
