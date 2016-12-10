package com.oppo.sfamanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class Promoter implements Parcelable{
    private String firstName;
    private String requestId;
    private String requestType;
    private String lastName;
    private String phoneNum;
    private String emailAddress;
    private String address;
    private String seAssignment;
    private String storeAssignment;

    public Promoter() {

    }

    protected Promoter(Parcel in) {
        firstName = in.readString();
        requestId = in.readString();
        requestType = in.readString();
        lastName = in.readString();
        phoneNum = in.readString();
        emailAddress = in.readString();
        address = in.readString();
        seAssignment = in.readString();
        storeAssignment = in.readString();
    }

    public static final Creator<Promoter> CREATOR = new Creator<Promoter>() {
        @Override
        public Promoter createFromParcel(Parcel in) {
            return new Promoter(in);
        }

        @Override
        public Promoter[] newArray(int size) {
            return new Promoter[size];
        }
    };

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getStoreAssignment() {
        return storeAssignment;
    }

    public void setStoreAssignment(String storeAssignment) {
        this.storeAssignment = storeAssignment;
    }

    public String getSeAssignment() {
        return seAssignment;
    }

    public void setSeAssignment(String seAssignment) {
        this.seAssignment = seAssignment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(requestId);
        dest.writeString(requestType);
        dest.writeString(lastName);
        dest.writeString(phoneNum);
        dest.writeString(emailAddress);
        dest.writeString(address);
        dest.writeString(seAssignment);
        dest.writeString(storeAssignment);
    }
}
