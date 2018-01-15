package com.mmec.centerService.userModule.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the c_interface database table.
 * 
 */
@Entity
@Table(name="c_interface")
public class InterfaceEntity implements Serializable {
	private static final long serialVersionUID = 1L;
 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="auth_num")
	private String authNum;

	@Column(name="infterface_name")
	private String infterfaceName;

	@Column(name="interface_desc")
	private String interfaceDesc;

	//bi-directional many-to-one association to PlatformCallbackEntity
	@OneToMany(mappedBy="CInterface")
	private List<PlatformCallbackEntity> CPlatformCallbacks;

	public InterfaceEntity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthNum() {
		return this.authNum;
	}

	public void setAuthNum(String authNum) {
		this.authNum = authNum;
	}

	public String getInfterfaceName() {
		return this.infterfaceName;
	}

	public void setInfterfaceName(String infterfaceName) {
		this.infterfaceName = infterfaceName;
	}

	public String getInterfaceDesc() {
		return this.interfaceDesc;
	}

	public void setInterfaceDesc(String interfaceDesc) {
		this.interfaceDesc = interfaceDesc;
	}

	public List<PlatformCallbackEntity> getCPlatformCallbacks() {
		return this.CPlatformCallbacks;
	}

	public void setCPlatformCallbacks(List<PlatformCallbackEntity> CPlatformCallbacks) {
		this.CPlatformCallbacks = CPlatformCallbacks;
	}

	public PlatformCallbackEntity addCPlatformCallback(PlatformCallbackEntity CPlatformCallback) {
		getCPlatformCallbacks().add(CPlatformCallback);
		CPlatformCallback.setCInterface(this);

		return CPlatformCallback;
	}

	public PlatformCallbackEntity removeCPlatformCallback(PlatformCallbackEntity CPlatformCallback) {
		getCPlatformCallbacks().remove(CPlatformCallback);
		CPlatformCallback.setCInterface(null);

		return CPlatformCallback;
	}

}