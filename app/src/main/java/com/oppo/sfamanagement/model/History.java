package com.oppo.sfamanagement.model;

/**
 * Created by allsmartlt218 on 01-12-2016.
 */

public class History {

    private String date;
    private String timeIn;
    private String timeOut;
    private String time;

    public History() {

    }
    public History(String date,String timeIn,String timeOut,String time) {
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        String time = "Time In: " + timeIn;
        this.timeIn = time;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        String time = "Time Out: " + timeOut;
        this.timeOut = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
