package com.xiao.smartband.entity;

import java.io.Serializable;

public class Sleep implements Serializable{

	private static long serialVersionUID = 1L;
	private int id;
	private String sleepDeep;
	private String sleepTime;//浅睡时间
	private String sleepDate;
	private String sleepPercent;
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
	public String getSleepDeep() {
		return sleepDeep;
	}
	public void setSleepDeep(String sleepDeep) {
		this.sleepDeep = sleepDeep;
	}
	public String getSleepTime() {
		return sleepTime;
	}
	public void setSleepTime(String sleepTime) {
		this.sleepTime = sleepTime;
	}
	public String getSleepDate() {
		return sleepDate;
	}
	public void setSleepDate(String sleepDate) {
		this.sleepDate = sleepDate;
	}
	public String getSleepPercent() {
		return sleepPercent;
	}
	public void setSleepPercent(String sleepPercent) {
		this.sleepPercent = sleepPercent;
	}

}
