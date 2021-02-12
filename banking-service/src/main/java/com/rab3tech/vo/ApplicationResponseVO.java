package com.rab3tech.vo;

import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class ApplicationResponseVO {
	private int code;
	private String staus;
	private String message;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getStaus() {
		return staus;
	}
	public void setStaus(String staus) {
		this.staus = staus;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
