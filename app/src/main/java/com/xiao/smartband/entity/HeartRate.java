package com.xiao.smartband.entity;

import java.io.Serializable;

public class HeartRate implements Serializable {
    private static long serialVersionUID = 1L;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String heartRate;
    private int heartRated;
    private String heartDate;
    private String HRpercent;//标准的心率75/分钟

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static void setSerialversionuid(long serialversionuid) {
        serialVersionUID = serialversionuid;
    }

    public int getHeartRated() {
        return heartRated;
    }

    public void setHeartRated(int heartRated) {
        this.heartRated = heartRated;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getHeartDate() {
        return heartDate;
    }

    public void setHeartDate(String heartDate) {
        this.heartDate = heartDate;
    }

    public String getHRpercent() {
        return HRpercent;
    }

    public void setHRpercent(String hRpercent) {
        HRpercent = hRpercent;
    }
}
