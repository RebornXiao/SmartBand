package com.xiao.smartband.entity;

import java.io.Serializable;

public class Mac implements Serializable{

	private static long serialVersionUID = 1L;
	private String mac;
	private String psword;
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public static void setSerialversionuid(long serialversionuid) {
		serialVersionUID = serialversionuid;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getPsword() {
		return psword;
	}
	public void setPsword(String psword) {
		this.psword = psword;
	}

}
