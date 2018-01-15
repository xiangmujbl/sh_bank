package com.mmec.centerService.feeModule.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the c_pay_service database table.
 * 
 */
@Entity
@Table(name="c_pay_service")
public class PayServiceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="type_code")
	private String typeCode;

	@Column(name="type_contractname")
	private String typeContractname;

	@Column(name="type_desc")
	private String typeDesc;

	@Column(name="type_name")
	private String typeName;


	public PayServiceEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTypeCode() {
		return this.typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeContractname() {
		return this.typeContractname;
	}

	public void setTypeContractname(String typeContractname) {
		this.typeContractname = typeContractname;
	}

	public String getTypeDesc() {
		return this.typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}