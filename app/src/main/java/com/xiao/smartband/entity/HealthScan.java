package com.xiao.smartband.entity;

import java.io.Serializable;

public class HealthScan implements Serializable {

    private static long serialVersionUID = 1L;
    private int id;
    private String HSsteps;
    private String HSheart;
    private String HSkaloria;
    private String HSsleepDeep;
    private String HSstepTime;
    private String HShealthScore;
    private String HShealthDate;
    private String HSstepsPer;
    private String HSsleepDeeper;
    private String HSheartRatePer;
    private String HShealthAsses;//健康浏览评价

    public String getHShealthAsses() {
        return HShealthAsses;
    }

    public void setHShealthAsses(String hShealthAsses) {
        HShealthAsses = hShealthAsses;
    }

    public String getHSstepsPer() {
        return HSstepsPer;
    }

    public void setHSstepsPer(String hSstepsPer) {
        HSstepsPer = hSstepsPer;
    }

    public String getHSsleepDeeper() {
        return HSsleepDeeper;
    }

    public void setHSsleepDeeper(String hSsleepDeeper) {
        HSsleepDeeper = hSsleepDeeper;
    }

    public String getHSheartRatePer() {
        return HSheartRatePer;
    }

    public void setHSheartRatePer(String hSheartRatePer) {
        HSheartRatePer = hSheartRatePer;
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

    public String getHSsteps() {
        return HSsteps;
    }

    public void setHSsteps(String hSsteps) {
        HSsteps = hSsteps;
    }

    public String getHSheart() {
        return HSheart;
    }

    public void setHSheart(String hSheart) {
        HSheart = hSheart;
    }

    public String getHSkaloria() {
        return HSkaloria;
    }

    public void setHSkaloria(String hSkaloria) {
        HSkaloria = hSkaloria;
    }

    public String getHSsleepDeep() {
        return HSsleepDeep;
    }

    public void setHSsleepDeep(String hSsleepDeep) {
        HSsleepDeep = hSsleepDeep;
    }

    public String getHSstepTime() {
        return HSstepTime;
    }

    public void setHSstepTime(String hSstepTime) {
        HSstepTime = hSstepTime;
    }

    public String getHShealthScore() {
        return HShealthScore;
    }

    public void setHShealthScore(String hShealthScore) {
        HShealthScore = hShealthScore;
    }

    public String getHShealthDate() {
        return HShealthDate;
    }

    public void setHShealthDate(String hShealthDate) {
        HShealthDate = hShealthDate;
    }


}
