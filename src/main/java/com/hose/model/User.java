package com.hose.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {
	
	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String pwd;
	
	@Column(nullable = true)
	private String username;

	@Column(nullable = true)
	private String phone;
	
	@Column(nullable = false)
	private Long typeid;
	
	@Column(nullable = false)
	private Boolean deleteflag;

	protected User() {
	}
	
	public User(Long id, String name, String pwd, String username, String phone) {
		super();
		
		this.id = id;
		this.name = name;
		this.pwd = pwd;
		this.username = username;
		this.phone = phone;
		this.typeid = (long) 2;
		this.deleteflag = false;
	}

	public User(Long id, String name, String pwd, String username, String phone, Long typeid, Boolean deleteflag) {
		super();
		this.id = id;
		this.name = name;
		this.pwd = pwd;
		this.username = username;
		this.phone = phone;
		this.typeid = typeid;
		this.deleteflag = deleteflag;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getTypeid() {
		return typeid;
	}

	public void setTypeid(Long typeid) {
		this.typeid = typeid;
	}

	public Boolean getDeleteflag() {
		return deleteflag;
	}

	public void setDeleteflag(Boolean deleteflag) {
		this.deleteflag = deleteflag;
	}
}
