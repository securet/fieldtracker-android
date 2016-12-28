package com.oppo.sfamanagement.model;

/**
 * Created by allsmartlt218 on 27-12-2016.
 */

public class TimeLine {
    private String fromDate;
    private String thruDate;
    private String difference;
    private long differenceMill;

    public long getDifferenceMill() {
        return differenceMill;
    }

    public void setDifferenceMill(long differenceMill) {
        this.differenceMill = differenceMill;
    }

    public String getDifference() {

        return difference;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }

    public  TimeLine() {

    }
    public TimeLine(String fromDate, String thruDate) {
        this.fromDate = fromDate;
        this.thruDate = thruDate;
    }

    public String getThruDate() {
        return thruDate;
    }

    public void setThruDate(String thruDate) {
        this.thruDate = thruDate;
    }

    public String getFromDate() {

        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }
}
