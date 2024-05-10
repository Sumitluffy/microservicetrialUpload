package com.training.userservice.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Payments {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer paymentid;
private String paymentType;
private String status;
public Integer getPaymentid() {
	return paymentid;
}
public void setPaymentid(Integer paymentid) {
	this.paymentid = paymentid;
}
public String getPaymentType() {
	return paymentType;
}
public void setPaymentType(String paymentType) {
	this.paymentType = paymentType;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}


}
