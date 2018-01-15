package com.mmec.centerService.contractModule.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="c_authority_contract")
public class AuthorityContractEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="authority_contract_path")
	private String authorityContractPath;

	@Column(name="original_filename")
	private String originalFilename;
	
	@Column(name="extension")
	private String extension;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthorityContractPath() {
		return authorityContractPath;
	}

	public void setAuthorityContractPath(String authorityContractPath) {
		this.authorityContractPath = authorityContractPath;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}	
}
