package com.rab3tech.dao.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "customer_account_transaction_tbl")
public class TransactionEntity {
	
	private int id;
	private PayeeInfo payeeId;
	private String debitAccountNumber;
	private String description;
	private float amount;
	private Date DOE;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="payeeId", nullable = false)
	public PayeeInfo getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(PayeeInfo payeeId) {
		this.payeeId = payeeId;
	}
	public String getDebitAccountNumber() {
		return debitAccountNumber;
	}
	public void setDebitAccountNumber(String debitAccountNumber) {
		this.debitAccountNumber = debitAccountNumber;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public Date getDOE() {
		return DOE;
	}
	public void setDOE(Date dOE) {
		DOE = dOE;
	}
	
	

}
