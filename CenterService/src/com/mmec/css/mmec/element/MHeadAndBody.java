package com.mmec.css.mmec.element;

public class MHeadAndBody {
	/* 头文件内�?*/
	private String head;
	/* 文件正文 */
	private String cont;
	
	/**
	 * 合同标题
	 */
	private String title;
	
	public String getCont() {
		return cont;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setCont(String cont) {
		this.cont = cont;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
}
