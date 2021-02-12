package com.rab3tech.dao.entity;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="customer_address")
public class Address {

	private int id;
	private String fname;
	private String lname;
	private Login loginid;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String country;
	private Timestamp doe;
	private Timestamp dom;
	private String pnumber;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="loginid", nullable = false)
	public Login getLogin() {
		return loginid;
	}
	public void setLogin(Login login) {
		this.loginid = login;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	public Timestamp getDoe() {
		return doe;
	}
	public void setDoe(Timestamp doe) {
		this.doe = doe;
	}
	public Timestamp getDom() {
		return dom;
	}
	public void setDom(Timestamp dom) {
		this.dom = dom;
	}

	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	
	public String getPnumber() {
		return pnumber;
	}
	public void setPnumber(String pnumber) {
		this.pnumber = pnumber;
	}
	@Override
	public String toString() {
		return "Address [id=" + id + ", fname=" + fname + ", lname=" + lname + ", loginid=" + loginid + ", address1="
				+ address1 + ", address2=" + address2 + ", city=" + city + ", state=" + state + ", zip=" + zip
				+ ", country=" + country + ", doe=" + doe + ", dom=" + dom + ", pnumber=" + pnumber + "]";
	}

	
}
