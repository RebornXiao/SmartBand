package com.xiao.smartband.entity;

import java.io.Serializable;

public class Push implements Serializable{
	private static long serialVersionUID = 1L;
	private int id;
	private String date;
	private String content;
	private String title;
	private String serverTime;
	private int collect;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public static void setSerialversionuid(long serialversionuid) {
		serialVersionUID = serialversionuid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getServerTime() {
		return serverTime;
	}
	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}
	public int getCollect() {
		return collect;
	}
	public void setCollect(int operate) {
		this.collect = operate;
	}
	
}
