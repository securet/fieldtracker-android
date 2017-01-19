package com.allsmart.fieldtracker.model;

import android.database.Cursor;

import com.allsmart.fieldtracker.database.SqliteHelper;

/**
 * Created by AllSmart-LT008 on 12/15/2016.
 */

public class TimeInOutDetails {

    private int id;
    private String username="";
    private String clockDate="";
    private String actionType="";
    private String comments="";
    private String latitude="";
    private String longitude="";
    private String actionImage="";
    private String isPushed="";

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getClockDate() {
        return clockDate;
    }
    public void setClockDate(String clockDate) {
        this.clockDate = clockDate;
    }
    public String getActionType() {
        return actionType;
    }
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getActionImage() {
        return actionImage;
    }
    public void setActionImage(String actionImage) {
        this.actionImage = actionImage;
    }
    public String getIsPushed() {
        return isPushed;
    }
    public void setIsPushed(String isPushed) {
        this.isPushed = isPushed;
    }
	static public TimeInOutDetails fromCursor(Cursor cursor) {
        TimeInOutDetails data = new TimeInOutDetails();
		if (cursor != null && cursor.getCount() != 0) {
            data.setId(cursor.getInt(cursor.getColumnIndex(SqliteHelper.COLUMN_ID)));
            data.setUsername(cursor.getString(cursor.getColumnIndex(SqliteHelper.COLUMN_USERNAME)));
            data.setClockDate(cursor.getString(cursor.getColumnIndex(SqliteHelper.COLUMN_CLOCKDATE)));
            data.setActionType(cursor.getString(cursor.getColumnIndex(SqliteHelper.COLUMN_ACTIONTYPE)));
            data.setComments(cursor.getString(cursor.getColumnIndex(SqliteHelper.COLUMN_COMMENTS)));
            data.setLatitude(cursor.getString(cursor.getColumnIndex(SqliteHelper.COLUMN_LATITUDE)));
            data.setLongitude(cursor.getString(cursor.getColumnIndex(SqliteHelper.COLUMN_LONGITUDE)));
            data.setActionImage(cursor.getString(cursor.getColumnIndex(SqliteHelper.COLUMN_ACTIONIMAGE)));
            data.setIsPushed(cursor.getString(cursor.getColumnIndex(SqliteHelper.COLUMN_ISPHUSHED)));
		}
		return data;
	}
}
