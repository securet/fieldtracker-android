package com.allsmart.fieldtracker.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class PromoterApprovals implements Parcelable{
    private String firstName;
    private String requestId;
    private String requestType;
    private String lastName;
    private String phoneNum;
    private String emailAddress;
    private String address;
    private String statusId;
    private String seAssignment;
    private String storeAssignment;
    private String productStoreId;
    private String userPhoto;
    private String addressIdPath;
    private String aadharIdPath;
    private ArrayList<Promoter> promoterArrayList;
    public PromoterApprovals() {

    }

    protected PromoterApprovals(Parcel in) {
        firstName = in.readString();
        requestId = in.readString();
        requestType = in.readString();
        lastName = in.readString();
        phoneNum = in.readString();
        emailAddress = in.readString();
        address = in.readString();
        statusId = in.readString();
        seAssignment = in.readString();
        storeAssignment = in.readString();
        productStoreId = in.readString();
        userPhoto = in.readString();
        addressIdPath = in.readString();
        aadharIdPath = in.readString();
        promoterArrayList = in.createTypedArrayList(Promoter.CREATOR);
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
        dest.writeString(statusId);
        dest.writeString(seAssignment);
        dest.writeString(storeAssignment);
        dest.writeString(productStoreId);
        dest.writeString(userPhoto);
        dest.writeString(addressIdPath);
        dest.writeString(aadharIdPath);
        dest.writeTypedList(promoterArrayList);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getSeAssignment() {
        return seAssignment;
    }

    public void setSeAssignment(String seAssignment) {
        this.seAssignment = seAssignment;
    }

    public String getStoreAssignment() {
        return storeAssignment;
    }

    public void setStoreAssignment(String storeAssignment) {
        this.storeAssignment = storeAssignment;
    }

    public String getProductStoreId() {
        return productStoreId;
    }

    public void setProductStoreId(String productStoreId) {
        this.productStoreId = productStoreId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getAddressIdPath() {
        return addressIdPath;
    }

    public void setAddressIdPath(String addressIdPath) {
        this.addressIdPath = addressIdPath;
    }

    public String getAadharIdPath() {
        return aadharIdPath;
    }

    public void setAadharIdPath(String aadharIdPath) {
        this.aadharIdPath = aadharIdPath;
    }

    public ArrayList<Promoter> getPromoterArrayList() {
        return promoterArrayList;
    }

    public void setPromoterArrayList(ArrayList<Promoter> promoterArrayList) {
        this.promoterArrayList = promoterArrayList;
    }
}
