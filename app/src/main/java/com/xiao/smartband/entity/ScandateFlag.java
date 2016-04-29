package com.xiao.smartband.entity;

import java.io.Serializable;

public class ScandateFlag implements Serializable{

	/**
	 * 健康浏览，假如没有网络，日期会保存一次
	 */
	private static long serialVersionUID = 1L;
	private int id;
	private String date;
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public static void setSerialversionuid(long serialversionuid) {
		serialVersionUID = serialversionuid;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
