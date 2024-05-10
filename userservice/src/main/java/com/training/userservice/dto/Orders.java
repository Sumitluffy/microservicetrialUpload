package com.training.userservice.dto;

public class Orders {
	private Integer oid;
	private String categery;
	private String status;
	public Integer getOid() {
		return oid;
	}
	public void setOid(Integer oid) {
		this.oid = oid;
	}
	public String getCategery() {
		return categery;
	}
	public void setCategery(String categery) {
		this.categery = categery;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
