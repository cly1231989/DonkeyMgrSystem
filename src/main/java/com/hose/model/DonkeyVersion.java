package com.hose.model;

public class DonkeyVersion {
	public DonkeyVersion() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DonkeyVersion(Long id, Integer num, Long version) {
		super();
		this.id = id;
		this.num = num;
		this.version = version;
	}
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	private Integer num;
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	private Long version;
}
