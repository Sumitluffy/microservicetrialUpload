package com.training.userservice.dao;

import java.util.List;

//import org.hibernate.annotations.GeneratorType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.training.userservice.dto.Orders;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	private String userName;

	private String email;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	@OneToOne(cascade = CascadeType.ALL)
	private Address addr;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Payments> payments;

	

	public List<Payments> getPayments() {
		return payments;
	}

	public void setPayments(List<Payments> payments) {
		this.payments = payments;
	}

	public Address getAddr() {
		return addr;
	}

	public void setAddr(Address addr) {
		this.addr = addr;
	}

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

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(Integer userId, String userName, Address addr, String email, String password) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.addr = addr;
		this.email = email;
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", addr=" + addr + ", email=" + email
				+ ", password=" + password + "]";
	}

}
