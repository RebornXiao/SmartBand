package com.xiao.smartband.entity;

import java.io.Serializable;

public class Status implements Serializable{

	/**
	 * 判断是否绑定，如已绑定直接进入主界面
	 */
	private static long serialVersionUID = 1L;
	private int staus;
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public static void setSerialversionuid(long serialversionuid) {
		serialVersionUID = serialversionuid;
	}
	public int getStaus() {
		return staus;
	}
	public void setStaus(int staus) {
		this.staus = staus;
	}
}
