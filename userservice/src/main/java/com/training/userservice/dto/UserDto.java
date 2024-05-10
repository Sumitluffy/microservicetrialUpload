package com.training.userservice.dto;

import java.util.List;

import com.training.userservice.dao.Address;
import com.training.userservice.dao.Payments;

public class UserDto {
	private Integer userId;
	private String userName;
    private String email;
    private String password;
	private Address addr;
	private List<Payments> payments;
	private List<Orders> orders;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Address getAddr() {
		return addr;
	}
	public void setAddr(Address addr) {
		this.addr = addr;
	}
	public List<Payments> getPayments() {
		return payments;
	}
	public void setPayments(List<Payments> payments) {
		this.payments = payments;
	}
	public List<Orders> getOrders() {
		return orders;
	}
	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}
	
}
