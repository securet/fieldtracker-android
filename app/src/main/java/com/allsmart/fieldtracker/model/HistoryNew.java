package com.allsmart.fieldtracker.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 16-12-2016.
 */

public class HistoryNew implements Parcelable {
    private String startDate;
    private String endDate;
    private ArrayList<HistoryChild> historyChildren;
    private String date;
    private HistoryChild historyChild;
    private String timeIn;
    private String timeOut;
    private String hours;

    public HistoryChild getHistoryChild() {
        return historyChild;
    }

    public void setHistoryChild(HistoryChild historyChild) {
        this.historyChild = historyChild;
    }

    public static Creator<HistoryNew> getCREATOR() {
        return CREATOR;
    }

    public HistoryNew() {

    }

    protected HistoryNew(Parcel in) {
        startDate = in.readString();
        endDate = in.readString();
        date = in.readString();
        timeIn = in.readString();
        timeOut = in.readString();
        hours = in.readString();
    }

    public static final Creator<HistoryNew> CREATOR = new Creator<HistoryNew>() {
        @Override
        public HistoryNew createFromParcel(Parcel in) {
            return new HistoryNew(in);
        }

        @Override
        public HistoryNew[] newArray(int size) {
            return new HistoryNew[size];
        }
    };

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
        this.timeIn = "Time In: " + timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = "Time Out: " + timeOut;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(date);
        dest.writeString(timeIn);
        dest.writeString(timeOut);
        dest.writeString(hours);
    }
}
