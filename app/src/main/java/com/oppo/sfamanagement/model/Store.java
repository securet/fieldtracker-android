package com.oppo.sfamanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by allsmartlt218 on 01-12-2016.
 */

public class Store implements Parcelable{
    private int storeId;
    private String storeName;
    private String lattitude;
    private String longitude;
    private String address;
    private String siteRadius;

    public Store() {

    }

    protected Store(Parcel in) {
        storeId = in.readInt();
        storeName = in.readString();
        lattitude = in.readString();
        longitude = in.readString();
        address = in.readString();
        siteRadius = in.readString();
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSiteRadius() {
        return siteRadius;
    }

    public void setSiteRadius(String siteRadius) {
        this.siteRadius = siteRadius;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(storeId);
        dest.writeString(storeName);
        dest.writeString(lattitude);
        dest.writeString(longitude);
        dest.writeString(address);
        dest.writeString(siteRadius);
    }
}
