package com.allsmart.fieldtracker.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by allsmartlt218 on 25-01-2017.
 */

public class Manager implements Parcelable {
    private String agentName;
    private String storeName;
    private String status;
    private String phone;
    private String username;

    public Manager() {

    }

    public Manager(String agentName,String storeName,String status) {
        this.agentName = agentName;
        this.storeName = storeName;
        this.status = status;
    }

    protected Manager(Parcel in) {
        agentName = in.readString();
        storeName = in.readString();
        status = in.readString();
        phone = in.readString();
        username = in.readString();
    }

    public static final Creator<Manager> CREATOR = new Creator<Manager>() {
        @Override
        public Manager createFromParcel(Parcel in) {
            return new Manager(in);
        }

        @Override
        public Manager[] newArray(int size) {
            return new Manager[size];
        }
    };

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(agentName);
        dest.writeString(storeName);
        dest.writeString(status);
        dest.writeString(phone);
        dest.writeString(username);
    }
}
