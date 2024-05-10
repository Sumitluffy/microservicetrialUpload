package com.training.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer oid;
private Integer uid;
private String categery;
private String status;
public Order() {
	super();
	// TODO Auto-generated constructor stub
}
public Order(Integer oid, Integer uid, String categery, String status) {
	super();
	this.oid = oid;
	this.uid = uid;
	this.categery = categery;
	this.status = status;
}

public Integer getOid() {
	return oid;
}
public void setOid(Integer oid) {
	this.oid = oid;
}
public Integer getUid() {
	return uid;
}
public void setUid(Integer uid) {
	this.uid = uid;
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
@Override
public String toString() {
	return "Order [oid=" + oid + ", uid=" + uid + ", categery=" + categery + ", status=" + status + "]";
}
}
