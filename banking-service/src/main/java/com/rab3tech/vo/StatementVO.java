package com.rab3tech.vo;

import java.util.Date;

public class StatementVO {
	private String payeeNamee;
	private Date doe;
	private String remarks;
	private String type;
	private float amount;
	
	@Override
	public String toString() {
		return "statementVO [payeeAccountNo=" + payeeNamee + ", doe=" + doe + ", remarks=" + remarks + ", type="
				+ type + ", amount=" + amount + "]";
	}
	
	
	public String getPayeeName() {
		return payeeNamee;
	}
	public void setPayeeName(String payeeName) {
		this.payeeNamee = payeeName;
	}
	public Date getDoe() {
		return doe;
	}
	public void setDoe(Date date) {
		this.doe = date;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	
	

}
