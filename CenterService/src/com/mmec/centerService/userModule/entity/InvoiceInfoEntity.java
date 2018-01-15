package com.mmec.centerService.userModule.entity;

import java.io.Serializable;
import javax.persistence.*;

import com.mmec.centerService.feeModule.entity.OrderRecordEntity;

import java.util.List;


/**
 * The persistent class for the c_invoice_info database table.
 * 
 */
@Entity
@Table(name="c_invoice_info")
public class InvoiceInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="bank_account")
	private String bankAccount;

	@Column(name="bank_name")
	private String bankName;

	private String code;

	private String company;

	private String content;

	@Column(name="mail_address")
	private String mailAddress;

	@Column(name="mail_method")
	private String mailMethod;

	private String mobile;

	private String name;

	@Column(name="register_address")
	private String registerAddress;

	@Column(name="register_phone")
	private String registerPhone;

	private String title;

	private int type;


	public InvoiceInfoEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBankAccount() {
		return this.bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMailAddress() {
		return this.mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getMailMethod() {
		return this.mailMethod;
	}

	public void setMailMethod(String mailMethod) {
		this.mailMethod = mailMethod;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegisterAddress() {
		return this.registerAddress;
	}

	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}

	public String getRegisterPhone() {
		return this.registerPhone;
	}

	public void setRegisterPhone(String registerPhone) {
		this.registerPhone = registerPhone;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

}