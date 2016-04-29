package com.xiao.smartband.entity;

import java.io.Serializable;

public class ScanFlag implements Serializable{
	private static long serialVersionUID = 1L;
	private int date;
	private int flag;//如果健康扫描已向手环取昨天的数据，标志为1，否则为0
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public static void setSerialversionuid(long serialversionuid) {
		serialVersionUID = serialversionuid;
	}
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}
