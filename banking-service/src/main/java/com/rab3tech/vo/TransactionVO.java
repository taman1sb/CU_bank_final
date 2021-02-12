package com.rab3tech.vo;

import java.util.Date;

public class TransactionVO {
	private int payeeID;
	private String debitAccountNumber;
	private String description;
	private String customerId;
	private String type;
	private Date doe;
	private float transferAmount;
	private String payeeName;
	
	public int getPayeeID() {
		return payeeID;
	}
	public void setPayeeID(int payeeID) {
		this.payeeID = payeeID;
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
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public float getTransferAmount() {
		return transferAmount;
	}
	public void setTransferAmount(float transferAmount) {
		this.transferAmount = transferAmount;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getDoe() {
		return doe;
	}
	public void setDoe(Date doe) {
		this.doe = doe;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	
	
}
