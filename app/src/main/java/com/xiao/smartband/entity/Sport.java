package com.xiao.smartband.entity;

import java.io.Serializable;

public class Sport implements Serializable{
	private static long serialVersionUID = 1L;
	private int id;
	private String date;
	private String step;
	private String stepRate;
	private String sportTime;
	private String koloria;
	public String getSportTime() {
		return sportTime;
	}
	public void setSportTime(String sportTime) {
		this.sportTime = sportTime;
	}
	public String getKoloria() {
		return koloria;
	}
	public void setKoloria(String koloria) {
		this.koloria = koloria;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	public String getStepRate() {
		return stepRate;
	}
	public void setStepRate(String stepRate) {
		this.stepRate = stepRate;
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
