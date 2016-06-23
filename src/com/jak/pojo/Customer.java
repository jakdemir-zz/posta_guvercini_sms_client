package com.jak.pojo;

import java.util.List;

public class Customer {
	private long id;
	private String name;
	private String surname;
	private List<String> phoneNoList;
	private List<String> emailAddressList;
	private String address;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<String> getPhoneNoList() {
		return phoneNoList;
	}

	public void setPhoneNoList(List<String> phoneNoList) {
		this.phoneNoList = phoneNoList;
	}

	public List<String> getEmailAddressList() {
		return emailAddressList;
	}

	public void setEmailAddressList(List<String> emailAddressList) {
		this.emailAddressList = emailAddressList;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
