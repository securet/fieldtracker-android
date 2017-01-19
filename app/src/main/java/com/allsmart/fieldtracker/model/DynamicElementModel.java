package com.allsmart.fieldtracker.model;

/**
 * Created by allsmartlt218 on 12-12-2016.
 */

public class DynamicElementModel {
    private String timeInBlue;
    private String timeOutGray;
    private String outOfLocationRed1;
    private String outOfLocationRed2;
    private String inLocationGreen1;
    private String inLocationGreen2;


    public DynamicElementModel(){}

    public DynamicElementModel(String timeInBlue, String outOfLocationRed1, String inLocationGreen1, String outOfLocationRed2, String inLocationGreen2, String timeOutGray) {
        this.timeInBlue = timeInBlue;
        this.inLocationGreen2 = inLocationGreen2;
        this.inLocationGreen1 = inLocationGreen1;
        this.outOfLocationRed2 = outOfLocationRed2;
        this.outOfLocationRed1 = outOfLocationRed1;
        this.timeOutGray = timeOutGray;
    }

    public String getTimeOutGray() {
        return timeOutGray;
    }

    public void setTimeOutGray(String timeOutGray) {
        this.timeOutGray = timeOutGray;
    }

    public String getInLocationGreen2() {
        return inLocationGreen2;
    }

    public void setInLocationGreen2(String inLocationGreen2) {
        this.inLocationGreen2 = inLocationGreen2;
    }

    public String getInLocationGreen1() {
        return inLocationGreen1;
    }

    public void setInLocationGreen1(String inLocationGreen1) {
        this.inLocationGreen1 = inLocationGreen1;
    }

    public String getOutOfLocationRed2() {
        return outOfLocationRed2;
    }

    public void setOutOfLocationRed2(String outOfLocationRed2) {
        this.outOfLocationRed2 = outOfLocationRed2;
    }

    public String getOutOfLocationRed1() {
        return outOfLocationRed1;
    }

    public void setOutOfLocationRed1(String outOfLocationRed1) {
        this.outOfLocationRed1 = outOfLocationRed1;
    }

    public String getTimeInBlue() {
        return timeInBlue;
    }

    public void setTimeInBlue(String timeInBlue) {
        this.timeInBlue = timeInBlue;
    }
}
