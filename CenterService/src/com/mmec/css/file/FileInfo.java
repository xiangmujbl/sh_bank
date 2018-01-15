package com.mmec.css.file;

import java.io.File;

public class FileInfo {
	private String fileName;
	private boolean filetype;
	private File f;
	private String filePath;
	
	/**
	 * 使用URLEncoder.encode转码后传�?	 * @return
	 */
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}
	public File getF() {
		return f;
	}
	public void setF(File f) {
		this.f = f;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public boolean isFiletype() {
		return filetype;
	}
	public void setFiletype(boolean filetype) {
		this.filetype = filetype;
	}
	
}
