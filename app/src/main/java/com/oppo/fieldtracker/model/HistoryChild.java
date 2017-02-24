package com.oppo.fieldtracker.model;

/**
 * Created by allsmartlt218 on 16-12-2016.
 */

public class HistoryChild {
    private String comments;
    private String fromDate;
    private String thruDate;
    private int background;
    private int timeSpace;

    public int getTimeSpace() {
        return timeSpace;
    }

    public void setTimeSpace(int timeSpace) {
        this.timeSpace = timeSpace;
    }

    public String getThruDate() {
        return thruDate;
    }

    public void setThruDate(String thruDate) {
        this.thruDate = thruDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    @Override
    public String toString() {
        return this.fromDate + " " + this.comments;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }
}
