package com.training.userservice.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Address {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;
private String state;
private String city;
private String country;
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public String getState() {
	return state;
}
public void setState(String state) {
	this.state = state;
}
public String getCity() {
	return city;
}
public void setCity(String city) {
	this.city = city;
}
public String getCountry() {
	return country;
}
public void setCountry(String country) {
	this.country = country;
}
public Address() {
	super();
	// TODO Auto-generated constructor stub
}
public Address(Integer id, String state, String city, String country) {
	super();
	this.id = id;
	this.state = state;
	this.city = city;
	this.country = country;
}
@Override
public String toString() {
	return "Address [id=" + id + ", state=" + state + ", city=" + city + ", country=" + country + "]";
}

}
