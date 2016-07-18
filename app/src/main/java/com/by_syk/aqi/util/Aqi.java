package com.by_syk.aqi.util;

import android.graphics.Color;

/**
 * Created by By_syk on 2016-07-14.
 */
public class Aqi {
    private String city = null;
    private int aqi = -1;
    private int aqi_grade = -1;
    private String quality = null;
    private int color = Color.TRANSPARENT;
    private String advice = null;
    private String time = null;

    public Aqi() {}

    public Aqi(String city, int aqi, int aqi_grade, String quality, int color, String advice, String time) {
        setCity(city);
        setAqi(aqi);
        setAqiGrade(aqi_grade);
        setQuality(quality);
        setColor(color);
        setAdvice(advice);
        setTime(time);
    }

    public void setCity(String city) {
        if ("null".equals(city)) {
            return;
        }

        this.city = city;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public void setAqiGrade(int aqi_grade) {
        this.aqi_grade = aqi_grade;
    }

    public void setQuality(String quality) {
        if ("null".equals(quality)) {
            return;
        }

        this.quality = quality;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setAdvice(String advice) {
        if ("null".equals(advice)) {
            return;
        }

        this.advice = advice;
    }

    public void setTime(String time) {
        if ("null".equals(time)) {
            return;
        }

        this.time = time;
    }

    public String getCity() {
        return city;
    }

    public int getAqi() {
        return aqi;
    }

    public int getAqiGrade() {
        return aqi_grade;
    }

    public String getQuality() {
        return quality;
    }

    public int getColor() {
        return color;
    }

    public String getAdvice() {
        return advice;
    }

    public String getTime() {
        return time;
    }
}
