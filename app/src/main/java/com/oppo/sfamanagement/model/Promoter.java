package com.oppo.sfamanagement.model;

/**
 * Created by allsmartlt218 on 02-12-2016.
 */

public class Promoter {
    private String firstName;
    private String lastName;
    private String phoneNum;
    private String emailAddress;
    private String address;
    private String seAssignment;
    private String storeAssignment;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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
}
