package com.oppo.sfamanagement.model;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class Leave {
    private String days;
    private String fromDate;
    private String toDate;
    private String status;
    private String reason;

    public String getDays() {
        return days;
    }

    public void setDays(String days) {

        this.days = days;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        String to = "To: " + toDate;
        this.toDate = to;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        String from = "From: " + fromDate;
        this.fromDate = from;
    }
}
