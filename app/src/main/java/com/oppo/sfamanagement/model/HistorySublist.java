package com.oppo.sfamanagement.model;

/**
 * Created by allsmartlt218 on 12-12-2016.
 */

public class HistorySublist {
    private String color;
    private String time;
    private String timeIn;

    public HistorySublist(String s, String s1) {
        this.time = s;
        this.timeIn = s1;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }
}
