package com.xiao.smartband.entity;

import java.io.Serializable;

public class UId implements Serializable{

	/**
	 * 服务器返回的uId
	 */
	private static long serialVersionUID = 1L;
	
	private String uId;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static void setSerialversionuid(long serialversionuid) {
		serialVersionUID = serialversionuid;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

}
