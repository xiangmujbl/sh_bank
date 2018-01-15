package com.mmec.centerService.contractModule.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the c_contract_path database table.
 * 
 */
@Entity
@Table(name="c_contract_path")
public class ContractPathEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="att_name")
	private String attName;

	private String extension;

	@Column(name="file_path")
	private String filePath;
	
	@Column(name="original_file_path")
	private String originalFilePath;

	private byte type;

	@Column(name="contract_serial_num")
	private String contractSerialNum;
	
	//bi-directional many-to-one association to ContractEntity
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="contract_id")
	private ContractEntity CContract;

	@Column(name="contract_path")
	private String contractPath;
	
	@Column(name="original_filename")
	private String originalFileName;

	/**
	 * 对接传来的文件路径
	 */
	@Column(name="original_file")
	private String originalFile;	
	
	public String getOriginalFile() {
		return originalFile;
	}

	public void setOriginalFile(String originalFile) {
		this.originalFile = originalFile;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getOriginalFilePath() {
		return originalFilePath;
	}

	public void setOriginalFilePath(String originalFilePath) {
		this.originalFilePath = originalFilePath;
	}

	public ContractPathEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAttName() {
		return this.attName;
	}

	public void setAttName(String attName) {
		this.attName = attName;
	}

	public String getExtension() {
		return this.extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public byte getType() {
		return this.type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public ContractEntity getCContract() {
		return this.CContract;
	}

	public void setCContract(ContractEntity CContract) {
		this.CContract = CContract;
	}
	
	public String getContractSerialNum() {
		return contractSerialNum;
	}

	public void setContractSerialNum(String contractSerialNum) {
		this.contractSerialNum = contractSerialNum;
	}

	public String getContractPath() {
		return contractPath;
	}

	public void setContractPath(String contractPath) {
		this.contractPath = contractPath;
	}

}