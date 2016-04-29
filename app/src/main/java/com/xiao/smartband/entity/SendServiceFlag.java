package com.xiao.smartband.entity;

import java.io.Serializable;

public class SendServiceFlag implements Serializable{

	/**
	 * 判断上传注册信息，健康扫描信息是否成功，如不成功，则重新提交
	 */
	private static long serialVersionUID = 1L;
	private int registerFlag;
	private int healthScanFlag;
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public static void setSerialversionuid(long serialversionuid) {
		serialVersionUID = serialversionuid;
	}
	public int getRegisterFlag() {
		return registerFlag;
	}
	public void setRegisterFlag(int registerFlag) {
		this.registerFlag = registerFlag;
	}
	public int getHealthScanFlag() {
		return healthScanFlag;
	}
	public void setHealthScanFlag(int healthScanFlag) {
		this.healthScanFlag = healthScanFlag;
	}
	
}
