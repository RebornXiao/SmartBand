package com.xiao.smartband.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HealthState implements Serializable {

    /**
     * 今天健康状况
     */
    private static long serialVersionUID = 1L;
    private int id;
    private String bloodPressure;//高压
    private String bloodPressureLow;//低压
    private String bloodSugar;//餐前血糖
    private String bloodSugarAfter;//餐后血糖
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

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

    public String getBloodPressureLow() {
        return bloodPressureLow;
    }

    public void setBloodPressureLow(String bloodPressureLow) {
        this.bloodPressureLow = bloodPressureLow;
    }

    public String getBloodSugarAfter() {
        return bloodSugarAfter;
    }

    public void setBloodSugarAfter(String bloodSugarAfter) {
        this.bloodSugarAfter = bloodSugarAfter;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(String bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

}
