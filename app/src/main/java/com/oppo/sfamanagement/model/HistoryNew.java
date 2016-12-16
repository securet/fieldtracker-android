package com.oppo.sfamanagement.model;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 16-12-2016.
 */

public class HistoryNew {
    private String startDate;
    private String endDate;
    private ArrayList<HistoryChild> historyChildren;
    private String date;
    private String timeIn;
    private String timeOut;
    private String hours;

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
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public ArrayList<HistoryChild> getHistoryChildren() {
        return historyChildren;
    }

    public void setHistoryChildren(ArrayList<HistoryChild> historyChildren) {
        this.historyChildren = historyChildren;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
