package com.example.demo.crawler.model;


import java.util.Date;
public class AstronomyDay {
    private Date solarDay;


    private String whichDay;
    private String lunarDay;
    private String solarTerm;
    private String phenomenon;




    public String getWhichDay() {
        return whichDay;
    }

    public void setWhichDay(String whichDay) {
        this.whichDay = whichDay;
    }

    public Date getSolarDay() {
        return solarDay;
    }

    public void setSolarDay(Date solarDay) {
        this.solarDay = solarDay;
    }
    public String getLunarDay() {
        return lunarDay;
    }

    public void setLunarDay(String lunarDay) {
        this.lunarDay = lunarDay;
    }


    public String getSolarTerm() {
        return solarTerm;
    }

    public void setSolarTerm(String solarTerm) {
        this.solarTerm = solarTerm;
    }

    public String getPhenomenon() {
        return phenomenon;
    }

    public void setPhenomenon(String phenomenon) {
        this.phenomenon = phenomenon;
    }

    @Override
    public String toString() {
        return "AstronomyDay{" +
                "solarDay=" + solarDay +
                ", lunarDay='" + lunarDay + '\'' +
                ", solarTerm='" + solarTerm + '\'' +
                ", phenomenon='" + phenomenon + '\'' +
                '}';
    }
}
